##display: 表示为伸缩容器
[参考属性]()：
- flex  |设置为容器
- inline-flex| 行内元素设置
![容器模型](http://upload-images.jianshu.io/upload_images/1110736-8a069d4809b3dfc2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

---
##flex-direction: 设置容器的方向
[参考属性]()：
-  row                   |    水平排列 从左往右(图3)
-   column             |  垂直排列 从上往下(图2)
-  row-reverse       |  反向水平排列 从右往左 (图4)
- column-reverse |  反向垂直 从下往上 (图1)

![flex-direction](http://upload-images.jianshu.io/upload_images/1110736-f1cf7ea2328330fa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

---
##flex-wrap: 容器空间不足时是否换行显示
[参考属性]()：
- nowrap  | 不允许换行
- wrap  |  换行
- wrap-reverse | 反向排序并向上换行

![wrap](http://upload-images.jianshu.io/upload_images/1110736-3aa02914091ac5ad.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

---
##flex-flow: 该属性是direciton和wrap的缩写属性,是两个属性的缩写
- 默认值为row nowrap

---
##justify-content:沿主轴对齐的方式(主轴方向参考flex-direciton属性来确定)|
[参考属性]()：
- flex-start |从主轴起始位置对齐   (左对齐)
- flex-end | 从主轴结束位置对齐  (右对齐)
- center | 向主轴方向中间靠齐 ( 居中)
- space-between| 平均分布在主轴上 第一个view紧贴主轴起始点,最后一个view紧贴主轴结束点  (两端对齐)
-space-around| 平均分布 主轴两端不紧贴    (两端对齐但不贴边,但每个项目两侧的间隔相等)


![justify-content](http://upload-images.jianshu.io/upload_images/1110736-1250eee0f710e1ac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

---
##align-items: 对于交叉轴的位置
- flex-start | 起始对齐
- flex-end | 结束对齐
- center | 居中
- baseline | 基线对齐 也就是基于内容对齐
- stretch | 拉伸铺满

![align-items](http://upload-images.jianshu.io/upload_images/1110736-8f3434d332397ed4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

---
##align-content: 在开启换行后,或者多行展示主轴内容后生效在交叉轴上排列的属性
- flex-start |与交叉轴的起点对齐。
- flex-end | 与交叉轴的终点对齐。
- center | 与交叉轴的中点对齐。
- space-between | 与交叉轴两端对齐，轴线之间的间隔平均分布。
- space-around | 每根轴线两侧的间隔都相等。所以，轴线之间的间隔比轴线与边框的间隔大一倍。
- stretch（默认值） |轴线占满整个交叉轴。

![align-content](http://upload-images.jianshu.io/upload_images/1110736-fed6cbccca38297f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

本文参考了 [阮一峰的Flex 布局教程：语法篇](http://www.ruanyifeng.com/blog/2015/07/flex-grammar.html)
图片均来自阮一峰的博客