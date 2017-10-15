**注意：**本篇是一个以方法论为导向的文章。

**Q1：Smali是什么。**
>Smali是一种宽松式的Jasmin/dedexer语法.

简单来说就是我们用java写的代码编译成class打包成dex文件后使用baksmali程序逆向回来的一种语法。

**Q2：为什么要学习Smali。**
首先，提到smali就不得不说逆向。早在还没有android之前，各大平台和语言上就有对应的逆向一说。那么到目前为止，逆向一个apk通常是安全工程师（逆向工程师）和做破解等恶意分子因为某些利益在做（apk二次打包插入广告、破解收费应用、恶意代码植入、剽窃api等）。

技术是一把双刃剑，怎么用在人。而不在技术本身上。那么我们说为什么应用层开发者也要学smali呢？我能想到以下几点供参考。

1.**借鉴** 当我们发现其他应用有一个很牛逼功能，而我们想不明白如何实现的时候。拿不到源码可以选择逆向。
2.**安全** 我们写的app需要考虑安全性，但是我们可能只知道混淆和第三方加固，需要明白别人是怎么破解我们的应用。
3.**适配** 当我们发现api在某些手机上被弃用，而其他应用或系统应用又能实现该功能的时候。关于这点我之前写过一篇[逆向小米做适配的文章](http://www.jianshu.com/p/6f313b4876ab)。

喂，差不多够了吧？还不能打动你学吗？给你升职加薪怎么样？ :)

噗，坏蛋！！！，我学 我学，还不行吗？

**Q3：Smali难不难？**
不难。也许你很早之前看过一些文章。或者也常用一些工具去打开反编译后的代码。看着一团麻的指令和一些你从未见过的关键字、代码格式风格，赖不住性子就潦草的关掉了。但实际上是你没有找对方法来学习它。

**Q4：怎么学**
我一向的风格都是不爱把知识生拉硬套的往脑子里塞，我更加习惯从实践中去分析，而后反过来做总结。现在给大家推荐一款好用的Smali学习工具插件。我们打开AndroidStudio找到插件安装的位置。如下图

![](http://upload-images.jianshu.io/upload_images/1110736-962ab78e97217329.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
打开 Browse Repositories，输入java2smali安装重启即可。

![](http://upload-images.jianshu.io/upload_images/1110736-9a2d862deec356f2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
github地址在这里：**[intellij-java2smali](https://github.com/ollide/intellij-java2smali)**

这个步骤以后，我们就可以愉快的将任何java代码在androidStudio中直接转换成smali来学习里。步骤如下。
1.编写一个最简单的java文件。比如下面这样的。


![](http://upload-images.jianshu.io/upload_images/1110736-36a006f00078288e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

然后我们点击Build->Compile to smali

![](http://upload-images.jianshu.io/upload_images/1110736-8b668a88ad330ace.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

稍等几秒钟后就会得到smali文件。

![](http://upload-images.jianshu.io/upload_images/1110736-0b3b36c3d2b21f8a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

接下来我们就可以对照着java代码来逐行分析这个smali文件。如果是第一次看我们可能会被一些没见过关键字干扰到。其实这里有个很简单的办法。注意**.line**关键字就是用来描述当前代码在java源文件中的行数。然后你可以通过对照两组代码的方法进行反推。这样就可以很轻松的学会看smali文件。

好下面是一个示例代码，供参考。





### 示例代码：

**原java代码**
```
    public AA methodAReturn(AA mAA, AA sAA) {
        return mAA;
    }
AA aa= new AA();
//调用
  methodAReturn(aa, aa);

```

**Smali代码**
```
.method public methodAReturn(Lcom/bolex/AA;Lcom/bolex/AA;)Lcom/bolex/AA;
    .registers 3
    .param p1, "mAA"    # Lcom/bolex/AA;
    .param p2, "sAA"    # Lcom/bolex/AA;

    .prologue
    .line 34
    return-object p1
.end method

  .line 21
    new-instance v0, Lcom/bolex/AA;
    invoke-direct {v0}, Lcom/bolex/AA;-><init>()V
    .line 22
    invoke-virtual {p0, v0, v0}, Lcom/bolex/seamAct;->methodAReturn(Lcom/bolex/AA;Lcom/bolex/AA;)Lcom/bolex/AA;
```

###   .line
```
  .line 34
```
表示当前代码在源java文件中的行数。

### method
```
.method public methodAReturn(Lcom/bolex/AA;Lcom/bolex/AA;)Lcom/bolex/AA;
```
表示来自公共方法methodAReturn返回值是一个对象com.bolex.AA

### registers
```
.registers 3
```
表示该函数上需要使用3个寄存器

## param
```
    .param p1, "mAA"    # Lcom/bolex/AA;
    .param p2, "sAA"    # Lcom/bolex/AA;

```
表示接收两个入参都是AA对象，并标记寄存器p1和p2

## .prologue

```
    .prologue
```
表示函数内执行的起始标记。直译为开场白的意思。

## .line
```
   .line 34
```
表示在源代码中的第34行。

##  return-object

```
  return-object p1
```

表示 返回寄存器上p1对象

## .end method
```
.end method
```
表示函数结束标记

##    new-instance
```
 new-instance v0, Lcom/bolex/AA;
```
创建一个AA对象

## invoke-direct
```
 invoke-direct {v0}, Lcom/bolex/AA;-><init>()V
```
表示使用无参构造方法直接调用

##  invoke-virtual
```
 invoke-virtual {p0, v0, v0}, Lcom/bolex/seamAct;->methodAReturn(Lcom/bolex/AA;Lcom/bolex/AA;)Lcom/bolex/AA;
```
表示为虚拟方法

---

就是这个样子的，有没有很简单呢？

以上只举例了部分关键字，更多的关键字可以自行依赖两组文件反推。其实有时候更加讲究的是一个方法。我觉得这个方法就挺不错的，所以就分享给大家咯，咱也不需要刻意去背下来。熟能生巧，玩多了岂能不是老司机？

关于smali的知识还有很多本文并未详细阐述，如寄存器、类型（原始类型、对象类型）、数组方法的表示形式。如读者还需要进一步深入挖。可以参考官方文档。里面有详细的解释，已翻译成中文版了。
https://source.android.com/devices/tech/dalvik/dex-format

---

## 如何下次找到我?

- 关注我的简书
- 本篇同步Github仓库:[https://github.com/BolexLiu/DevNote](https://github.com/BolexLiu/DevNote) (可以关注)![](http://upload-images.jianshu.io/upload_images/1110736-ee7c10ba951a8a88.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**本文发自简书-香脆的大鸡排。原创文章，未授权禁止转载。**