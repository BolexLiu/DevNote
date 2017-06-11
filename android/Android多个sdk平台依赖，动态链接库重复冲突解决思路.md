## 场景：
你正在开发一个刁飞的项目，该项目的功能需要用到多个不同sdk平台的依赖裤(aar)。这些sdk平台用到了动态链接库，也就是我们平时看到的.so文件。他们的.so文件有些并非自己开发的，是从开源项目中挖到的宝贝。作为A平台调用了名称为 adc.so。B平台也恰巧调用了名称为adc.so的动态链接库。作为你正在开发刁飞的项目的你同时接入了A平台的sdk和B平台的sdk。那么问题才刚刚开始。一图胜千言，请看下图描述信息。


![](http://upload-images.jianshu.io/upload_images/1110736-829f3bb45495cdcb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


首先一开始你要发现这个问题是不容易的。因为你可能先接入A平台的sdk，运行以后并没有发生任何问题。但当你接入B平台的时候就开始抛出so文件的奇怪错误了。而他并不会抛出是冲突问题，打包和编译都能顺利通过。在运行时抛出如下异常：
```
E/AndroidRuntime: FATAL EXCEPTION: main  java.lang.UnsatisfiedLinkError: dlopen failed: cannot locate symbol "_ZSt14__once_functor" referenced by "libfb.so"...
 ```
你可能会想，我tm明明在libs里加入libfb.so这个库啊，抛出_ZSt14__once_functor这个又是tm的什么意思？不行反编译看看apk到底是否存在这个so文件。解压后发现明明存在。为什么会抛这样的问题，没法看懂啊？

让你试图移除B平台的sdk后又正常了。恰巧的是B平台独立的demo又能运行。这时这样的问题并不好找。但也不是说没有办法。我们的解决方案不是重点，重点是找到解决方案的思路。我们往下看。

----

## 思路：
前面我们说过这个问题是由于两个平台都用了同一份so导致的。可我们刚遇见这个问题的时候并不清楚。我的思路是：

1 .首先对apk进行反编译查看。将正常与异常的两份apk都拆包查看。对比so是否有真实打包到apk内中。解释一下apk里分为不同的平台打包进去的动态链接库可能存在多个不同的目录。此时要看手机适合在哪个平台运行。安装后的apk并不会把所有的库文件都写入到手机，只会写入对应当前平台的库。推荐一款工具 native libs monitor 可以用来查看当前手机上安装的app的库文件。豌豆荚可以下载到。


```
//查看手机支持的cpu构架版本
adb shell cat /proc/cpuinfo
```
2.对未集成之前的sdk和打包的apk中的so文件进行文件对比。查看是否有公共的so。


## 若发现有公共的部分我们如下操作即可
- 1.单独建一个Module，并让主工程依赖当前Module。
- 2.在Module中集成sdk。
- 3.删除对比重复的so。只删除一份。保另一个sdk的一份。
- 4.注意：重复的so不能使用 packagingOptions exclude进行排除，应该直接在依赖中删除。因为一旦使用了排除会导致整个被排除的so都没法打包进去 。

---

## 其他关于动态链接库的参考：
https://segmentfault.com/a/1190000005646078
https://developer.android.com/ndk/index.html

---
# 如何下次找到我?
- 关注我的简书
- 本篇同步Github仓库:[https://github.com/BolexLiu/MyNote](https://github.com/BolexLiu/MyNote)  (可以关注)
![](http://upload-images.jianshu.io/upload_images/1110736-f0a700624e0723ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)