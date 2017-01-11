#Android 2017热门流行框架 【简单说】

2016年过去了,想想是不是又距离你的小目标更远了一些呢?

![](http://upload-images.jianshu.io/upload_images/1110736-a8cdf84f3101ef02.gif?imageMogr2/auto-orient/strip/h/200)


那么就让老司机带带你.赶快上车,没时间解释了.
![](http://upload-images.jianshu.io/upload_images/1110736-6ecf151b5354635d.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/100)

#你能在本篇文章中收获什么?
 - 认识目前流行的框架
 - rx系列全家桶
 - 高性能的图片压缩上传
 - 网络图片性能优化
 - AndroidM 动态权限管理
 - 内存泄漏优化
 - RecyclerView适配器优化
 - 注解释放双手
 - 超简洁的http请求
 - 一次布局,全尺寸适配


  本篇文章基于2016年已经很成熟&热门的第三方框架的一个提炼.所以库都附带GitHub地址.是新人进阶丶新项目启动丶重构框架等必备脚手架工具.避免重复造轮子才是我们的初衷.(我们从不写代码,我们只是Github的搬运工.)
 - 本篇同步Github仓库:https://github.com/BolexLiu/MyNote (欢迎star)
 - 注:以下排名不分先后次序

 >
    - [RxJava](https://github.com/ReactiveX/RxJava)
    - [RxAndroid](https://github.com/ReactiveX/RxAndroid)
    - [RxBus](https://github.com/AndroidKnife/RxBus)
    - [RxPermissions](https://github.com/tbruyelle/RxPermissions)
    - [RxLifecycle](https://github.com/trello/RxLifecycle)
    - [Eventbus](https://github.com/greenrobot/EventBus)
    - [Gson](https://github.com/google/gson)
    - [FastJson](https://github.com/alibaba/fastjson)
    - [retrofit](https://github.com/square/retrofit)
    - [butterknife](https://github.com/JakeWharton/butterknife)
    - [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
    - [glide](https://github.com/bumptech/glide)
    - [AndroidAutoLayout](https://github.com/hongyangAndroid/AndroidAutoLayout)
    - [zxing](https://github.com/zxing/zxing)
    - [compressor](https://github.com/zetbaitsu/Compressor)
    - [RxBinding](https://github.com/li-yu/FakeWeather/blob/master/github.com/JakeWharton/RxBinding)
    - [LitePal](https://github.com/LitePalFramework/LitePal)
    - [Jsoup](https://github.com/jhy/jsoup)
    - [ASimpleCache](https://github.com/yangfuhai/ASimpleCache)
    -  ````
 ---
# 他们是什么?一句话告诉你
- 注:一句话仅是他们的简介,若是你并不熟悉它们,还请参考GitHub给出的示例或其他文章.本篇不详细介绍,只做资源聚合与简介

##[RxJava](https://github.com/ReactiveX/RxJava)
 - 观察者模式的事件消息交互框架

##[RxAndroid](https://github.com/ReactiveX/RxAndroid)
 - 支持在Android 中通过Rx切换到主线程

## [RxBus](https://github.com/AndroidKnife/RxBus)
 - 提供如EventBus一般使用的Rx框架

##[RxPermissions](https://github.com/tbruyelle/RxPermissions)
 - 提供在Rx上来管理Android M (Android6.0)的动态权限框架

##[RxLifecycle](https://github.com/trello/RxLifecycle)
 - 解决Rx因为观察者在订阅后Fragment持有context导致内存泄漏的问题

## [Eventbus](https://github.com/greenrobot/EventBus)
- 翻译为事件总线,用于解决android中的事件交互和回调.同Rx一样也是观察者模式

## [retrofit](https://github.com/square/retrofit)
- android中的通讯注解框架,用于发送http请求.配合Rx能达到高效的开发速度

##[butterknife](https://github.com/JakeWharton/butterknife)
 - AS中通过自动导入生成注解,解放findViewById的痛苦.并且它是编译时注解,效率爆高

##[BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
 - 如名字一样他是RecyclerView的适配器超类库.支持各种姿势写adapter,轻松减少大量重复代码.

##[glide](https://github.com/bumptech/glide)
 - 目前最好的之一的图片加载框架.压缩质量高丶效率高丶性能好是他的特点

##[AndroidAutoLayout](https://github.com/hongyangAndroid/AndroidAutoLayout)
- 国内大神张鸿洋出的布局框架.很不错.减少大量布局调优工作

##[zxing](https://github.com/zxing/zxing)
- 老牌二维码扫描框架

##[compressor](https://github.com/zetbaitsu/Compressor)
- 图片压缩框架,压缩率很高.支持配置.Api友好

##[RxBinding](https://github.com/li-yu/FakeWeather/blob/master/github.com/JakeWharton/RxBinding)
- 可以实现数据层与View层的绑定，当数据发生变化，View会自动更新UI。还有其他功能非常强大(MVVM)

##[LitePal](https://github.com/LitePalFramework/LitePal)
- ORM数据框架比原生好用.

## [Jsoup](https://github.com/jhy/jsoup)
- 一个Html解析框架.用于爬网页后进行剔除数据

##[ASimpleCache](https://github.com/yangfuhai/ASimpleCache)
 - 一个为android制定的 轻量级的 开源缓存框架。轻量到只有一个java文件（由十几个类精简而来）。



---
#感谢&打赏
- 如果本文对你的工作或学习有帮助,您可以打赏支持我.这样我会更有动力.都说2017年是为内容付费的元年.O(∩_∩)O哈哈~
![](http://upload-images.jianshu.io/upload_images/1110736-c57defa4b04682f6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##支付宝
![](http://upload-images.jianshu.io/upload_images/1110736-b0ea390490081c5b.png?imageMogr2/auto-orient/strip%7CimageView2/2/h/300)

##微信
![](http://upload-images.jianshu.io/upload_images/1110736-a3a7f345e0cc17ad.png?imageMogr2/auto-orient/strip%7CimageView2/2/h/300)