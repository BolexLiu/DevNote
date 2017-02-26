时间从来没有等过我们,岁月这把捅猪刀.捅得你满脸都是沧桑.你一定是为工作操碎了心.不知道现在的身处何处,是否有挚爱的人照顾你.过得快乐或委屈?
哦忘了.你是个有故事的人,"你想ta过得比你要好,希望你永远不都会知道".
![](http://upload-images.jianshu.io/upload_images/1110736-c0fe2d7bfec53d99.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/h/400)

有时在过度劳累之后，腰腿酸痛精神不振，好像身体被掏空，是不是*透支了？


![](http://upload-images.jianshu.io/upload_images/1110736-79fa48abc8221caa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


骚年莫慌,老夫带你撸个框架,进可重振雄风,退可养精蓄锐!

![](http://upload-images.jianshu.io/upload_images/1110736-419cb34823983cd4.gif?imageMogr2/auto-orient/strip)

 # 本篇适合什么样的人群看？
 - 做android有一段时间了，撸码就是一把梭。
 - 项目开工，但是目前不知道怎么去拆分业务和技术依赖分层。
 - 刚刚毕业出来，进了一家不大不小的公司一人我因酒醉，两眼是...啊，呸呸呸！一人单挑项目。


 # 你能在本篇文章中收获什么?

- 自己动手搭一个通用型依赖框架
- 学习拆分业务层和技术层
- and 吹吹水 装装哔

---

# 目录
- 前言
- 踩坑
- 分析
- 实现
- 总结

---
# 前言

本篇是从线上的APP中基于一次次的采坑、总结、分享抽离出来的。但由于商业原因具有不可公开性。所以不会提及任何与业务有关联的场景用例。以及不会包含与任何商用项目相干的提及。所有技术仅仅用于技术学习交流。本篇不会是一个纯代码讲解的文章。文本中的故事也是一个虚构的。具体技术实现请您移步致GIthub，地址在篇尾。特此声明。

## 致谢
 感谢 jeasinlee、LeoFangQ、Michael、zaneCC 几位前辈无私的分享才得已产出。

# 踩坑
  什么？要求一个月后上线？老夫就是一把梭。复制粘贴东拼西凑就能出来。
 上线一周后。小王啊。经过产品和老板讨论过后，我们下一步要把首页上的XX改一下。后面主流程不这样走了。你接下来...？
 fuck！为毛你们之前不想好。这怎么改。代码是一大坨的。动一下可能就会导致到处都会崩溃。

项目中的代码是下面这样的：


![](http://upload-images.jianshu.io/upload_images/1110736-6596a361bf53af17.png?imageMogr2/auto-orient/strip%7CimageView2/2/h/200)


![](http://upload-images.jianshu.io/upload_images/1110736-71c01ec3186a1538.png?imageMogr2/auto-orient/strip%7CimageView2/2/h/200)

你叫劳资怎么改？当初是你说要一个月上线。这个项目的代码以前只有我和神能看懂。现在只有神了。

# 分析

想想就气人，都是你们这帮孙子逼逼逼，连夜加班一个月赶出来的东西。上线不到一周就要改。这下我可头大了。之前没有考虑到后期可能会出现维护或者扩展。改?还不要了我命。但也没有更好的办法了。只好默默的理代码了。


![](http://upload-images.jianshu.io/upload_images/1110736-2cb267ae59a9f377.png?imageMogr2/auto-orient/strip%7CimageView2/2/h/150)


第二天项目里进来了一个看起来像dalao的气息的人，dalao看完会心一笑，小王啊。这个时候我们宁可重构一把也不可在原有的基础上改，救得了今天救不过明天。这样，我向上级申请延长一些时间，我们一起来重新设计一把架构把。



之前的项目结构：

![一把梭项目架构](http://upload-images.jianshu.io/upload_images/1110736-f42ca4724fa798ff.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

 dalao的项目结构：
![dalao的架构](http://upload-images.jianshu.io/upload_images/1110736-01822d6892367619.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#  实现
等一下，dalao。你直接给我上个图，我看不懂呀。并且也没有解释清楚怎么实现分离的呢。

dalao咳咳两声喃喃道：首先，我们整体的思路是让现在开发的APP去依赖AndroidBasicLibs，我们尽量做到精简只需要在Gradle依赖它就行了。
```
 dependencies { compile 'xxx.xxx.xxx:AndroidBasicLibs' }
```
AndroidBasicLibs作为一个通用型的依赖库，它拥有三只麒麟臂，分别是basekit、common、uihelper。

###AndroidBasicLibs

#### basekit
basekit是一个基于MVP+RXjava的的基础框架，它承载了MVP的设计风格，我们的上层APP只需要继承它的BaseActivity、BaseModel 、IBaseView 、BasePresenter 就可以了。上层要做的仅仅只是往对应的层填充独有的业务。如下图5是Base。 图6是APP中的业务单元。具体用法请参考GIthub，地址在本文末尾。

![图5](http://upload-images.jianshu.io/upload_images/1110736-1abd0b7a69c8378b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![图6](http://upload-images.jianshu.io/upload_images/1110736-37457a1792d2b389.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



#### common
common是一个通用型的工具箱集，它可以对上层所依赖的第三方框架做解耦操作。如何理解这句话呢。假如我们要做图片加载，图片加载框架你肯定不会自己重复造轮子。在没有common层时。项目1.0的依赖的是ImageLoader，2.0的时候发现这个库有BUG需要将ImageLoader替换成主流的glide框架。但因为项目中多处都有使用到ImageLoader的API。无法做到加载图片处只改一行代码，就能让所有的业务都换了新的加载工具。这时common的威力就显而易见。，中间做解耦，上层调用common。common调用第三方依赖。当然这只是一个列子。还有很多地方都可以进行二次封装。比如网络请求、视图动画等我们把这些封装都放在common。下面是图片加载的二次封装实现代码。


```
//用接口统一约束Api
public interface ILoader {
    void init(Context context);

    void loadNet(ImageView target, String url, Options options);

    void loadResource(ImageView target, int resId, Options options);

    void loadAssets(ImageView target, String assetName, Options options);

    void loadFile(ImageView target, File file, Options options);

    void clearMemoryCache(Context context);

    void clearDiskCache(Context context);

    class Options {

        public static final int RES_NONE = -1;
        public int loadingResId = RES_NONE;//加载中的资源id
        public int loadErrorResId = RES_NONE;//加载失败的资源id

        public static Options defaultOptions() {
            return new Options(JConfig.IL_LOADING_RES, JConfig.IL_ERROR_RES);
        }

        public Options(int loadingResId, int loadErrorResId) {
            this.loadingResId = loadingResId;
            this.loadErrorResId = loadErrorResId;
        }
    }
}

//具体的实现
public class GlideLoader implements ILoader {
    @Override
    public void init(Context context) {

    }

    @Override
    public void loadNet(ImageView target, String url, Options options) {
        load(getRequestManager(target.getContext()).load(url), target, options);
    }

    @Override
    public void loadResource(ImageView target, int resId, Options options) {
        load(getRequestManager(target.getContext()).load(resId), target, options);
    }

    @Override
    public void loadAssets(ImageView target, String assetName, Options options) {
        load(getRequestManager(target.getContext()).load("file:///android_asset/" + assetName), target, options);
    }

    @Override
    public void loadFile(ImageView target, File file, Options options) {
        load(getRequestManager(target.getContext()).load(file), target, options);
    }

    @Override
    public void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    @Override
    public void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    private RequestManager getRequestManager(Context context) {
        return Glide.with(context);
    }

    private void load(DrawableTypeRequest request, ImageView target, Options options) {
        if (options == null) options = Options.defaultOptions();

        if (options.loadingResId != Options.RES_NONE) {
            request.placeholder(options.loadingResId);
        }
        if (options.loadErrorResId != Options.RES_NONE) {
            request.error(options.loadErrorResId);
        }
        request.crossFade().into(target);
    }
}

//暴露给上层加载用的Factory
public class LoaderFactory {
    private static ILoader loader;

    public static ILoader getLoader() {
        if (loader == null) {
            synchronized (LoaderFactory.class) {
                if (loader == null) {
                    loader = new GlideLoader();
                }
            }
        }
        return loader;
    }
}
```



 #### uihelper

uihelper是一个自定义View的依赖。所有不含业务型的View都应该放在这个module。

 #### APP
APP就是上层的项目了，注意在androidStudio的module中除了可以传递依赖以外，还可以有自己独立的依赖，所以我们可以在APP中对业务类型的依赖添加在此处。如：后端SDK、友盟统计、微博分享等，只属于该项目中才会出现的就应当放在此处。


来我们再来看一眼构架图，是不是理清楚多了。

![](http://upload-images.jianshu.io/upload_images/1110736-01822d6892367619.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

###对比：
一把梭项目架构：
>优点：
 - 快速开发
 - 无脑堆业务

> 缺点：
 - 维护成本高
 - 可阅读性差
 - 扩展性低

AndroidBasicLibs构架：

>优点:
 - 可扩展
 - 可重用
 - 可维护
 - 更快的速度开发
 - 底层与业务抽离

>缺点：
- 前期搭建费时间
- 依赖结构的层级略多


 # 总结
项目的架构设计绝不是一成不变的套路，应该根据业务的类型去做模块拆解。并且实际上个人认为很多时候开发第一个版本的项目即便你考虑去搭建一套框架去写。但会因为进度和协同合作方面等因素导致理想状态不太一样。在线上稳定住后，就应该考虑去重构你的项目。见过一位同事的编码风格是边写业务边重构之前的代码。会导致与你协同开发的同事。翻阅过后就懵逼了。每次都要重读一遍。这不是一个好办法。最好与团队中成员达成一致的意见后开整。

本来这篇我并不想发表的，怕是自己学艺不精误导了新人。加上工作上的变动，近期略显仓促。希望各位看官多多给点评。觉得有收获就点个喜欢就是对我最大的鼓励。

最后本框架的地址是：https://github.com/wwah/AndroidBasicLibs

# 如何下次找到我?
- 关注我的简书
- 本篇同步Github仓库:[https://github.com/BolexLiu/MyNote](https://github.com/BolexLiu/MyNote)  (欢迎star)
- 加入大鸡排QQ群一起撸码成长:110801914
![](http://upload-images.jianshu.io/upload_images/1110736-f0a700624e0723ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)