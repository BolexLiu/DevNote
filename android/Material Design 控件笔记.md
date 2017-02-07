
---
# 别找了啦,俺都藏在这里 Material Design 控件大全 【简单说】

## 本篇适合哪些人?
- 刚刚从基于android 4.4以下项目中切出来,还没有用过5.0+新特性
- 新人入门学点牛逼特效控件
- 撸一个爱屁屁

## 本篇包含
- MaterialDrawer
- material-menu
- CoordinatorLayout
- AppBarLayout
- CollapsingToolbarLayout
- DrawerLayout
- MaterialDesignLibrary
- material
- MaterialDesignInXamlToolkit


 我先抽一口,再和你讲. 啊~舒坦.
![](http://upload-images.jianshu.io/upload_images/1110736-e6a5d87e22ee3db6.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/200)





----

## MaterialDrawer
https://github.com/mikepenz/MaterialDrawer
提供比NavigationView 更好的交互效果的侧滑菜单项


![MaterialDrawer.png](http://upload-images.jianshu.io/upload_images/1110736-d25bc73656328fbd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/600)

---
## material-menu

提供带切换效果的Ioc
github:[material-menu](https://github.com/balysv/material-menu)

![material-menu.gif](http://upload-images.jianshu.io/upload_images/1110736-e48a272d1894fef2.gif?imageMogr2/auto-orient/strip)

---

## CoordinatorLayout && AppBarLayout
 - 如果你想做出嵌套View滚动效果那么必须用它来实现
 - 他和CoordinatorLayout是一对的.CoordinatorLayout的直接子View必须是它.因为AppBarLayout.ScrollingViewBehavior对子View滑动响应事件直接做了处理.
在另外一个嵌套的View中必须设置app:layout_behavior="@string/appbar_scrolling_view_behavior"这个特殊的值.也可以自定义Behavior.
 - 具体用法参考:[泡在网上的日子](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0717/3196.html)
 - [[CoordinatorLayout的使用如此简单](http://blog.csdn.net/huachao1001/article/details/51554608)](http://blog.csdn.net/huachao1001/article/details/51554608)
 - [CoordinatorLayout布局的使用方式](http://www.jianshu.com/p/97206f5973c5)


## CollapsingToolbarLayout
实现视差滚动动画和Toolbar滚动
![CollapsingToolbarLayout .gif](http://upload-images.jianshu.io/upload_images/1110736-9764adce96c412ad.gif?imageMogr2/auto-orient/strip)
 [CoordinatorLayout与CollapsingToolbarLayout实现视差滚动动画和Toolbar滚动](http://blog.csdn.net/a8341025123/article/details/53006865)

---
## DrawerLayout
提供类似 [SlidingMenu](https://github.com/jfeinstein10/SlidingMenu)的官方支持库(侧滑菜单)


![1.gif](http://upload-images.jianshu.io/upload_images/1110736-669b00571ef03f80.gif?imageMogr2/auto-orient/strip)
``` xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.v4.widget.DrawerLayout
        android:id="@+id/simple_navigation_drawer"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <!--内容视图-->
        <include
            android:id="@+id/tv_content"
            layout="@layout/drawer_content_layout"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

        <!--左侧滑菜单栏-->
        <include
            layout="@layout/drawer_menu_layout"
            android:layout_width="250dp"
            android:layout_height="match_parent"
            android:layout_gravity="start" />

        <!--右侧滑菜单栏-->
        <include
            layout="@layout/drawer_menu_layout"
            android:layout_width="250dp"
            android:layout_height="match_parent"
            android:layout_gravity="end" />


    </android.support.v4.widget.DrawerLayout>


</RelativeLayout>
```

--
## discreteSeekBar
https://github.com/AnderWeb/discreteSeekBar
百分比滑动条

![](http://upload-images.jianshu.io/upload_images/1110736-6333f414ad1dbd71.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

---
## MaterialDesignLibrary
https://github.com/navasmdc/MaterialDesignLibrary
一个支持兼容到android2.2 的依赖库.很强大. 详情见github.

![](http://upload-images.jianshu.io/upload_images/1110736-ab4589bf00ebea7c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

----

## material
https://github.com/rey5137/material
  一大波封装完整的组件 直接调用就可以了

[![](https://github.com/lightSky/Awesome-MaterialDesign/raw/master/demoRes/material-a.gif)](https://github.com/lightSky/Awesome-MaterialDesign/blob/master/demoRes/material-a.gif) [![](https://github.com/lightSky/Awesome-MaterialDesign/raw/master/demoRes/material-d.gif)](https://github.com/lightSky/Awesome-MaterialDesign/blob/master/demoRes/material-d.gif) [![](https://github.com/lightSky/Awesome-MaterialDesign/raw/master/demoRes/material-e.gif)](https://github.com/lightSky/Awesome-MaterialDesign/blob/master/demoRes/material-e.gif) [![](https://github.com/lightSky/Awesome-MaterialDesign/raw/master/demoRes/material-f.gif)](https://github.com/lightSky/Awesome-MaterialDesign/blob/master/demoRes/material-f.gif) [![](https://github.com/lightSky/Awesome-MaterialDesign/raw/master/demoRes/material-g.gif)](https://github.com/lightSky/Awesome-MaterialDesign/blob/master/demoRes/material-g.gif) [![](https://github.com/lightSky/Awesome-MaterialDesign/raw/master/demoRes/material-h.gif)](https://github.com/lightSky/Awesome-MaterialDesign/blob/master/demoRes/material-h.gif) [![](https://github.com/lightSky/Awesome-MaterialDesign/raw/master/demoRes/material-i.gif)](https://github.com/lightSky/Awesome-MaterialDesign/blob/master/demoRes/material-i.gif) [![](https://github.com/lightSky/Awesome-MaterialDesign/raw/master/demoRes/material-b.gif)](https://github.com/lightSky/Awesome-MaterialDesign/blob/master/demoRes/material-b.gif) [![](https://github.com/lightSky/Awesome-MaterialDesign/raw/master/demoRes/material-c.gif)](https://github.com/lightSky/Awesome-MaterialDesign/blob/master/demoRes/material-c.gif) 


---
## MaterialDesignInXamlToolkit
https://github.com/ButchersBoy/MaterialDesignInXamlToolkit
 同上都是组件军火库

![](http://upload-images.jianshu.io/upload_images/1110736-55edca3624e16e57.gif?imageMogr2/auto-orient/strip)




---
更多设计请参考:[最全面的 Material Design 学习资料](https://github.com/Luosunce/material-design-data)
本篇同步Github仓库:[https://github.com/BolexLiu/MyNote](https://github.com/BolexLiu/MyNote)  (欢迎star)
加入大鸡排QQ群一起撸码成长:110801914

![](http://upload-images.jianshu.io/upload_images/1110736-535d5b771e2291d0.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)