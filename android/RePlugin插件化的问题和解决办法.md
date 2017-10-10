这篇文章基于 Replugin2.2版本的，后续可能会随着框架改动和bug修复有些问题可能就不存在了。如果你跟我一样也是对插件化开发跃跃欲试，又在里面栽跟头了，我想本片文章可能会对大家提供点小小的帮助。最终问题的解决还是离不开对框架原理的熟悉。否则遇到了问题也不知道从何下手。孤陋寡闻的我可能踩了一些很低级的错误，还望各位大大们在发现本文讲得不对的地方指正。


-----
## 插件使用宿主的对象

1.我想在插件中使用宿主的new出来的对象，但是如果在插件中和宿主中同时依赖一份资源是无法使用的，会报类转化异常。比如宿主和插件都要用okhttp3，但是我不想插件中也要初始化一个。

**答：**
只在宿主中通过dependencies的方式依赖一下，然后将工程切换成Project模式。如下图

![](http://upload-images.jianshu.io/upload_images/1110736-e18570fbf0b01f1a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

找到这个依赖文件，在jar文件上右键打开目录。

![](http://upload-images.jianshu.io/upload_images/1110736-692713e17c11f1eb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![](http://upload-images.jianshu.io/upload_images/1110736-7d9b4211ba363cad.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

将这个jar文件拷贝到你的插件目录下。然后通过provided的方式进行编译依赖。

```
 provided files('libs/xxx.jar')

```

注意，provided仅仅是编译依赖，不会打包进apk包里。这样当Replugin框架在dex加载的时候拿到的就是宿主的class。就不会造成类转化异常了。

**小技巧提示：**
如果你的插件需要公共host里面的一些类和基础服务。（注意不含资源，资源指图片和xml）
可以在host里剥离出一个模块，让host去依赖它，然后将该模块打包成jar的方式，提供给插件使用。犹如sdk的开发模式，但是一定要注意务必使用provided关键字。莫打包进去了。

**模块打包成jar的方式如下：**

在依赖模块中的gradle里添加配置：

```
android {
  lintOptions {
        abortOnError false
    }

 }
//注意makeJar放在外层 
task makeJar(type: Jar, dependsOn: ['build']) {
    destinationDir = file('build/outputs/jar/')
    baseName = "AppSDK"
    version = "1.0.0"
    from('build/intermediates/classes/debug')
    exclude('**/BuildConfig.class')
    exclude('**/BuildConfig\$*.class')
    exclude('**/R.class')
    exclude('**/R\$*.class')
    include('**/*.class')
}

````
打开gradle命令列表（在as的右侧，看到没？）
找到该模块的的other选项

![](http://upload-images.jianshu.io/upload_images/1110736-75b1dd529af07fa4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

找到 makeJar

![](http://upload-images.jianshu.io/upload_images/1110736-d4f19fd1bc37ff5b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

ok，这样就编译好了，接下来复制到插件的libs里 provided files一下即可。

这里比较麻烦的事是，每当你修改过这个依赖模块的代码都需要重写打包一份jar。项目上线后如果宿主没有升级，那么最好不要改动依赖模块中的代码。

---

## v7包

**1.插件布局加载后莫名其妙的多了一些外边距，无法全屏 独立运行是正常的**

**答：**
插件不能共享资源宿主的v7包，注意如果你违反这条去尝试开发插件。会导致你的插件apk布局莫名其妙的多出一些边距。

**注意：**
即便插件中你没有依赖v7包，也是可以使用AppCompatActivity的。但是包里不会包含v7包的资源。
是因为SdkVersion版本过高所以可以直接使用。

**解决办法：**
在插件中单独依赖一份v7包，而不是共享宿主的。

----

**2.插件中加载的布局是上一个宿主的布局**

**答：**
你使用了基础的Activity，没有使用v7包里的AppCompatActivity并且关闭了 useAppCompat = false

**解决办法：**
如果你的抛资源找不到错误，先尝试使用RePlugin.getPluginContext() 取获取资源。
加载布局文件配合     LayoutInflater.from()。塞入View。或者直接替换成AppCompatActivity就没有这个问题了。（推荐用AppCompatActivity问题会少很多）


----

## 插件横屏

插件目前manifest配置了横屏是无效的。这也许是坑位导致的，可以动态在onCreate的时候改变一下
```
  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
          ·····
}

```
---
## 热更新插件

wiki文档里已经讲过了，对于没有启动（加载过）的插件是可以安装后就运行的。但是若插件被启动过或正在运行时则不行。针对特殊的应用场景不得不重启app，而且还是主动重启。
那么发现如果开启了    persistentEnable = true 常驻应用内存。（就是一个服务）会导致即便重启了app，插件安装后还是没有被更新。这里迫使我关掉了persistentEnable选项，随后重启后就成功更新了。注意我这里说的重启是非root下，应用自己杀死自己，而非用户主动kill掉。

----

## 幽灵魔术

什么叫幽灵魔术问题？就和变魔术一样，我好像没有改什么逻辑性的代码，突然bug又好了。
多半是你动了rePlugin中的gradle里的配置项，可能留下了上一次的缓存在里面，你要做的就是每当改动了gradle里的配置最好搭配Clean Project一下。如下图：

![](http://upload-images.jianshu.io/upload_images/1110736-660b4c7314fd102d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



然后有其他问题可以去参考https://github.com/Qihoo360/RePlugin/issues。如果找不到的情况下可以加官方的群。里面还是比较活跃的。

这里再次说明一个问题，跑demo玩测一测和距离正式项目使用还是有很大的区别的。很多问题在demo里看不出来的，只有真正用的时候才会踩到。尽管说replugin的文章说只hook了一个点来确保稳定性。但是由于开发人员对框架本身的不够熟悉，和存在的一些bug。导致接入成本不能说极低了。如果要考虑团队使用，最好由提前做有一个人来做预研接入再推广给大家使用。熟悉这种模式后，可能会是如虎添翼。