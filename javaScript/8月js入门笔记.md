#### 小知识
>* **NaN 表示非数值。**可以用isNaN(X);函数进行验证。
>* **Undefined理解上不等于null，但是Undefiend派生自null，所以如果进行比较会返回true。** 这里null为空值,而Undefined为未初始化和使用过。
typeof 运算符
typeof 运算符有一个参数，即要检查的变量或值。例如：
var sTemp = "test string";alert (typeof sTemp); //输出 "string"alert (typeof 86); //输出 "number"
对变量或值调用 typeof 运算符将返回下列值之一：
undefined - 如果变量是 Undefined 类型的
boolean - 如果变量是 Boolean 类型的
number - 如果变量是 Number 类型的
string - 如果变量是 String 类型的
object - 如果变量是一种引用类型或 Null 类型的


-------------------
#####取子串
    substring((starPos,stopPos);
    注意:如果strpos>StopPos 会导致参数交换位置。
![](http://upload-images.jianshu.io/upload_images/1110736-9106bc27d0bb0197.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 取子串指定长度
    substr(starPos,length);
    方法从字符串中提取从 startPos位置开始的指定数目的字符串。
    注意：如果参数startPos是负数，从字符串的尾部开始算起的位置。
    也就是说，-1 指字符串中最后一个字符，-2 指倒数第二个字符，以此类推。

####  大写转换 
    string.toUpperCase()

####大写转换 
    string.toLowerCase()


#### 取绝对位置的字符
    stringObject.charAt(index);
    charAt() 方法可返回指定位置的字符。返回的字符是长度为 1 的字符串。
#### 取字符出现的索引
     stringObject.indexOf(substring, startpos)
[![](http://upload-images.jianshu.io/upload_images/1110736-72035b95663b86ee.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://img.mukewang.com/53853d4200019feb04920149.jpg)
#### 切割字符串
     stringObject.split(separator,limit)
     如果把空字符串 ("") 用作 separator，那么 stringObject 中的每个字符之间都会被分割。
**[![](http://upload-images.jianshu.io/upload_images/1110736-303b714cbd939181.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://img.mukewang.com/532bee4800014c0404230108.jpg)**

-----

####Math对象
    Math对象，提供对数据的数学计算。
    var mypi=Math.PI;
    var myabs=Math.abs(-15);
Math 对象属性
[![](http://upload-images.jianshu.io/upload_images/1110736-8a208f94bf47f692.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://img.mukewang.com/532fe7cf0001e7b505170269.jpg)

Math 对象方法
[![](http://upload-images.jianshu.io/upload_images/1110736-b4c6ea939bde01f9.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://img.mukewang.com/532fe841000174db05160622.jpg)

-----

**location对象属性图示:**
[![](http://upload-images.jianshu.io/upload_images/1110736-a25ace47fb30d06c.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://img.mukewang.com/53605c5a0001b26909900216.jpg)
**location 对象属性：**
**[![](http://upload-images.jianshu.io/upload_images/1110736-1464cf2f1bb3196b.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://img.mukewang.com/5354b1d00001c4ec06220271.jpg)**
**location 对象方法:**
**[![](http://upload-images.jianshu.io/upload_images/1110736-98a9b8767d6fcec4.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://img.mukewang.com/5354b1eb00016a2405170126.jpg)**

      replace和assign的区别是assign会新增一条历史记录，而replace只是替换。

------------------

#####1.innerText
      是id为object的闭合标签内的文本,输入输出的是转义文本(字符串);  (label控件用innerText有效)

#####2.innerHtml
    是<div>标签内的文本,输入输出到该DOM内部纯HTML代码(流); 
     (获得td、div等html元素时候,它们是没有value或是text属性,只能用innerHtml)

####3.value
      是表单元素特有的属性,输入输出的是转义文本(字符串);  (Button、CheckBox、Radio)随表单一起发送的值;
     (Reset、Submit)标签;  (Text、Hidden)默认值;  (File、Password)  (注: Text控件用value有效)


-----

####document.getElementById('myHead');
    根据id取 返回结果唯一,如果标签中有多个就匹配第一个
####document.getElementsByName("sex");
     根据name取,返回结果为数组,因为name可以为多个
#####document.getElementsByTagName('input');
     根据TAG取,返回结果为数组,因为tag可以为多个

###区别getElementByID,getElementsByName,getElementsByTagName
**以人来举例说明，人有能标识身份的身份证，有姓名，有类别(大人、小孩、老人)等。**
>* ID 是一个人的身份证号码，是唯一的。所以通过getElementById获取的是指定的一个人。

>* Name 是他的名字，可以重复。所以通过getElementsByName获取名字相同的人集合。

>* TagName可看似某类，getElementsByTagName获取相同类的人集合。如获取小孩这类人，getElementsByTagName("小孩")。

**把上面的例子转换到HTML中，如下:**
```
<input type="checkbox" name="hobby" id="hobby1"> 音乐
```
input标签就像人的类别。
name属性就像人的姓名。
id属性就像人的身份证。

**方法总结如下:**
[![](http://upload-images.jianshu.io/upload_images/1110736-1686390fba2b947c.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)](http://img.mukewang.com/5405263300018bcf05760129.jpg)

----
####getAttribute()方法
    获取标签下的属性

####setAttribute()方法

    setAttribute() 方法增加一个指定名称和值的新属性，或者把一个现有的属性设定为指定的值。
    语法：
    elementNode.setAttribute(name,value)
    说明：
    1.name: 要设置的属性名。
    2.value: 要设置的属性值。