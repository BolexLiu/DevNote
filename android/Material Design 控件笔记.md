#Material Design 控件库

 ---
##DrawerLayout
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
---

##MaterialDrawer

提供比NavigationView 更好的交互效果的侧滑菜单项
github:[MaterialDrawer](https://github.com/mikepenz/MaterialDrawer)

![MaterialDrawer.png](http://upload-images.jianshu.io/upload_images/1110736-d25bc73656328fbd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/600)

---
##material-menu

提供带切换效果的Ioc
github:[material-menu](https://github.com/balysv/material-menu)

![material-menu.gif](http://upload-images.jianshu.io/upload_images/1110736-e48a272d1894fef2.gif?imageMogr2/auto-orient/strip)

---

##CoordinatorLayout && AppBarLayout
 - 如果你想做出嵌套View滚动效果那么必须用它来实现
 - 他和CoordinatorLayout是一对的.CoordinatorLayout的直接子View必须是它.因为AppBarLayout.ScrollingViewBehavior对子View滑动响应事件直接做了处理.
在另外一个嵌套的View中必须设置app:layout_behavior="@string/appbar_scrolling_view_behavior"这个特殊的值.也可以自定义Behavior.
 - 具体用法参考:[泡在网上的日子](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0717/3196.html)
 - [[CoordinatorLayout的使用如此简单](http://blog.csdn.net/huachao1001/article/details/51554608)](http://blog.csdn.net/huachao1001/article/details/51554608)
 - [CoordinatorLayout布局的使用方式](http://www.jianshu.com/p/97206f5973c5)
---

##CollapsingToolbarLayout
实现视差滚动动画和Toolbar滚动
![CollapsingToolbarLayout .gif](http://upload-images.jianshu.io/upload_images/1110736-9764adce96c412ad.gif?imageMogr2/auto-orient/strip)
 [CoordinatorLayout与CollapsingToolbarLayout实现视差滚动动画和Toolbar滚动](http://blog.csdn.net/a8341025123/article/details/53006865)

---
更多请参考:[最全面的 Material Design 学习资料](https://github.com/Luosunce/material-design-data)