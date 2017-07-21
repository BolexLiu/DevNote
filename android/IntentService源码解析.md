上一篇我们分析了[Android中的线程间通信HandlerThread](http://www.jianshu.com/p/69c826c8a87d)的原理.`HandlerThread`充分的利用Handler的通信机制和消息队列。本篇将分析`IntentService`的作用和原理。

**警告：**本篇的源码可能过于枯燥和乏味，中间涉及到一次采坑，错误的分析。最后纠正回来了。我觉得此处是比较有意义的。读者对着源码的同时细读本篇可能更好一点。

## 目录
- IntentService简单介绍
- 源码分析
- 续错误纠正

-----

## IntentService
`IntentService`继承自`Service`本质上就是一个服务。但它内部拥有一个完整的`HandlerThread`。可以这样说`IntentService=Service+HandlerThread。`

我们先来说下它的常见用法。将复杂耗时操作交由`IntentService`来处理，你可以通过`Intent`的方式启动它，然后实现`onHandleIntent`方法，在`onHandleIntent`的任何操作将属于内部`HandlerThread`的子线程。

**代码如下：**

继承一个IntentService
```
public class CustomIntentService extends IntentService {
 public CustomIntentService() {  super("CustomIntentService"); }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String msg = intent.getStringExtra("msg");
        Log.d("IntentService", "耗时操作" + msg);
    }

    @Override
    public void onDestroy() {
        Log.d("IntentService", "onDestroy" );
        super.onDestroy();
    }
}
```
启动它，并附带一个消息
```
 Intent intent = new Intent(MainActivity.this, CustomIntentService.class);
                intent.putExtra("msg","hello");
                startService(intent);
```
这里我们启动了`CustomIntentService`并附带了一个`hello`过去。此时`onHandleIntent方法`会接受到我们这个`Intent`，并模拟耗时后打印日志。
**注意两点**
- 1.这里的`Intent`是间接性传递过去的，按通常的思路这个`Intent`是在主线程中，但是这里并不是主线程。后面我们会从源码上来分析。
- 2.我们知道`IntentService`的特性会执行完任务后自动销毁。但有一种情况，如果我们快速调用两次`startService`会如何？下面是快速调用两次的日志。

```
07-20 21:10:57.490 5895-5977IntentService: 耗时操作hello
07-20 21:11:02.480 5895-5977IntentService: 耗时操作hello
07-20 21:11:02.480 5895-5895IntentService: onDestroy
```
显然，并不是说每次执行都会销毁掉，当第二条消息过来的时候它并没有销毁，而是做完后才销毁。但是这是为什么呢？先卖个关子，我们带着疑问去看源码吧。

## 源码分析：
注：*此处源码删除了一些不影响阅读的注释和方法*
```
public abstract class IntentService extends Service {
    private volatile Looper mServiceLooper;
    private volatile ServiceHandler mServiceHandler;
    private String mName;
    private boolean mRedelivery;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            onHandleIntent((Intent)msg.obj);
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread("IntentService[" + mName + "]");
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = intent;
        mServiceHandler.sendMessage(msg);
    }

    @Override
    public void onDestroy() {
        mServiceLooper.quit();
    }
    protected abstract void onHandleIntent(@Nullable Intent intent);
}

```
- 1.`IntentService` 内部总共有四个成员变量，我们只需要关注`mServiceLooper`和`mServiceHandler`即可。
- 2.首先我们看`onCreate方法`，它创建了一个`HandlerThread`并向`ServiceHandler`传递了一个`Looper`对象。
- 3.`ServiceHandler`的`handleMessage`内逻辑很简单，它调用了`onHandleIntent`这个虚拟方法（抽象方法）。并从中取出`msg.obj`。可以看到它就是一个`Intent`，接着调用了`stopSelf`方法携带了`msg.arg1（starId）`，来终止服务。
- 4.onStart方法会率先接受到我们启动服务的`Intent`对象，他将该对象最终使用`mServiceHandler`发送给`HandlerThread`内部的`Looper`，交由子线程来处理这个消息，所以我们需要重写`onHandleIntent`来实现自己的需求。
HandlerThread原理可参考：[HandlerThread线程间通信 源码解析](http://www.jianshu.com/p/69c826c8a87d)

自此流程就梳理完了，现在我们回到前面提到的问题，当快速两次启动`IntentService`时，他发生了什么。
1.由内部的 `hander`接受到第一条消息，在`onHandleIntent`里阻塞，立刻第二条消息进入。
2.第一条消息的`StopSelf方法`被调用。此时第二条消息还在处理中。
3.`StopSelf方法`携带了`startId`调用了。`ActivityManager`的`stopServiceToken`来停止服务，我们接着来看一下源码。

源码路径**/core/android/app/ActivityManagerNative.java**
```
   public boolean stopServiceToken(ComponentName className, IBinder token,
            int startId) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(IActivityManager.descriptor);
        ComponentName.writeToParcel(className, data);
        data.writeStrongBinder(token);
        data.writeInt(startId);
        mRemote.transact(STOP_SERVICE_TOKEN_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }
```
可以看到入参里携带了`ComponentName Binder`和`StartId`，前面没有讲到，这里补充一下，`StartId` 是每次启动服务时都会携带过来的一个标记，它用来表示该服务在终止以前被启动了多少次。而后`stopServiceToken`方法将`startId`和和`Binder`一并写入了`Parcel`对象内。很抱歉，分析到这里翻车了，我没法再根据调试跟进去，断点下了是一把红叉。如果有大神知道这里如何动态调这块，还请告诉我一声，感激不尽。（或者我弄错了这里根本不是这样调的。）

不过这里已经大致能说明通过`Binder`传递了消息  `mRemote.transact(STOP_SERVICE_TOKEN_TRANSACTION, data, reply, 0);`来暂停服务。~~`mRemote`就是`ActivityManagerNative`~~（错误）我们再顺着思路找到了`onTransact`方法里的`case`语句
```
    case STOP_SERVICE_TOKEN_TRANSACTION: {
            data.enforceInterface(IActivityManager.descriptor);
            ComponentName className = ComponentName.readFromParcel(data);
            IBinder token = data.readStrongBinder();
            int startId = data.readInt();
            boolean res = stopServiceToken(className, token, startId);
            reply.writeNoException();
            reply.writeInt(res ? 1 : 0);
            return true;
        }
```
~~非常有意思的是我们可以发现`stopServiceToken方法`是一个递归调用。此时我更加懵了。没有找到`return `的点，这将会是死循环。~~是不是思路错误了。

**但是通过上层日志看出来，当IntentService里有消息存在时它不会结束掉。一定是IntentService内部消息全部被消耗后才会结束。**

今天先到这里，我们来日弄明白了底层如何实现的后再战。

-----

## 续错误纠正

前面的问题我们今天找到答案了。实际上面讲到的`mRemote`并非`ActivityManagerNative`，而是代理对象。以至于我误认为他们在递归调用。这是不对的。`mRemote`实际是`ActivityManagerProxy`，他是一个本地代理对象，而经过`transact`调用实际运行的是远端的`ActivityManagerService`中，这里牵扯到了`AMS`与`ActivityManager`通信流程，我们后续再单独分析。先把`IntentService`给弄明白。
源码地址(需要翻墙)：[ActivityManagerService](https://android.googlesource.com/platform/frameworks/base/+/4f868ed/services/core/java/com/android/server/am/ActivityManagerService.java)

我们发现在`ActivityManagerService`里调用的`stopServiceToken`调用了`mServices.stopServiceTokenLocked`方法。
```
  @Override
    public boolean stopServiceToken(ComponentName className, IBinder token,
            int startId) {
        synchronized(this) {
            return mServices.stopServiceTokenLocked(className, token, startId);
        }
    }
 ```
这里的`mServices`是`ActiveServices`，我们跟进去看。
```
 ServiceLookupResult res =retrieveServiceLocked(service, resolvedType, callingPackage,
                    callingPid, callingUid, userId, true, callerFg, false);

ServiceRecord r = res.record;

 boolean stopServiceTokenLocked(ComponentName className, IBinder token,
            int startId) {
        if (r != null) {
            if (startId >= 0) {
                ServiceRecord.StartItem si = r.findDeliveredStart(startId, false);
                if (si != null) {
                    while (r.deliveredStarts.size() > 0) {
                        ServiceRecord.StartItem cur = r.deliveredStarts.remove(0);
                        cur.removeUriPermissionsLocked();
                        if (cur == si) {
                            break;
                        }
                    }
                }

                if (r.getLastStartId() != startId) {
                    return false;
                }
                if (r.deliveredStarts.size() > 0) {
                    Slog.w(TAG, "stopServiceToken startId " + startId
                            + " is last, but have " + r.deliveredStarts.size()
                            + " remaining args");
                }
            }

            synchronized (r.stats.getBatteryStats()) {
                r.stats.stopRunningLocked();
            }
            r.startRequested = false;
            if (r.tracker != null) {
                r.tracker.setStarted(false, mAm.mProcessStats.getMemFactorLocked(),
                        SystemClock.uptimeMillis());
            }
            r.callStart = false;
            final long origId = Binder.clearCallingIdentity();
            bringDownServiceIfNeededLocked(r, false, false);
            Binder.restoreCallingIdentity(origId);
            return true;
        }
        return false;
    }

```
我们可以看到最关键的`if (r.getLastStartId() != startId) `不是最后一个startId就直接`return false`。否则将执行  `r.stats.stopRunningLocked();`来终止。自此这个问题终于真相大白了。

回过头了再理一遍。我粗略的画了一张图。顺序从上往下看。

![](http://upload-images.jianshu.io/upload_images/1110736-2fcc9db226717c78.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



IntentService的源码本身并不复杂，读者不要被我深究stopSelf方法牵扯到AMS里给绕晕了。AMS这块后续我会单独做文章分析，这一块特别重要。

如果本篇看得比较云雾头疼，可以先去熟悉下面两篇。
[HandlerThread线程间通信 源码解析](http://www.jianshu.com/p/69c826c8a87d)
[Handler消息源码流程分析（含手写笔记）](http://www.jianshu.com/p/6f25729ef62a)



下一篇，我们将分析ThreadPoolExecutor线程池。





本文参考：

- [AndroidFramework官方源码](https://github.com/android/platform_frameworks_base)
- 《Android开发艺术探索》


---
# 如何下次找到我?
- 关注我的简书
- 本篇同步Github仓库:[https://github.com/BolexLiu/MyNote](https://github.com/BolexLiu/MyNote)  (可以关注)
![](http://upload-images.jianshu.io/upload_images/1110736-f0a700624e0723ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)