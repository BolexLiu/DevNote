上一篇[Android之重新推导设备尺寸](http://www.jianshu.com/p/3475c0006948)对屏幕尺寸的概念进行了推算。如果你对设备尺寸的掌握程度还比较模糊，不妨看一看，方便我们继续搞事拉。




在自定义View上做适配，多数情况下可以根据权重和获取整个控件的宽高然后根据百分比去设置某个特殊的属性需要用到的值。but，这些都太常见了。也不是一个我们今天要讲的重点。

不知道大家有没有纠结一个问题，在自定义View里，经常要定义Paint用来绘制。但是`setStrokeWidth(float width)`里的入参接受的到底是以`px`? `dp`? 哪个为单位。这里有本质上的区别。比如你写了一个控件，自己的开发机器没问题，结果换一个设备就尺寸就不准了。

我先卖个关子，不告诉你答案╭(╯^╰)╮。为此我们用官方`TextVIew`源码和`TypedArray`源码作为分析。

这是一条温柔的提示：*来么，宝贝儿！打开我们的`AndroidStudio`快速轻击`Shift`两下，然后输入`TextView`,我们一起看流星吧。（里面真的有很多小星星都是注释）*

![](http://upload-images.jianshu.io/upload_images/1110736-6ddc3d1c477d7fc7.gif?imageMogr2/auto-orient/strip)




# 目录
- TextView中的textSize源码分析
- TypedArray源码分析

---

## TextView中的textSize源码分析

### textSize默认大小
我们可以看到默认情况下，`TextView`的`textSize`是15。
![](http://upload-images.jianshu.io/upload_images/1110736-b1bac4a33f206a73.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 重绘方法
而后我发现了这样一个方法，用于重新设置`FontSize`，这里逻辑很清晰，判断Paint的Size和刚刚重设是否一致，不一致就重设`Paint`的参数，并重新绘制View。
![](http://upload-images.jianshu.io/upload_images/1110736-3270139c141b27f3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

最后调用的是Native方法，java层已经看不到了。
![](http://upload-images.jianshu.io/upload_images/1110736-ef7d2f3606e67090.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## setTextSize发现TypedValue

不过这些不是重点，我们还发现了另外一个方法。TextVIew设置字体有重载方法。我们重点去看两个参数的。

![](http://upload-images.jianshu.io/upload_images/1110736-79203c50846b4017.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这里说明了`Size`的单位大小是由`unit`来决定的，最后它调用了`SetRawTextSize`方法，而这个方法我们前面分析了是用来重绘的。我们来看Unit是个什么值。
![](http://upload-images.jianshu.io/upload_images/1110736-fef3d5f7c2d35b43.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

通过`TypedValue`类发现实际所有的单位都是定义在这里的。而我们的`setTextSize`需要的单位也在这里。

![](http://upload-images.jianshu.io/upload_images/1110736-aa2ed4d0790d8590.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### TypedValue.applyDimension
重头戏来了，如果我们用了两个参数构造方法进行设置字体大小，那么。
`TypedValue.applyDimension(  unit, size, r.getDisplayMetrics())`做了什
么，他是如何转换单位的。我跟进去看。
![](http://upload-images.jianshu.io/upload_images/1110736-f633ee221c52dadb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
我们来看最典型的一条case语句` case COMPLEX_UNIT_DIP: return value * metrics.density;`
这里的density就是我们上个章节里讲到的密度，也就是说如果你用dp作为单位，他就已当前的size乘以密度返回。
好家伙，恍然大悟把？实际上这里把你传入的任意单位最后都转换成了像素来处理的，最后设置给了`Paint`。

那么这里就说明了一个问题，`Paint`对象接受的参数最后都是px像素来使用的，不要漏掉了这一点喔。

这里我推荐直接使用`TypedValue.applyDimension()`来处理。这是android已经具备的Api，不需要自己再从零写了。可能有小伙伴用过类似dp2Px()这种类库。我个人不是很推荐往项目里堆叠繁杂的库进来，一来是会变得越来越臃肿，而是如果单纯的依赖不方便去修改源码，出了问题，你要么选择copy出代码出来进行修改，要么反射一下。但这都不是很优雅。尽可能使用系统已有的。




**参考代码：**
```
 TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, Resources.getSystem().getDisplayMetrics());

```

---

## TypedArray源码分析

### 什么是TypedArray
如果有常写自定义View属性的小伙伴一定对TypedArray不陌生，我们常用来在Java文件中取自定义View上的属性。这些属性可以是布尔、dp、整型、字符串甚至是一个布局文件的id等。

**现在我们思考一个问题，平时在一个控件里描述一个长度用了dp为单位，他是如何适配在不同设备上的？**

带着这个疑惑，我们来看源码如何解析的。首先我们不管是原生控件也好，还是自定义控件也好，我们都需要在构造方法中去取出XML布局文件中的属性。

### 获取TypedArray
这里获取TypedArray对象，其中`R.styleable.xxx`是该控件拥有的属性。
```
TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.xxx);
```
接着我们来看一眼TypedArray都有那些方法。

![](http://upload-images.jianshu.io/upload_images/1110736-db5d3b6f13d33fd1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### TypedArray的使用
实际上，我们的控件从XML中去解析就是通过该TypedArray的API来操作的。见TextView获取textSize源码如下。

![](http://upload-images.jianshu.io/upload_images/1110736-1b218f4288e9ea5f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
我们来看一眼，getDimensionPixelSize方法内部的逻辑。
![](http://upload-images.jianshu.io/upload_images/1110736-b0322e6919177c33.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

注意关键的一行`TypedValue.complexToDimensionPixelSize(
                data[index+AssetManager.STYLE_DATA], mMetrics);`， 我们跟进去看。

![](http://upload-images.jianshu.io/upload_images/1110736-93b5391ffda5dda6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
是不是似曾相识？熟悉吧，没错，该方法最终还是调用了`TypedValue.applyDimension()`进行了单位换算。我们上一章节讲过这个。我就不再展开里面的逻辑了。到这里真相大白。实际上在xml写的dp、pt、px等，最终都要来这里一趟，将其转换为px。再画出来。所以又一次证明了，我们的画图操作都是基于px为单位的。

### 流程总结

- 1.xml定义一个属性
- 2.在构造方法里用TypedArray来get这个属性。
- 3.调用getDimensionPixelSize()方法获取该属性上的具体值，并转换单位为像素。
- 4.绘制出来

我们发现它并不复杂，但很多时候往往是没有去阅读过源码，在工作中不能流畅的解决这些问题。
有小伙伴反应说他的自定义View通过java代码设置尺寸就是不能适配，但是通过属性就可以。这里其实就是忘记调用TypedValue.applyDimension(  unit, size, r.getDisplayMetrics())方法。
再比如面试过程中再有面试官问如何做适配的时候，你就不会再是背诵面试题上的答案了吧？



 题外话，我反对那些所谓的面试宝典类，不是说这些知识的对错性，而是很多新人在学习的过程中往往是囫囵吞枣，出发的目的如果是为了面试通过背诵这些东西，而不理解其中的概念，甚至想都没有想过为什么是这样。这样的结果绝对不是面试官想看到的。也不是我们应该做的。我们慢一点不要紧，多花点时间，不能途一时之快，那些偷过的懒，都是要还的。



---
# 如何下次找到我?
- 关注我的简书
- 本篇同步Github仓库:[https://github.com/BolexLiu/MyNote](https://github.com/BolexLiu/MyNote)  (可以关注)
![](http://upload-images.jianshu.io/upload_images/1110736-f0a700624e0723ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)