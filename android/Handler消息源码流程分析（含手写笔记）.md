`Handler`在android开发中可谓随处可见，不论你是一个刚开始学习android的新人，还是昔日的王者，都离不开它。关于 `handler`的源码已经很前人分享过了。如果我没能给大家讲明白可以参考网上其他人写的。

注：*文中所贴源码都有精简过，并非完整源码，只保留主思路，删减了一些非法校验细节实现*


## 目录
- 简单使用方法
- 源码流程分析

----

###  简单使用方法
应用层开发时`handle`常要用于线程切换调度和异步消息、更新UI等，但不仅限于这些。

 使用方法：略

![](http://upload-images.jianshu.io/upload_images/1110736-5960ce07804cfe30.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

哇哈哈哈，不要打我。为了不占用篇幅，想必识标题来者理当熟悉。若有不明之处且看其他偏基础点的教程便可。

----
###  源码流程分析

大王，且先随我看小的从网上盗来的一张图。`handler`发送`Message`（消息）至`MessageQueue`（模拟队列），由`Looper`（循环器）不断循环取出。然后通知`Handler`处理。这便是整个的消息机制。没有多复杂。
![](http://upload-images.jianshu.io/upload_images/1110736-83855d7599cae90d.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 关键对象源码分析：

- Looper 消息轮训器
- MessageQueue 消息暂存队列(单链表结构)
- Message 消息
- Handler 收发消息工具
- ThreadLocal (本地线程数据存储对象)

**ThreadLocal**

先说`ThreadLocal`的作用是不同的线程拥有该线程独立的变量，同名对象不会被受到不同线程间相互使用出现异常的情况。

即：*你的程序拥有多个线程，线程中要用到相同的对象，但又不允许线程之间操作同一份对象。那么就可以使用`ThreadLocal`来解决。它可以在线程中使用`mThreadLocal.get()`和`mThreadLocal.set()`来使用。若未被在当前线程中调用`set`方法，那么`get`时为空。*

在Looper中是一个静态变量的形式存在，并在每个线程中拥有独立的Looper对象，没有则为空。
`    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();`
如若还不明白可单独做了解，弄明白这个是必须的，否则后面会云里雾里。不知为何`hanlder`可以做到跨线程消息切换。我姑且当做读者已熟悉这点。
这里`Looper`是最为重要的一环，我们先来看这个，其余几个对象源码分析的意义不大，后面小节会在消息流程中分析到。就省略了。如果非要纠结解析可以自己去翻阅一下源码即可。



**Looper 关键源码**


记住了，`Looper`主要做两件事。`1.创建消息队列。 2.循环取队列中的消息分发。`后面一个小节会讲什么时候创建，见流程分析。

**构造函数**
在 `Looer`创建的时候初始化了`MessageQueue`
``` java
    private Looper(boolean quitAllowed) {
        mQueue = new MessageQueue(quitAllowed);//创建消息队列
        mThread = Thread.currentThread(); //获取当前线程
    }
```

**创建Looper **
其中分为在主线程中创建，和子线程创建，但都是借助`ThreadLocal`来实现跨线程`Looper`对象的存储。
```
    private static void prepare(boolean quitAllowed) {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper(quitAllowed));
    }
```

**开启循环**
可以看到`loop方法`中是一个死循环在不断的取消息。但注意当无消息时循环并不会做无用功，而是阻塞等待消息。
``` java
    public static void loop() {
        final Looper me = myLooper();
        final MessageQueue queue = me.mQueue;
        for (;;) {
            Message msg = queue.next(); // 取出消息，无消息则阻塞
          if (msg == null) {  return;   }
        msg.target.dispatchMessage(msg);//发送消息 其中target就是Handler
        }
    }
```




#### 消息流程分解：
**主线程中Looper的创建**
- 1.`ActivityThread`创建在`main方法`中调用`Looper.prepareMainLooper();`创建出`Looper`，而后将`创建的Looper`存于线程变量中(`ThreadLocal`)，再将主线程中的`Looper`单独存一份，因为他是主线程的`Looper`。（实际它调用的是`Looper.prepare()`,我们也可以在子线程中使用时，用它来创建`Looper`）。

- 2.在`main方法`的最后调用` Looper.loop();`来开启循环。

```
    public static void main(String[] args) {
        Looper.prepareMainLooper();
        ActivityThread thread = new ActivityThread();
        thread.attach(false);
        Looper.loop();
    }
```
**这便是，为什么`handler`可以做异步的原因了，因为在主UI线程创建的时候，就早已为UI线程创建了一个`Looper`，并开启了循环。**

注：*请思考一个问题，能不能在子线程中直接`new handler`发送消息？如果不可以？有没有办法解决？（切莫去搜一下看到某行代码添加完就可以了便不管为什么这样了，应当分析内部原理。）*

**handler如何收发消息**

- 1.`new Handler()`时构造方法从`Looper.myLooper();`获取当前线程中的`Looper`对象，然后取出`MessageQueue`对象,以备后面发消息用。

```

 public Handler(Callback callback, boolean async) {
        mLooper = Looper.myLooper();
        mQueue = mLooper.mQueue;
        mCallback = callback;
        mAsynchronous = async;
    }
```

-  2 `handler`通过`sendMessage(msg)`将消息发出，消息最终走向`queue.enqueueMessage(msg, uptimeMillis);`这里的`queque`便是我们前面从`handler`构造方法中`Looper`里取到消息队列。
```
private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
        msg.target = this;
        return queue.enqueueMessage(msg, uptimeMillis);
    }
```
- 3.`enqueueMessage方法`里其中还会将当前发消息的`handler`存于`msg`的`target`中。当`Looper`轮训到这条消息时，便会使用到。我们往下再看一眼之前`Looper.loop()`方法。最终调用了 `msg.target.dispatchMessage(msg);`
```
   public static void loop() {
        final Looper me = myLooper();
        final MessageQueue queue = me.mQueue;
        for (;;) {
            Message msg = queue.next(); // 取出消息，无消息则阻塞
          if (msg == null) {  return;   }
        msg.target.dispatchMessage(msg);//发送消息 其中target就是Handler
        }
    }
```
4.自此`dispatchMessage()`  中调用`handleMessage(msg);`回调。消息就送达了。收工。
```
   public void dispatchMessage(Message msg) {
        if (msg.callback != null) {
            handleCallback(msg);
        } else {
            if (mCallback != null) {
                if (mCallback.handleMessage(msg)) {
                    return;
                }
            }
            handleMessage(msg);
        }
    }
```

再来看看这幅图，按照我上面的思路流程在跟着图走一走。看看我们分析得是否正确。

![](http://upload-images.jianshu.io/upload_images/1110736-bd40b3c75e82c6a7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



最后附一张以前学习Hanlder手写笔记，其实这份笔记光看的话，可能对读者没什么很大做用。但对我的帮助很大。我要表达的是一个学习思路，像类似这种源码分析最好自己拿笔写写画画，映象会深刻很多。知识过久了会忘记，光靠死记若非常人，很难过目不忘。自己写一遍就完全不一样了，就算过了许久已淡忘这些，打开自己的笔记看一眼就会明白。而不用从头来学一遍。

用我的理解来解释这种现象是学习的过程中可能坑坑洼洼，消磨掉不少时间。这种笔记会成为最后总结出来的结晶，与脑子里的印象流关联在一起。何必再费力气每次都从头温习，不如直接看以前自己的总结岂不快哉？
![](http://upload-images.jianshu.io/upload_images/1110736-45f02048f1b37a13.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1480)
先生曰：*小伙子长得眉目清新秀，这字真是丑得像鸡爪子爬，各位受委屈了。*

下一篇我们将分解[HandlerThread的工作原理](http://www.jianshu.com/p/69c826c8a87d)和做用。


本文参考：
[AndroidFramework官方源码](https://github.com/android/platform_frameworks_base)
[Handler 之 源码解析](https://github.com/maoruibin/HandlerAnalysis)


---
# 如何下次找到我?
- 关注我的简书
- 本篇同步Github仓库:[https://github.com/BolexLiu/MyNote](https://github.com/BolexLiu/MyNote)  (可以关注)
![](http://upload-images.jianshu.io/upload_images/1110736-f0a700624e0723ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)