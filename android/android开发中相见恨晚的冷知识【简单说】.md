不知不觉我已有一个月没有更新了。

群里有朋友说:“鸡排，你变了。现在天天传播鸡汤。技术文章也不写了。”
![](http://upload-images.jianshu.io/upload_images/1110736-e6df3071bd916abc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/500)

抱歉抱歉，最近工作上确实太忙了。是时候上盘新菜了。请各位大佬们品尝品尝。


![](http://upload-images.jianshu.io/upload_images/1110736-f29ba19131e375fe.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


---

 ## 本篇都讲些什么?
- 如何排除第三方传递依赖导致的aar冲突
- 如何保持依赖最新的aar
- fragment里getActivity()空指针
- 代码new 出来的VIew没有ID
- 不传递的方式巧取context
- 不使用handle回到主线程（即UI线程）
- 防止VIew上信息被其他软件截屏或系统截图泄漏信息
- 专制接二手渣渣项目快速看方法调用栈顺序
---

### 如何排除第三方传递依赖导致的aar冲突

开发过程中经常出现你需要依赖第三方的某个库,比如下面的代码所示：
```
dependencies {
          compile 'com.github.BolexLiu:PressScanCode:v1.0.0'
}

```
PressScanCode是一个长按扫描屏幕上的二维码工具库，他底层的二维码识别使用了zxing库。我们假设作者开发时使用了老版本zxing 1.0.1的版本。而我们集成进来以后却发现本身项目里也依赖Zxing 但是我们的版本是3.3.0的。由于包管理具有传递性。这时就会起冲突。gradle无法自己处理你到底是该依赖哪个。下面是处理办法。

```
dependencies {
    compile('com.github.BolexLiu:PressScanCode:v1.0.0'', {
       exclude group: 'com.google.zxing' //排除依赖
    })
  compile 'com.google.zxing:core:3.3.0'
}

这样做的意思排除PressScanCode原有的依赖。而选择依赖我们自己设定的3.3.0的zxing库。

```

---

### 如何保持依赖最新的aar
这个相当容易，代码如下。你只需要将appcompat-v7:25.1.1这个版本好替换成“+”号即可每次都依赖最新的版本。
```
dependencies {
  // compile 'com.android.support:appcompat-v7:25.1.1'
    compile 'com.android.support:+'
｝

```
---
### fragment里getActivity()空指针

重写fragment然后在onAttach方法存下一个Activity引用。在其他地方需用用到context或者Activity的时候使用该引用。而不使用getActivity()。可以规避这个问题。但请留意强引用可能会导致内存泄漏的问题。
```
protected Activity mActivity;
@Override
public void onAttach(Activity activity) {
    super.onAttach(activity);
    this.mActivity = activity;
}

```
---
### 代码new 出来的VIew没有ID

通常我们的VIew是通过布局文件依照@+id的方式在R文件中生成对映的一个Int值。这是用于运行时保证资源唯一性。但有一种情况，我们需要动态的在代码中new出一个VIew来。如果一个VIew还好。多个view的时，没有id会导致你不方便持有一个引用。那么可以 View的generateViewId() 方法来生成 id，让系统来保证唯一。而不是用随机数产生或者手写一个具体的值。`注：API17++`

---
### 不传递的方式巧取context
context是我们经常用到的一个对象，这里我们不深入的讲解context，它的本质只是android组件的一个抽象接口，封装了一些统一的标准方法。有兴趣可以自己去查资料或翻阅源码。下面这张图已经说明了。

![](http://upload-images.jianshu.io/upload_images/1110736-877ed978cc9ab760.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

下面提供一种思路通过VIew直接获取context的api。特别是适配器中。别再传递这个对象了。`注:从View上拿到的一定是Activity对象，但是如果你通过Service中或者Application中获取的Context是不能用做操作View的。本质区别就是抽象方法和对象是无法保证你要操作的具体对象是你要的对象(这句话没读懂的多读几遍，慢点读。说到底它就是依赖倒置原则问题)`

```
View.getContext()  //任何被创建的VIEW都持有了context对象
```
---
### 不使用handle回到主线程（即UI线程）
通常我们使用Activity.runOnUiThread在子线程完成逻辑后更新UI。否则系统不会同意你在子线程中更新UI的。还有一种场景可以用下面的api
```
View.post(new Runnable() ) //同样可以切回UI线程执行。
```
当然现在Rxjava和EventBus可以完美的解决此类问题。我更推荐Rxjava。

---
### 防止VIew上信息被其他软件截屏或系统截图泄漏信息
在某些特殊的场景下，你的app可能和用户隐私有关系。如果需求需要禁止截图行为和覆盖你当前的Acitivity行为，可以使用 如下API。
```
getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE)
```
### 专制接二手渣渣项目快速看方法调用栈顺序
我们可能因为跳槽，或者其他原因接触到了一个陌生的项目。可能它就是那种神才能看懂的代码。有没有办法看某个方法到底是谁发起调用。都经过了什么过程呢？方法还是有的。如图： one>tow>printStack。我们需要找出printStack的调用顺序。

![](http://upload-images.jianshu.io/upload_images/1110736-cfcdcff005abb131.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/800)


```
//在最后你想看的方法中加入以下代码，就可以神奇的在日志中打印出来方法调用顺序
   RuntimeException here = new RuntimeException("bolex");
        here.fillInStackTrace();
        Log.w("myTag", "Called: " + this, here);

```
![](http://upload-images.jianshu.io/upload_images/1110736-6a817277641d4d91.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/800)

---
嗯。今天就到这里爸，不是不是，是吧(•‾̑⌣‾̑•)✧。

![](http://upload-images.jianshu.io/upload_images/1110736-b5ef60ad6068ba99.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/800)


---
# 如何下次找到我?
- 关注我的简书
- 本篇同步Github仓库:[https://github.com/BolexLiu/MyNote](https://github.com/BolexLiu/MyNote)  (可以关注)
![](http://upload-images.jianshu.io/upload_images/1110736-f0a700624e0723ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)