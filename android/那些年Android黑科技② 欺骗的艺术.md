![](http://upload-images.jianshu.io/upload_images/1110736-92c2d9c66f2b3316.png?imageMogr2/auto-orient/strip%7CimageView2/2/h/400)


“我的能量无穷无尽，只有强大暗能量才能统治Android界。
受屎吧！！！ =≡Σ((( つ•̀ω•́)つ    ”
                                 -- 来自暗世界android工程师


![](http://upload-images.jianshu.io/upload_images/1110736-2047b7ffcd3c9c2e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


前言：

这是黑科技系列的第二篇，是Android知识正营中较有深度难理解的知识。如果你是一个初学者，牵扯的知识太深，文中没有从零讲起。皆是拔云见雾的带大家看。可以先收藏起来，日后慢慢系统性的对着文中的Github源码写一遍。

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

那些年Android黑科技③:干大事不择手段  待续····
- 应用卸载反馈
- Home键监听
- 桌面添加快捷方式
- 无法卸载app(DevicePoliceManager)
- 无网络权限偷偷上传数据

---


## hook技术
hook一词最早我是在用windows的时候学习到的。当时用来做键盘监听。懵逼的我无法理解这个翻译叫“钩子”东西到底是什么鬼。

那么先说下hook到底是在干嘛。我们可以把hook当作代理模式或劫持来理解。在一个方法的前或后动态插入一段我们的逻辑事情，甚至改变原本方法在执行前的参数，返回后的参数。总之可以hook任意java写的代码，修改替换apk内部的资源文件。一摸索到现在到android中hook已知有两种。这两种分别是:

- 1.root设备后通过xposed 、Magisk等框架 hook应用做一些串改参数逻辑等事情
- 2.开发中通过反射实现Hook第三方库或系统内置Api（欺骗系统之偷梁换柱章节单独讲这块）

### xposed原理和插件开发

xposed是一个hook的框架。一般是用来做手机插件的。比如修改系统电池图标、信号、按键交换位置等等，但是也有人用来做一些应用的破解等。我们先说下xposed原理。只有明白原理了才知道这个东西是怎么玩的。

apk在android运行的时候是通过ActivityManagerService（以下简称AMS）发送Socket给Zygote进程进行通信。由Zygote进程fork一个子进程来启动我们的apk程序。

AMS -> Socket-> Zygote->apk启动

重点来了。下面将是android Xposed Hook的核心原理。仔细看。

**在android运行环境中，Zygote进程是所有虚拟机进程的父亲,Zygote进程在开机初始化的时候会创建一个虚拟机，AMS发消息给Zygote创建的时候实际上是copy一份虚拟机的实例在子进程中。同时在初始化的时候还会注册一些android核心Jni的库放在虚拟机实例内提供上层api调用。fork出的虚拟机会共享这些jni类库。 那么实际上XPosed就是在root后对替换/system/bin/app_process并将注入XposedBridge.jar。app_process是用来控制Zygote的，通过替换成修改后的 app_process可以使Zygote进程加载到我们的XposedBridege.jar。而这个库就是用来做动态Hook java代码造成劫持的。**


![](http://upload-images.jianshu.io/upload_images/1110736-73e6e0117e29adcc.png)

插件也是一样用android Studio写就可以了，只是我们不需要任何活动容器。
1 .新建一个工程在gradle里添加xposedApi依赖
```
dependencies {
provided ‘de.robv.android.xposed:api:++’
}
```
2.在AndroidManifest中配置声明标签让xposed壳认识你的插件
```
 <application
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name" >
        <meta-data
            android:name="xposedmodule"
            android:value="true" />
        <meta-data
            android:name="xposeddescription"
            android:value=" example " />
        <meta-data
            android:name="xposedminversion"
            android:value="22" />
    </application>
```
3.使用xposed提供的接口做具体的hook逻辑
- XposedHookZygoteInit  （Zygote进程启动前做的事情）
- XposedHookLoadPackage（ app的代码运行时）
- XposedHookInitPackageResources（加载app资源时）

4.串改变量示例
```
public class Demo implements IXposedHookInitPackageResources{

 @Override
 public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {
   //判断当前需要hook的包名
   if (resparam.packageName.equals("com.android.xxxx")) {
    //调用setReplacement方法替换名为aaa的变量值为false
    resparam.res.setReplacement(resparam.packageName, "String", "aaa", “123”);

   }
 }
```
5.在工程里的assets目录下创建名称为xposed_init的配置文件（注意是无格式的文本），在该文件里写入你插件的包名。xposed框架会在设备开机的时候读取的插件。
```
com.bolex.xxx

```
6.打包出来 安装到手机上 然后在Xposed里面勾上你的插件重启即可

Xposed框架在玩机的发烧友手机上号称是必装的神器。作为android的我们其实是有能力自己写插件的。同时如果我们是做付费软件和金融软件的对Xposed还是要做一些防范。比如加固、混淆你的应用让坏人没法很轻松的知道你的代码逻辑。

---


## 欺骗系统之偷梁换柱
相信做过android的同学就算没有用过反射也听过。我们知道反射可以在不修改源代码的情况下对私有方法和成员变量调用或修改。同上一个章节讲到的hook技术一样。除了Xposed的方式以外，反射本身也能做hook。
通常有两种方式动态代理、静态代理，本文使用动态代理来讲解，所以如果你不明白动态代理的原理可以去补补这块的知识。简单来说就是动态代理会利用被被代理对象的接口,通过反射模拟一份一样的接口来实现代理。

可以做的事情：
- 启动未注册的Activity（apk插件化）
- 全局无埋点插代码
- 不重启动态加载不存在的代码
- ···等等

**原理剖析:**
android系统层会提供接口的形式来实现一些回调操作。我们通过反射去获取持有接口引用的对象。然后偷梁换柱的将自己的接口塞进去替换原有的对象。导致系统调用接口的时候是调用我们换过的代理对象。我们在代理对象内部再去调用原本的对象的接口方法，这样就可以做到hook的目的。

比如我们常用的 OnClickListener()接口，用于点击事件的回调。如果hook View的ListenerInfo对象名称为mListenerInfo。就可以拦截点击或者在点击之前和之后做一些我们想做的事情。下面我们讲一个高深且好玩一点的案例。

 ### 免注册的Activity
先思考一个问题，为什么我们的Activity需要在AndroidManifest.xml文件中注册？如果不注册能不能启动呢？

我们知道是AMS负责调起Activity。在启动之前他做了些什么事情呢？前面我们说过实际上AMS是给Zygote发送了消息，由Zygote进程fork一个虚拟机进程来。

那么其实在开机时Zygote进程在运行时第一个fork的进程是system_server的进程，这个服务用于管理系统级别的服务启动。

**System_server进程包含了以下顶级的系统服务**
- ActivityManagerService
- PowerManagerService
- MountService
- NetworkManagementService
- InputManagerService
- WindowManagerService

既然AMS承载了调用逻辑。我们是否可以对AMS动刀子来做到不注册Activity也能直接启动呢？

这里为什么我们前面要讲system_server服务，原因是我们要知道AMS进程不在我们自己的应用内，而是独立的远程服务进程。java层的上的反射是无法做到跨进程的。所以我们可以在自己的应用内利用AIDL的特性拿代理对象，去欺骗AMS服务。
**流程如下：**
- 1.我们使用 startActivity(intent);来启动一个Activity。
- 2.hook住ActivityManager内的本地binder对象，拦截startActivity方法。
- 3.用动态代理方式将未被注册Activity的Intent替换成已注册的Activity的Intent，并将未被注册的Intent带过去。（替换壳Activity，躲避AMS服务的检查，也就是欺骗AMS）
- 4.对ActivityThread中的handle进行hook。因为前面替换的是壳子，替换真正要启动的Intent。
- 5.发送handleMessage消息，启动未被注册的Activity。

### 实现欺骗：

这里我们通过反射获取到AMS的代理本地代理对象Hook以后动态串改Intent为已注册的来躲避检测
![](http://upload-images.jianshu.io/upload_images/1110736-95ac73107385a7a3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

通过动态代理实现对startActivity中的Intent串改，具体逻辑见代码和注释。
![](http://upload-images.jianshu.io/upload_images/1110736-b8de6d8b297336d4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

 hook ActivityThread 中的 handle 在这里我们需要替换我们未被注册的Activity Intent

![](http://upload-images.jianshu.io/upload_images/1110736-7d735a5e2e2e635f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

拦截启动消息
![](http://upload-images.jianshu.io/upload_images/1110736-33567abaeae04a21.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

替换我们真实要被启动的Intent
![](http://upload-images.jianshu.io/upload_images/1110736-6ed5f9ddf1ef75ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

封装以后我们ASMHook将变得非常简单，简单到一行代码就可以实现不注册的情况下启动Activity。
其中HostActivity是我们的壳，OtherActivity是未被注册的。我们可以像平时正常调用API的条件下直接使用startActivity了。
![](http://upload-images.jianshu.io/upload_images/1110736-91aa37ee4eec923f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

结果 启动了。

![](http://upload-images.jianshu.io/upload_images/1110736-eb5bda98ff237fc9.gif?imageMogr2/auto-orient/strip)

![](http://upload-images.jianshu.io/upload_images/1110736-87737eb88e37c772.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/500)

哎妈呀，好腻害的样子，喘口气看看刚刚发生了什么事情。


![](http://upload-images.jianshu.io/upload_images/1110736-8cbcd0b9251a3d1b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

献上封装好的HookAMS菊花(GitHubDemo)：https://github.com/BolexLiu/AndroidHookStartActivity



---


![](http://upload-images.jianshu.io/upload_images/1110736-c848af100a021f95.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



[那些年Android黑科技①:只要活着，就有希望](http://www.jianshu.com/p/cb2deed0f2d8)
- android应用内执行shell
- 双进程保活aidl版
- 双进程保活jni版
- 保活JobService版

[那些年Android黑科技②:欺骗的艺术](http://www.jianshu.com/p/2ad105f54d07)
- hook技术
- 欺骗系统之偷梁换柱

那些年Android黑科技③:干大事不择手段  待续····
- 应用卸载反馈
- Home键监听
- 桌面添加快捷方式
- 无法卸载app(DevicePoliceManager)
- 无网络权限偷偷上传数据
---
# 如何下次找到我?
- 关注我的简书
- 本篇同步Github仓库:[https://github.com/BolexLiu/MyNote](https://github.com/BolexLiu/MyNote)  (可以关注)
![](http://upload-images.jianshu.io/upload_images/1110736-f0a700624e0723ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)