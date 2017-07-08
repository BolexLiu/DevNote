![](http://upload-images.jianshu.io/upload_images/1110736-c2fd87c2a7ae360c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

“黑科技什么的最喜欢了！
对，我们就是要搞事。
来呀。谁怕谁。三年血赚,死刑不亏。(๑´ڡ`๑) ”
                                 -- 来自暗世界android工程师

![](http://upload-images.jianshu.io/upload_images/1110736-466ab79147c65008.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

前言：
这个世界上手机有三大系统，苹果、 安卓、 *中国安卓*  。本篇强烈呼吁大家不要去做哪些违反用户体验的黑科技功能，研究研究玩玩就好了啦。全当增长技术，在真实的项目开发中尽量能不用就不要用得好。道理大家都懂的。

# 目录
[那些年Android黑科技①:只要活着，就有希望](http://www.jianshu.com/p/cb2deed0f2d8)
- android应用内执行shell
- 双进程保活aidl版
- 双进程保活jni版
- 保活JobService版

[那些年Android黑科技②:欺骗的艺术](http://www.jianshu.com/p/2ad105f54d07)
- hook技术
- 欺骗系统之偷梁换柱

[那些年Android黑科技③:干大事不择手段 ](http://www.jianshu.com/p/8f9b44302139)
- 应用卸载反馈
- Home键监听
- 桌面添加快捷方式
- 无法卸载app(DevicePolicManager)
- 无网络权限偷偷上传数据


---
## android应用内执行shell
android系统本身是Linux作为内核，我们一般开发中使用 adb shell 命令来操作。但其实本身在应用内也是可以执行的。强大的地方是在root的情况下，可以实现静默安装和操作一切你想在设备内做事情。其方法如下。

调用工具代码：
```
    /**
     * 是否是在root下执行命令
     *
     * @param commands        命令数组
     * @param isRoot          是否需要root权限执行
     */
    public static void execCmd(String[] commands, boolean isRoot) {
    //便于观看删除来不影响的部分代码，完整的可以在文中的github里找到。
            process = Runtime.getRuntime().exec(isRoot ? "su" : "sh");
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) continue;
                os.write(command.getBytes());
                os.writeBytes("\n");
                os.flush();
            }
            os.writeBytes("exit\n");
            os.flush();
            result = process.waitFor();
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
    }
```




没有root权限的情况下在屏幕上操作,实测可被执行的命令只有swipe和部分keyevent可以生效，其余的可以通过adb的方式调用成功。但是一但在应用内通过shell是不可以的。这确保了android手机的安全。

其中keyevent *返回键* *音量键*可以调用 而home按键这种则不可以。
如果你试图调用`dumpsys activity activities` 来查看。会抛出权限的异常如下。实测中我有申请权限，但一样无法在应用内部调起。
```
Permission Denial: can't dump ActivityManager from from pid=12414, uid=10369 without permission android.permission.DUMP0
```

![image.png](http://upload-images.jianshu.io/upload_images/1110736-26b5d470dd189f86.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

使用参考：

![](http://upload-images.jianshu.io/upload_images/1110736-0909d300b5420e0b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

Root情况下静默安装：
```
 String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install " +"apk路径";
  ShellUtils.execCmd(command, ture);
```

代码:https://github.com/BolexLiu/AndroidShell

---

## 双进程保活aidl版 （android5.0以下）
原理介绍：实现的机制并不复杂，通过AIDL的方式开启两个服务分别在不同进程中启动，然后互相守护监听对方是否被关闭，如果有一方被断开连接，另一方测重启服务。因为android在5.0之前销毁进程是一个一个销毁的，他并不能同时销毁两个。所以可以做这件事。（被修改的rom除外，比如华为4.4就不行，但三星可以。）

1.配置服务进程。注意process属性会独立在另一个进程中。
```
    <service android:name=".Service.LocalService" />
    <service android:name=".Service.RemoteService"  android:process=".Remote"/>
```

2.我们拥有两个服务LocalService  RemoteService。项目运行后第一件事，同时启动服务。
```
       startService(new Intent(this, LocalService.class));
        startService(new Intent(this, RemoteService.class));
```
3.在LocalService中绑定RemoteService并监听对方的创建和销毁，RemoteService中的实现也一样。
```
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.e(TAG, TAG + " onStartCommand");
        //  绑定远程服务
        bindService(new Intent(this, RemoteService.class), mLocalServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }
    //连接远程服务
    class localServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                // 与远程服务通信
                MyProcessAIDL process = MyProcessAIDL.Stub.asInterface(service);
                Log.e(TAG, "连接" + process.getServiceName() + "服务成功");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // RemoteException连接过程出现的异常，才会回调,unbind不会回调
            // 监测，远程服务已经死掉，则重启远程服务
            Log.e(TAG, "远程服务挂掉了,远程服务被杀死");
            // 启动远程服务
            startService(new Intent(LocalService.this, RemoteService.class));
            // 绑定远程服务
            bindService(new Intent(LocalService.this, RemoteService.class), mLocalServiceConnection, Context.BIND_IMPORTANT);
        }
    }
```

代码:https://github.com/BolexLiu/DoubleProcess

---

## 双进程保活jni版 （android5.0以下）
原理介绍：这种双进程守利用了Linux子进程在父进程被干掉后还能运行而实现。所以我们要做的是通过java去fork一段C的代码。通过动态链接库封装起来。然后在C代码里不断轮训父进程的ppid是否存活。如果挂掉了侧重新唤醒。

1.配置服务进程。注意process属性会独立在另一个进程中。

```
        <service
            android:name=".service.DaemonService"
            android:process=":daemon"></service>

```

2.在DaemonService里利用静态代码块调起so。

```
public class DaemonService extends Service{
  // 便于阅读省略无关代码，详情去移步至github···
       static {
        System.loadLibrary("daemon");
    }
}

```

3.so中的C代码轮训进程判断是否存活。

```
 //便于阅读省略无关代码，详情去移步至github···
//fork子进程，以执行轮询任务
    pid_t pid = fork();
    LOGI("fork=%d", pid);
    if (pid < 0) {
// fork失败了
    } else if (pid == 0) {
// 可以一直采用一直判断文件是否存在的方式去判断，但是这样效率稍低，下面使用监听的方式，死循环，每个一秒判断一次，这样太浪费资源了。
        int check = 1;
        while (check) {
            pid_t ppid = getppid();
            LOGI("pid=%d", getpid());
            LOGI("ppid=%d", ppid);
            if (ppid == 1) {
                LOGI("ppid == 1");
                if (sdkVersion >= 17) {
                    LOGI("> 17");
                    int ret = execlp("am", "am", "startservice", "--user", "0",
                                     "-n", name,
                                     (char *) NULL);
                } else {
                    execlp("am", "am", "startservice", "-n",
                           name,
                           (char *) NULL);
                    LOGI("else");
                }
                check = 0;
            } else {
            }
            sleep(1);
        }
    }
```

感谢CharonChui开源代码。处应该有掌声！

代码:https://github.com/CharonChui/DaemonService



## 保活 JobService版 （android5.0++）

原理: JobService是官方推荐的方式，即使app完成被杀死的状态下也能调用起来，本质是向系统注册一个任务。通过getSystemService拿到系统的JobScheduler。然后通过JobInfo.Buidler进行构造。需要注意的是一定要指定被触发的条件。比如:设备充电中、空闲状态、连接wifi... 非常类似以前的广播保护原理。但是实现不一样。这次是我们反向注册给系统，而不是接收系统的广播。

1.在AndroidManifest进行配置添加permission属性

```
 <service
            android:name=".MyJobService"
            android:permission="android.permission.BIND_JOB_SERVICE" />
```

2.MyJobServer继承JobService类：

```

    @Override
    public boolean onStartJob(JobParameters params) {
        //该方法被触发调用 可以做唤醒其他服务的操作
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return true;
    }
```

3.在合适的地方向系统注册

```
 JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(MainActivity.this, MyJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(++mJobId, componentName);
         String delay = mDelayEditText.getText().toString();
        if (delay != null && !TextUtils.isEmpty(delay)) {
            //设置JobService执行的最小延时时间
            builder.setMinimumLatency(Long.valueOf(delay) * 1000);
        }
        String deadline = mDeadlineEditText.getText().toString();
        if (deadline != null && !TextUtils.isEmpty(deadline)) {
            //设置JobService执行的最晚时间
            builder.setOverrideDeadline(Long.valueOf(deadline) * 1000);
        }
        boolean requiresUnmetered = mWiFiConnectivityRadioButton.isChecked();
        boolean requiresAnyConnectivity = mAnyConnectivityRadioButton.isChecked();
        //设置执行的网络条件
        if (requiresUnmetered) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        } else if (requiresAnyConnectivity) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        }
        builder.setRequiresDeviceIdle(mRequiresIdleCheckbox.isChecked());//是否要求设备为idle状态
        builder.setRequiresCharging(mRequiresChargingCheckBox.isChecked());//是否要设备为充电状态
        scheduler.schedule(builder.build());

```


注意jobScheduler无法兼容Android 5.0以下的设备，可以参考下面的项目，在低版本中也可以使用。

**实际测试 : **
研究了一段时间发现这个玩意，在国内的厂商定制过后的rom好多不起作用。 比如魅族 和小米上 如果把app杀死以后，这个服务也调用不起来了。但是在模拟器和aosp版本的Rom上是可行的。我测试时用的电池充电状态来调用job服务。



代码:https://github.com/evant/JobSchedulerCompat

---

 第一部分就先到这里。后续还有两篇续集会紧接着营养跟上，如果你觉得不错可以关注我一波点个喜欢神马的哈哈。


![](http://upload-images.jianshu.io/upload_images/1110736-c848af100a021f95.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)




[那些年Android黑科技①:只要活着，就有希望](http://www.jianshu.com/p/cb2deed0f2d8)
- android应用内执行shell
- 双进程保活aidl版
- 双进程保活jni版
- 保活JobService版

[那些年Android黑科技②:欺骗的艺术](http://www.jianshu.com/p/2ad105f54d07)
- hook技术
- 欺骗系统之偷梁换柱

[那些年Android黑科技③:干大事不择手段 ](http://www.jianshu.com/p/8f9b44302139)
- 应用卸载反馈
- Home键监听
- 桌面添加快捷方式
- 无法卸载app(DevicePolicManager)
- 无网络权限偷偷上传数据

---
# 如何下次找到我?
- 关注我的简书
- 本篇同步Github仓库:[https://github.com/BolexLiu/MyNote](https://github.com/BolexLiu/MyNote)  (可以关注)
![](http://upload-images.jianshu.io/upload_images/1110736-f0a700624e0723ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)