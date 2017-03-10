曾经那个不学好英语的蜜汁少年已在路上！
fuck Code 不止一两天。莫名我就喜欢你，虽然我没有学好英语！

![](http://upload-images.jianshu.io/upload_images/1110736-ed21260d305832be.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/500)

老夫聊发少年狂，治肾亏，不含糖。三百年，九芝堂。英语亏欠太多api都读不懂怎么办!是时候该放大招了。

---

 # 你能在本篇文章中收获什么?
- 学会制作as插件
- get划词翻译带记录历史的as插件
- 擒住一只活鸡排

#目录
- 冥想
- 找轮子
- coding
- 集成和使用
- 总结

---

#冥想

![](http://upload-images.jianshu.io/upload_images/1110736-0813f06ccaae4f2f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
提示：不想看太多过程和废话请直接跳至集成和使用！

ヾ(｡｀Д´｡)我擦嘞。老夫编程思想已惊天地泣鬼神、思想健壮、码到成功、功德圆满、满上满上 干了... 喂！喂！停一下你开始又乱飙车。岂能被几个英语给蹩了一脚。
开始尝试记单词，从哪里看起呢。百词斩、扇贝单词、开心词场原价都是一百多 两百多的，通通通通20块。
:-( 三天后玛德实在看不下去，那些单词软件里的东西和我写的代码好像没有半毛钱关系啊。要是有办法能把我每天不认识的编程单词都记录下来该多好呀！
仅仅只是翻译还不能满足我，我要的是以后你都在我的脑海里。
不行，我要在我的ide上直接翻译，还要把翻译的单词历史记录下来。还要方便阅读，最好是markdown格式。


### 需求：
- ide上的划词翻译
- 历史单词保存
- markdown格式方便阅读

---

#找轮子

那谁谁说过，不要重复造轮子。(偷懒开始)
那我们就开始搬运现成的翻译轮子呗！哈哈哈
也就是说我们需要找一款已经开源的翻译插件，在原有的基础上加上历史记录和markdown格式的保存。
ok，我们找到了ECTranslation，他已经集成了有道翻译在as中使用。并且支持了驼峰命名的代码翻译。比如常见的onCreate是无法直接翻译的。需要把on和Create拆开。这点已经在ECTranslation中非常完善了。

那么就让我们就开始愉快的扩展编码吧！ 快来吧。还等啥。老娘都不怕，你怕蛇？

![](http://upload-images.jianshu.io/upload_images/1110736-5ecaa744eb21b622.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)


# coding

把妹子分三步：灌醉 上车 锁好门<(￣3￣)> 。
1.首先我们要下载IntelliJ IDEA。
2.创建Plugin项目给它取一个名：ReciteWords(意思是：背单词)
3.编码
具体的创建工程请参考：[自己动手写一个Android Studio插件](http://www.jianshu.com/p/c2a3e673188b)

我们在工程目录中的src建一个AnAction触发器取名ReciteWords。

![](http://upload-images.jianshu.io/upload_images/1110736-1e41043a529560de.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
将ECTranslation轮子中关键的类复制进来。
RequestRunnable是一个翻译请求线程。
Translation是翻译结果对象。
Logger是日志工具。

###ReciteWords

```
public class ReciteWords extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
          //按下热键触发回调
    }

  ···· 省略代码
}

```
我们的ReciteWords继承了AnAction类，它提供了当你按下快捷热键就会触发回调actionPerformed()方法。那么我们可以在该方法中取出要翻译和记录的单词。 具体实现请移步Github看代码。地址在本文末尾。

###历史记录和markdown

```
    /**
     *  存单词
     * @param Words 原文
     * @param translate 译文
     * @throws IOException
     */
    private void saveWords( String Words,String translate) throws IOException {
        String usrHome = System.getProperty("user.home");
        File file = new File(usrHome+"\\ReciteWords.md");// 要写入的文本文件
        if (!file.exists()) {// 如果文件不存在，则创建该文件
            file.createNewFile();
            FileWriter writer = new FileWriter(file,true);// 获取该文件的输出流
            writer.write("# 这里是你该记住的单词。请用Markdown编辑器打开它。\r\n");
            writer.write("---\r\n\r\n");
            writer.flush();// 清空缓冲区，立即将输出流里的内容写到文件里
            writer.close();// 关闭输出流，施放资源
        }
        FileWriter writer = new FileWriter(file,true);
        writer.write("- "+Words+"\r\n");
        writer.write("```\r\n");
        writer.write(translate);
        writer.write("```\r\n");
        writer.flush();
        writer.close();
    }
```
注释很丰满，设计很简单。saveWords方法接受两个参数，原词和译文信息。通过java File操作写入文本。markdown的格式是预先写好的字符串，在调用方法时就会往里写入。

### 生成插件
最后我们将整个工程生成一个Jar文件。这个文件就是插件。

![](http://upload-images.jianshu.io/upload_images/1110736-11d354892457cdb6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

方法是点击Build找到Prepare Plugin Module选项就生成了插件。

![](http://upload-images.jianshu.io/upload_images/1110736-31513dbed99a7c18.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 集成和使用

- 打开Android Studio， Preferences -> Plugins -> Install plugin from disk -> 获取ReciteWords.jar安装并重启Android Studio。
- 选中代码，按下 Ctrl + Alt+Q(也可以自己设定)。即可翻译。效果如下:

![](http://upload-images.jianshu.io/upload_images/1110736-9039420fb0b9189c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 你所翻译的历史单词会被记录在你当前用户目录下的ReciteWords.md文件中（如:C:\Users\Bolex\ReciteWords.md）。可以通过Markdown编辑器打开它进行学习。效果如下:

![](http://upload-images.jianshu.io/upload_images/1110736-9e8aba26ca533e4c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 修改快捷键
如果你要改成自己的设置的快捷键也可以哦。
- Preferences -> Keymap -> 获取ReciteWords - > 右键 add Keyboard Shortcut. 输入你想要的快捷键即可。


# 总结

程序员英语不好的人当下一抓一大把，现在编程的门槛降低了。但是如果碰上了新技术，或者是深入的去看源码性质的东西。我想未必你能立刻找到中文文档。像我这种该读书的年纪没好好念书(高中贪玩)，后知后觉的才开始捡起来学的人更应该重视。毕竟学好英语等于如虎添翼呀。所以反过来看制作这样的插件是非常有必要的。如果单纯只停留在划词翻译上，可能还是没有针对性的去记忆。当时用得挺爽。过后也给忘记了。

最后如果你和我一样是个蹩脚英语的程序员，用这个插件的同时还是要回头去看那个历史记录去记哦。哈哈哈

**源码和插件下载地址：**https://github.com/BolexLiu/ReciteWords

---
# 如何下次找到我?
- 关注我的简书
- 本篇同步Github仓库:[https://github.com/BolexLiu/MyNote](https://github.com/BolexLiu/MyNote)  (欢迎star)
- 加入大鸡排QQ群一起撸码成长:110801914
![](http://upload-images.jianshu.io/upload_images/1110736-f0a700624e0723ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)