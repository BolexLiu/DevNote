![](http://upload-images.jianshu.io/upload_images/1110736-f451008707e310bd.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


漂亮的皮囊千篇一律，
有趣的灵魂就是老夫啦。
这位看文章的小哥，还在无聊的套接口解析数据吗？

来来来，鸡排君带你玩一把将一个网站撸成App。

![](http://upload-images.jianshu.io/upload_images/1110736-b44c75dc47adc5b2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


本篇是一个实战文章讲解，
在无接口的情况下，将网站转换成App。
有料有干货，奇淫技巧我赌两包辣条你没见过。

啊，哈哈哈（你这个笑是认真的吗？）


别特么瞎BB，NotFoundBitmap你说个毛球啊。


![](http://upload-images.jianshu.io/upload_images/1110736-2fbdfda018f9890f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

哟，这位小爷看样子挺懂行啊，有滴有滴，看下面。
（警告前方高能）


![](http://upload-images.jianshu.io/upload_images/1110736-9161f8b9eda45b8d.png?imageMogr2/auto-orient/strip%7CimageView2/2/h/400)

不不不，不是你想的那样。
那个谁? 助理，对就是助理。他拿错了图
扣工资~~
你们假装没看见就好 ：）


![](http://upload-images.jianshu.io/upload_images/1110736-38facc6e28fcc719.gif?imageMogr2/auto-orient/strip)

(ˉ▽￣～) 切~~，不就是一个列表搭配了一个详情页嘛。
五毛特效。还装。

别走呀，老弟。这不是重点。
诺，先看看我们的网站。[www.apkbus.com](http://www.apkbus.com/plugin.php?id=cxy_common_blog)

没错，丫的，原生的网页并没有接口暴露出来给我们获取列表，
文章详情页排版也没有对移动端做区别展示。
不信你拿手机访问看看。

但是，但是我们要重新排版。

进入主题
---

通过本篇文章你能收获如下。

- 解析任意网页上的数据，转换成实体对象
- 奇淫技巧，动态注入js修改WebView内容排版

咱们这个项目是依照安卓巴士博客网站动态解析制作的APP。
使用了MVP+RxJava+Retrofit的主流开发套路。
篇幅原因我就展开讨论如何搭建框架搭建这种知识了，网上已有很多优秀的文章。
我们今天重点讲解这次实战中有用的干货知识啦。

快来吧，小宝贝儿，上车啦。


![](http://upload-images.jianshu.io/upload_images/1110736-7efe37a55be82057.gif?imageMogr2/auto-orient/strip)

------


## 目录
- 设计思路
- Chrome 页面分析与调试
- 解析页面数据
- WebView阅读模式
- 内容加载优化
- 其他
- 小结

-----

## 设计思路

### Banner :
![](http://upload-images.jianshu.io/upload_images/1110736-9e0254d3bc3cfb5f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这是巴士的首页，里面包含一个Banner。我们现在可以将这个Banner作为我的App的Banner轮播效果。

有三个信息我们需要获取。
- 背景图
- 标题
- Banner地址

怎么取数据？稍后我们会讲到。

### 博客列表

![](http://upload-images.jianshu.io/upload_images/1110736-5e0ee4d0edc9c206.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
博客列表页比较中规中矩，该有的都有。
- 用户头像
- 用户名称
- 文章标题
- 文章摘要
- 时间
- 阅读数
- 评论数
- 赞数
- 文章地址

### 详情页

![](http://upload-images.jianshu.io/upload_images/1110736-6fb9fda52aa0935d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
文章详情页面里内容看上去比较多。我们其实只需要关注三个东西。
- 用户信息
- 标题
- 内容

没错内容不再拆分了。

### 拆解数据思路
首先Banner和博客列表，我们知道他们都是网页。在前端中常见用`document`或者`JQuery`去获取页面节点上的属性或信息。那么其实`Java`也有轮子可以用来直接解析`Html`上的节点数据。大型的叫爬虫。当然我们这里用不到爬虫这么个东西。有更轻巧的[jsoup](https://jsoup.org/)框架。

而文章详情页面的思路不一样，这里我们不方便用`Jsoup`来解析，因为文章的排版是很复杂的，我们无法知道UGC 会把内容写成什么样子，自然也无法针对性的去解析。但是这不代表我们就不能重新排版了。不知道大家有没有用过`Iphone`自带的Safari浏览器，他支持将原本没有在移动端适配的页面重新布局。思索了许久这是如何做到的。最后采用了`WebView`里注入`js`，然后通过`js`去操作`dom`上的元素修改其样式。

----

## Chrome 页面分析与调试

因为我们并没有接口，所以我们先来分析页面结构，方便获取我们需要得到的数据。看Banner标签。
这里我们使用的是Chrome浏览器。打开开发者模式。


![](http://upload-images.jianshu.io/upload_images/1110736-e52009587968bbef.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/800)
可以看到最外层的`div`的`id`为`theTarget`里面包含了五个`div`就是我们需要取的数据。

我们展开其中第一个子`div`容器
```html
<div class="visible" style="position: absolute; left: 823.15px;">
  <a target="_blank" href="http://www.apkbus.com/thread-282214-1-1.html">
    <img src="data/attachment/forum/201707/31/161204zqgdz0cm22n2mmym.jpg">
    <div class="title">
        <span>不做将死之蛙 安卓巴士博文大赛第三期为你加温！</span>
    </div>
  </a>
</div>

```
可以很清楚的看到：

` <a>`里包含了文章详情的链接
`<img>`里包含了轮播图链接
`<span>`里包含了标题

同理在博客列表上也是一样的取法，可自行打开开发者模式查看就不展开浪费篇幅了。


![](http://upload-images.jianshu.io/upload_images/1110736-929200e683a9a12b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/800)

--------

## 解析页面数据

我们根据之前在Chrome上的分析得到了如下需要使用的对象。

**Banner模型**
![](http://upload-images.jianshu.io/upload_images/1110736-6e70c505cb733bdb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**文章列表模型**

![](http://upload-images.jianshu.io/upload_images/1110736-3fe2e5be968d58c2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

先在要做的是什么呢？

没错就是激动人心的从网页里取数据了。

这里我们使用`jsoup`来解析数据，我们在`Gradle`里依赖一下
```
    compile 'org.jsoup:jsoup:1.10.1'
```

这个框架的API已经很简单了噢。我简单说下如何使用。


1.使用` Document doc =  Jsoup.parse(htmlString)`方法加载我们需要解析Html。
2.假设我们需要获取`<h2 id="test">123<h2>`元素里的123。
3.调用` Element test=doc.getElementById("test");`就可以了。
4.` String value=test.text(); `就获取到了`value="123"`

怎么样，是不是超方便？这是第一步。其他的取法也大同小异。


下面我们依照**Banner**作为列子解析，看看其他的元素和属性如何解析。


这是Banner里的某一个Item。我们可以对照着解析。
```html
<div class="visible" style="position: absolute; left: 823.15px;">
  <a target="_blank" href="http://www.apkbus.com/thread-282214-1-1.html">
    <img src="data/attachment/forum/201707/31/161204zqgdz0cm22n2mmym.jpg">
    <div class="title">
        <span>不做将死之蛙 安卓巴士博文大赛第三期为你加温！</span>
    </div>
  </a>
</div>

```

为了方便大家阅读理解，我就把解析过程写在下面图中的代码上了。

![](http://upload-images.jianshu.io/upload_images/1110736-9ea7e594fc9e0c13.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

然后我们在UI层对数据进行渲染加载。

![](http://upload-images.jianshu.io/upload_images/1110736-2f1e75dee2d00323.gif?imageMogr2/auto-orient/strip)



这样就完成了从`Html`到APP上的解析过程。

![](http://upload-images.jianshu.io/upload_images/1110736-06938b044c06fc4d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)





**博客列表**的信息稍微多一点，不过一样如法制炮。
```
<div class="row">
	<a href="blog-889706-68413.html" target="_blank"><h2>Handler消息源码流程分析（含手写笔记）</h2></a>
	<div class="preview">相关文章链接：Handler消息源码流程分析（含手写笔记）HandlerThread线程间通信源码解析IntentService源码解析Handler在android开发中可谓随处可见，不论你是一个刚开始学习android的新人，还是昔日的王者，都离不开它。关于 handler的源码已经很前人分享过了。如果我没能给大家讲明白可以参考网上其他人写的。注：文 ...</div>
	<div class="info">
	<div class="uinfo">
		<img onclick="window.open('home.php?mod=space&uid=889706&do=index');" src="http://www.apkbus.com/uc_server/avatar.php?uid=889706&size=small">
		<span>香脆的大鸡排</span>
	</div>
	<div class="cinfo">
		<span>阅读：197</span>
		<span>评论：5</span>
		<span>赞：1</span>
	</div>
</div>
</div>
```
有兴趣深入看`ApkBusBolgMode.java`解析的代码[ApkBusBolgMode](https://github.com/BolexLiu/ApkBusBlog/blob/4881ce9633dded832d83ef29cb3d0f7812ce571f/app/src/main/java/bolex/com/apkbus/Blog/model/ApkBusBolgMode.java)，就不细说了。


![](http://upload-images.jianshu.io/upload_images/1110736-50b4609b3726e2e3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/200)

------

## WebView阅读模式

前面说过Iphone自带的Safari浏览器支持将本来PC的网页，转换成阅读模式，让移动端方便阅读。这里一样，我们的详情页也是一个PC上展示的，如果在移动端上直接用`WebView`进行加载，那将很不适合阅读。

在做之前我左寻思，右冥想。晚上都睡不好觉了，在`android`上怎么做，又没有轮子。

半梦半醒之间，大概是4点多的样子。

梦里我女朋友说：
“老公，你们可曾记得有一招从天而降的掌法？！？”

![](http://upload-images.jianshu.io/upload_images/1110736-432004731059b4ab.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

卧槽，莫非是失传已久的如来神掌？

顿时BMG响起，唢呐、古筝。（喂，你们配合一下脑补音乐下好嘛）

甚（肾）好 ，甚（肾）好 ！



灵光闪过，老夫一弹坐了起来，摸起我那20年的梭(键盘)。

![](http://upload-images.jianshu.io/upload_images/1110736-496e275f0f46ce32.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/300)


开机 撸代码！！ 哦不，那是灵魂的敲击声。



**js注入**
WebView加载网页时，可以通过如下方法从java层将js注入进去。
```
mWebView.loadUrl("javascript:function myFunction(){
}") //声明方法

mWebView.loadUrl("javascript:myFunction()") //调用方法。
```
那么我们知道`javascript`是可以动态的操作`html`和`css`样式的，虽然我们不能从服务器上修改巴士网站的代码进行适配，但是可以选择在客户端上操作响应后给`WebView`的结果呀。

如果用原生的`API`可能不是那么简洁，在分析网页的过程中，我们发现了网站中有引入`Jquery`框架。

![](http://upload-images.jianshu.io/upload_images/1110736-d14d9aae3905c6d0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

那么接下来的事情就简单了，我们直接调用`Jquery`来动态修改网页。

**css样式调试**

这里我们还是先用Chrome在PC端进行调试，因为如果每次都在`android`中修改`js`再编译，太慢了，不方便。我们一次性调试完成后。写入到项目中去。且看我们下面的操作。

1.打开详情页，开启控制台。看看文章主体内容的`div`是赋予的什么`id`或者`class`。

![](http://upload-images.jianshu.io/upload_images/1110736-d0451e3a873372ec.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可以发现，这里没有`id`，只有`class="article"`,不过已经够用了。
在控制台上调用`JQuery`获取内容。

![](http://upload-images.jianshu.io/upload_images/1110736-4ac8afd54888e249.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
此时我们已经取到了文章主体内容。

接下来将`Body`内容先清空。再给我们的`content`内容加上样式使其铺满全屏。最后设置到`Body`里。这时网站里其他的内容已经被全部清空，只剩下网站的文章内容了。如下图。
![](http://upload-images.jianshu.io/upload_images/1110736-d28e243e657dce7d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

但这还不够噢，因为文章主体里用户如果用的是`markdown`编写的还好，倘若使用了富文本编辑所生成的`html`，将导致内联一些样式在里面。当我们在`android`上渲染的时候，文字不会自动换行，还有图片可能巨大无比，不方便阅读。

 接下来我们来处理这个问题。

![](http://upload-images.jianshu.io/upload_images/1110736-aadd398ff4e2e394.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们在控制台中将所有的`div、a、h1、h2、h3、h4、h5、h6、img`，这种常用元素的`class、id`和内联`css`样式一并删除。为什么要删掉呢？因为网站里使用了各种样式来修饰。倘若我们针对某个`id`来修改样式，不能兼顾内联样式。势必导致我们的网页长相奇怪。索性一并删除。

而后添加样式，使图片充满，`h`元素用标准的`markdown`字体大小。这里我参考了`github`的原生样式，使用的是`em`属性来动态设置`fontSize`。

下面是完整的js注入代码。
``` javascript
var $jquery = jQuery.noConflict();
var content=$jquery('.article');
$jquery('body').empty();
content.css({
background:"#fff",
position:"absolute",
top:"0",left:"0",
});
$jquery('body').append(content);
$jquery("div").removeAttr("class").removeAttr("style").removeAttr("id");
$jquery("a").removeAttr("class").removeAttr("style").removeAttr("id");
$jquery("h1").removeAttr("class").removeAttr("style").removeAttr("id");
$jquery("h2").removeAttr("class").removeAttr("style").removeAttr("id");
$jquery("h3").removeAttr("class").removeAttr("style").removeAttr("id");
$jquery("h4").removeAttr("class").removeAttr("style").removeAttr("id");
$jquery("h5").removeAttr("class").removeAttr("style").removeAttr("id");
$jquery("h6").removeAttr("class").removeAttr("style").removeAttr("id");
$jquery("img").removeAttr("class").removeAttr("style").removeAttr("id");
$jquery("img").css({width: "100%",height:"100%",objecFit:"cover"});
$jquery("h1").css({paddingBottom: "0.3em",fontSize:"2em",borderBottom:"1px solid #eaecef"});
$jquery("h2").css({paddingBottom: "0.3em",fontSize:"1.5em",borderBottom:"1px solid #eaecef"});
$jquery("h3").css({fontSize:"1.25em"});
$jquery("h4").css({fontSize:"1em"});
$jquery("h5").css({fontSize:"0.875em"});
$jquery("h6").css({fontSize:"0.85em"});
```

我们在`android`中`WebVIew`渲染结束后调用即可。
我们使用了`WebViewClient`来处理，在`onPageFinished`回调后说明网页已被加载成功。在此处注入`js`即可。
```
 llWeb.setWebViewClient(mWebViewClient);
 private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void o

nPageFinished(WebView view, String url) {
            mWebView.loadUrl("javascript:XXXXXXX") //注入我们前面调试用的Jquery代码 XXXXXXX为省略
        }
    };

```

效果如下：
![](http://upload-images.jianshu.io/upload_images/1110736-2479eb623cd5ab03.gif?imageMogr2/auto-orient/strip)

注入完整代码见：[BlogDetailAct.java](https://github.com/BolexLiu/ApkBusBlog/blob/4881ce9633dded832d83ef29cb3d0f7812ce571f/app/src/main/java/bolex/com/apkbus/Blog/act/BlogDetailAct.java)

-----

## 内容加载优化

经过测试一段时间后，我们发现`WebViewClient`里使用`nPageFinished`方法加载太慢了。因为它必须要等待页面完整的渲染完成后才会回调。一般来说文中都会有图片，当图片资源过大的时候。就会导致页面阻塞。时间长的可达到数分钟之久。（也许是我的网络太慢吧。）

但这终究不是一个很好的体验。怎么办？

![](http://upload-images.jianshu.io/upload_images/1110736-3fd2584ea568d7ba.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/300)


看来老夫又要去睡一觉，让女神姐姐拖个梦给我了。

····
天黑了，请闭眼。

请狼人出来活动。

啊呜~~

天亮了

这不睡一觉马上来神了。

我去参考了其他项目发现，一般`WebView`里都会带有一个进度条。那就说明网页在`WebVIew`中解析的时候是可以知道他的进度的。最终找到了。
`WebChromeClient`这个牛逼的家伙。

`onProgressChanged`方法会回调网页的加载进度。如果我们不想让图片加载来阻塞整个活动。那么可以在进度达到95的这个阀值的时候，注入`js`。这样就会比在整个网页渲染完成后注入，时间短很多。代码如下。
```
  private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            if (newProgress > 95 && isNeedExe) {
                isNeedExe = !isNeedExe;
                view.loadUrl("javascript:function myFunction(){\n" +
                        "var $jquery = jQuery.noConflict();\n" +
                        "var content=$jquery('.article');\n" +
                        "$jquery('body').empty();\n" +
                        "content.css({\n" +
                        "background:\"#fff\",\n" +
                        "position:\"absolute\",\n" +
                        "top:\"0\",left:\"0\",\n" +
                        "});\n" +
                        "$jquery('body').append(content);\n" +
                        "$jquery(\"div\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
                        "$jquery(\"a\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
                        "$jquery(\"h1\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
                        "$jquery(\"h2\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
                        "$jquery(\"h3\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
                        "$jquery(\"h4\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
                        "$jquery(\"h5\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
                        "$jquery(\"h6\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
                        "$jquery(\"img\").removeAttr(\"class\").removeAttr(\"style\").removeAttr(\"id\");\n" +
                        "$jquery(\"img\").css({width: \"100%\",height:\"100%\",objecFit:\"cover\"});\n" +
                        "$jquery(\"h1\").css({paddingBottom: \"0.3em\",fontSize:\"2em\",borderBottom:\"1px solid #eaecef\"});\n" +
                        "$jquery(\"h2\").css({paddingBottom: \"0.3em\",fontSize:\"1.5em\",borderBottom:\"1px solid #eaecef\"});\n" +
                        "$jquery(\"h3\").css({fontSize:\"1.25em\"});\n" +
                        "$jquery(\"h4\").css({fontSize:\"1em\"});\n" +
                        "$jquery(\"h5\").css({fontSize:\"0.875em\"});\n" +
                        "$jquery(\"h6\").css({fontSize:\"0.85em\"});\n}");
                view.loadUrl("javascript:myFunction()");
                pDialog.cancel();
            }

            super.onProgressChanged(view, newProgress);
        }
    };

```

先生乃神人也。

呵，兄弟！此言差矣，汝可知道鸡排与汝最大的区别是什么吗？

鸡排，只是在晚上会有女神托梦于老夫，教导老夫写代码。岂是尔等能比的？


![](http://upload-images.jianshu.io/upload_images/1110736-e00c3bd42c9ccf92.gif?imageMogr2/auto-orient/strip)

-----

## 其他

**配色**
在配色上选用了巴士网站本身长久使用的三个主色调，搭配另外颜色鲜艳的七种颜色作为文章列表的Item背景色，同时使用共享元素动画，将`Item`的背景色和详情页利用`Intent`传递，做成了沉浸式。

![](http://upload-images.jianshu.io/upload_images/1110736-e203654f2166fb90.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**项目结构**

非常典型的`MVP`结构，这次本文的重点解析层都在`model`内，详情页的`WebView`优化在`Act`内。

![](http://upload-images.jianshu.io/upload_images/1110736-4e4d713a8c0a6dda.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


如果你对这个架构感兴趣可以去参考这篇文章：[少年，老夫带你撸一把Android项目框架，你可想学](http://www.jianshu.com/p/06d417b554ef)

本次的项目地址：[ApkBusBlog](https://github.com/BolexLiu/ApkBusBlog)

-----

## 小结

我没有把项目中每一个细节都贴代码来讲，如果感兴趣可自行翻阅源码看。因为本文的重点不是教大家如何去写代码，而是结合其它技术。将一些本该难以实现，或者说不是那么容易做到的东西，融会贯通。我也更倾向于去传递思想，而不喜欢死扣细节代码上。

这里只简单的拿了两份数据和一个详情页。其他的页面也是一样的做法，所以如果要把客户端做完整，后面还有很多事情可以做，可以优化。后续我打算将WebView阅读模式像苹果的Safari一样再封装得完善一些然后开源出来，我相信也许有很多人需要用。

本篇的内容可能需要读者有一些综合能力，熟悉前端知识和移动端知识。毕竟来说移动端同样也属于大前端的一个分支，尚且js现在也比较火。对比来说如今kotlin在android正营里处于火热的状态下。其实如果你有学习过js的ES6，你会觉得当你同时掌握了动态语言和静态语言后，学习另一门语言是如此之容易，并不需要系统的从零学习。大概一周内，就可以很快速的转过去。上手即可用。思想都是想通的嘛。


---
# 如何下次找到我?
- 关注我的简书
- 本篇同步Github仓库:[https://github.com/BolexLiu/MyNote](https://github.com/BolexLiu/MyNote)  (可以关注)
![](http://upload-images.jianshu.io/upload_images/1110736-f0a700624e0723ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)