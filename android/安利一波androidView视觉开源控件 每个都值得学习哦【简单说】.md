# 安利一波androidView视觉开源控件 每个都值得学习哦【简单说】

啊哈!情人节又到了.简直是虐狗节好不好?
![](http://upload-images.jianshu.io/upload_images/1110736-ee4ed6d9a5995d23.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/800)


要想生活过得去,还得多学新东西.老铁没毛病! 下面我们就发车吧!!!
![](http://upload-images.jianshu.io/upload_images/1110736-df46932be3686c21.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


# 本篇包含了那些?
>
   - [ZuiMeiTAG](https://github.com/huage2580/ZuiMeiTAG)
   - [XyzInfo](https://github.com/zhangxuyang321/XyzInfo)
   - [ToDoList](https://github.com/Yalantis/ToDoList)
   - [FloatingView](https://github.com/UFreedom/FloatingView)
   - [CouponView](https://github.com/dongjunkun/CouponView)
   - [FiftyShadesOf](https://github.com/florent37/FiftyShadesOf)
   - [CircularAnim](https://github.com/XunMengWinter/CircularAnim)
---

# [ZuiMeiTAG](https://github.com/huage2580/ZuiMeiTAG)
用recyclerView实现最美应用底栏 效果如下图.
作者的简书说明地址是:http://www.jianshu.com/p/7202416974f6
![](http://upload-images.jianshu.io/upload_images/2495533-2c98b30ee98d288b.gif?imageMogr2/auto-orient/strip)


---

# [ToDoList](https://github.com/Yalantis/ToDoList)
 平滑的To-Do List添加,删除和排序动画效果。

![](http://upload-images.jianshu.io/upload_images/1110736-04c27414ba61c361.gif?imageMogr2/auto-orient/strip)

---
#  [FloatingView](https://github.com/UFreedom/FloatingView)
  能够让View执行漂亮的漂浮动画的库。
![](http://upload-images.jianshu.io/upload_images/1110736-71ef2625cb088d57.gif?imageMogr2/auto-orient/strip)

---

# [CouponView](https://github.com/dongjunkun/CouponView)
半圆锯齿背景虚线边框组合实现简单优惠券效果
简书地址:http://www.jianshu.com/p/b27d0f9b3856
![](http://upload-images.jianshu.io/upload_images/1110736-f19dcea8e4cd23fc.gif?imageMogr2/auto-orient/strip)

## 支持属性
|           自定义属性            |    格式     |        说明        |
| :------------------------: | :-------: | :--------------: |
|     cv_dash_line_color     |   color   |      虚线的颜色       |
|      cv_dash_line_gap      | dimension |      虚线的间隔       |
|    cv_dash_line_height     | dimension |      虚线的高度       |
|    cv_dash_line_length     | dimension |      虚线的长度       |
|    cv_semicircle_color     |   color   | 半圆的颜色，一般需要和背景色一致 |
|     cv_semicircle_gap      | dimension |     半圆之前的间隔      |
|    cv_semicircle_radius    | dimension |      半圆的半径       |
|    cv_is_semicircle_top    |  boolean  |    是否绘制顶部半圆锯齿    |
|  cv_is_semicircle_bottom   |  boolean  |    是否绘制底部半圆锯齿    |
|   cv_is_semicircle_left    |  boolean  |    是否绘制左侧半圆锯齿    |
|   cv_is_semicircle_right   |  boolean  |    是否绘制右侧半圆锯齿    |
|    cv_is_dash_line_top     |  boolean  |     是否绘制顶部虚线     |
|   cv_is_dash_line_bottom   |  boolean  |     是否绘制底部虚线     |
|    cv_is_dash_line_left    |  boolean  |     是否绘制左侧虚线     |
|   cv_is_dash_line_right    |  boolean  |     是否绘制右侧虚线     |
|  cv_dash_line_margin_top   | dimension | 顶部虚线距离View顶部的距离  |
| cv_dash_line_margin_bottom | dimension | 底部虚线距离View底部的距离  |
|  cv_dash_line_margin_left  | dimension | 左侧虚线距离View左侧的距离  |
| cv_dash_line_margin_right  | dimension | 右侧虚线距离View右侧的距离  |

---

# [FiftyShadesOf](https://github.com/florent37/FiftyShadesOf)
  一个android加载View模糊loding效果,优点类似Facebook的加载
![](http://upload-images.jianshu.io/upload_images/1110736-b1e00f3a282d658e.gif?imageMogr2/auto-orient/strip)

---

#  [CircularAnim](https://github.com/XunMengWinter/CircularAnim)
Android水波动画帮助类，一行代码实现View显示/隐藏/startActivity特效。(对 ViewAnimationUtils.createCircularReveal() 方法的封装)


![](http://upload-images.jianshu.io/upload_images/1110736-4a75500b297e5b13.gif?imageMogr2/auto-orient/strip)

![](http://upload-images.jianshu.io/upload_images/1110736-cf4f5475a67b1b19.gif?imageMogr2/auto-orient/strip)

## 使用说明
为了使用起来简单，我将动画封装成CircularAnimUtil.
- 现在，让按钮收缩只需一行代码，如下：
```
CircularAnimUtil.hide(mChangeBtn);
```
- 同理，让按钮伸展开：
```
CircularAnimUtil.show(mChangeBtn);
```
- 水波般铺满指定颜色并启动一个Activity:
```
CircularAnimUtil.startActivity(MainActivity.this, EmptyActivity.class, view, R.color.colorPrimary);
```
- 这里，你还可以放图片：
```
CircularAnimUtil.startActivity(MainActivity.this, EmptyActivity.class, view, R.mipmap.img_huoer_black);
```
- 也许在显示或隐藏视图时，你想要设置半径和时长，你可以调用这个方法：
```
显示：show(View myView, float startRadius, long durationMills)
隐藏：hide(final View myView, float endRadius, long durationMills)
```
- 以及，你可以在startActivity时带上Intent:
```
startActivity(Activity thisActivity, Intent intent, View triggerView, int colorOrImageRes)
```
- 还可以startActivityForResult:
```
startActivityForResult(Activity thisActivity, Intent intent, Integer requestCode, View triggerView, int colorOrImageRes)
```
同理，startActivity同样可以设置时长。
用起来非常的方便，一切逻辑性的东西都由帮助类搞定。


---

# [XyzInfo](https://github.com/zhangxuyang321/XyzInfo)
  自定义Switch 和 Ruler控件

![](http://upload-images.jianshu.io/upload_images/1110736-bfd6ddaa8c45a923.gif?imageMogr2/auto-orient/strip)


## 介绍
xSwitch | xRuler
 --- | ---
支持背与滑块颜色自定义          | 支持两种显示模式
支持颜色过渡动画               | 支持上下两种刻度
支持自定义圆角弧度             | 支持自定义指示器颜色大小
支持自定义宽高比               |支持自定义刻度之间像素数目(间距)
支持自定义滑块与背景大小比例     |支持自定义刻度线宽度
支持自定义图片(建议灰色)以及大小 |支持自定义刻度基本单位(最小刻度单位)
支持自定义滑动时间             |支持自定义起止范围
                                  |支持自定义字体颜色大小
                                  |支持自定义最小滑动速率
                                  |支持自定义刻度与文字之间距离
                                  |支持边界回弹动画
                                  |支持Fling滑动
                                  |支持设置默认值
                                  |支持自定义设置回弹动画基本时间

## 属性介绍

属性 | 介绍 | 类型 | 默认 | 是否必须
--- | --- | --- | --- | ---
xRadian | 圆角弧度 | dimension | 45 |否
xASPECTRATIO | 宽高比 | float | 0.37f | 否
xASPEERRATIO | 滑块与背景宽度比 | float | 0.56f | 否
xIconWidth | 图片宽度 |dimension | 80 | 否
xIconHeight | 图片高度 | dimension | 80 | 否
xBackgroundFromColor | 背景起始颜色 | color |0xff443c6f | 否
xBackgroundToColor |背景终止颜色 | color | 0xffff5a73 | 否
xSliderFromColor | 滑块起始颜色 | color | 0xffff5a73 | 否
xSliderToColor |滑块终止颜色 | color | 0xff443c6f | 否
xChangedTime | 滑块滑动时间 | int | 300 | 否
xLeftIcon | 左侧图片 | reference | null | 是
xRightIcon | 右侧图片 | reference | null |是

```
   compile 'com.xyz.xruler:xruler:1.0.3'
```

---

# 看完了不得劲?
另外还有两篇简单说哦:
[2017Android开发你必须要掌握的热门开源框架 【简单说】](http://www.jianshu.com/p/9d65b6eb28fe)
[别找了啦,俺都藏在这里 Material Design控件军火库集合 【简单说】](http://www.jianshu.com/p/4aaf04749f16)

本篇同步Github仓库:[https://github.com/BolexLiu/MyNote](https://github.com/BolexLiu/MyNote)  (欢迎star)
加入大鸡排QQ群一起撸码成长:110801914

![](http://upload-images.jianshu.io/upload_images/1110736-1177be5ca0166201.gif?imageMogr2/auto-orient/strip)