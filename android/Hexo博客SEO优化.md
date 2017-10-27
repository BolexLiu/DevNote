


很多小伙伴们都有属于自己的博客，其中有一部分是机遇Hexo搭建的，但是好多都没有做优化，所以就有了本篇文章，希望对你有帮助。

14年的时候搭建过一个用wordpress的博客，不过后面慢慢的转向了简书后就停止更新了。这两天用hexo重新捣鼓了下。基于github上做静态资源，使用了next主题。（这样的话你只需要一个域名，其他统统免费。）

地址是**[www.dajipai.cc](http://www.dajipai.cc)**

# 目录
- 站点地图
- robots配置
- Google Search Console
- 百度站长平台
- Url持久化
- nofollow
- 页面关键字优化
----
<!-- more -->

###  站点地图
首先安装`sitemap`和百度版本的`sitemap`
```
npm install hexo-generator-sitemap --save
npm install hexo-generator-baidu-sitemap --save
```
打开配置文件` _config.yml `添加
```
sitemap:
path: sitemap.xml
baidusitemap:
path: baidusitemap.xml
```
OK，搞定了这样会在hexo g 编译时会在网站目录上sitemap.xml文件。该文件是提供给搜索爬虫用的。不出意外是下面这样的如下图：

![](http://upload-images.jianshu.io/upload_images/1110736-9b6e38e8838e1374.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

打开里面的内容：
 ```xml
<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
  <url>
    <loc>http://dajipai.cc/archives/e8998065.html</loc>
    <lastmod>2017-10-27T05:16:15.000Z</lastmod>
    <data>
        <display>
        <title>FlexBox布局属性笔记</title>
        <pubTime>2016-09-11T06:20:03.000Z</pubTime>
        <tag>FlexBox</tag>
        <tag>布局</tag>
       <breadCrumb title="react-native" url="http://dajipai.cc/categories/react-native/"/>
        </display>
    </data>
    </url>
</urlset>
```

参考：http://dajipai.cc/baidusitemap.xml

----
### robots配置
```
User-agent: *
Allow: /
Allow: /home/
Allow: /archives/
Allow: /about/
Disallow: /vendors/
Disallow: /js/
Disallow: /css/
Disallow: /fonts/
Disallow: /vendors/
Disallow: /fancybox/
Sitemap: http://dajipai.cc/sitemap.xml
Sitemap: http://dajipai.cc/baidusitemap.xml
```
参考：http://dajipai.cc/robots.txt

Allow表示允许被访问的，Disallow是不允许的意思。注意后面两个Sitemap就是网站地图了。而网站地图前面说了是给爬虫用的。这里配置在robots中。

---
### Google Search Console

https://www.google.com/webmasters/tools/home?hl=zh-CN
针对谷歌我们登陆谷歌搜索控制台在里面添加自己的站点就可以了。添加方法也很简单就是将一个带key的html从google下载下来后放到我们的自己的博客更目录上就可以了。
![](http://upload-images.jianshu.io/upload_images/1110736-d9cd22e4941ca45f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

然后我们找到网站地图。

![](http://upload-images.jianshu.io/upload_images/1110736-f21facb97d275bea.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

点击添加站点地图

![](http://upload-images.jianshu.io/upload_images/1110736-c25e5f459fd4657b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

再将我们之前生成好的地图地址贴入即可。

![](http://upload-images.jianshu.io/upload_images/1110736-3aec0a7c797f482e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

然后我们点击robots.txt测试工具

![](http://upload-images.jianshu.io/upload_images/1110736-68d2f4b53821c67f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
确保没有错误即可。
其他里面还有一些好用的工具可以自行测试玩玩。

---

###  百度站长平台
http://zhanzhang.baidu.com/
百度的玩法几乎和谷歌一毛一样，没什么区别。
首先也是要配置站点认证，我这边是之前配置好的，所以没法截图出来了。这里有一个坑需要注意。如果你的hexo博客是托管在github上的就不要用html静态key来做验证了。应该是域名解析的方式也就是别名解析。

认证成功后找到Robots：

![](http://upload-images.jianshu.io/upload_images/1110736-6c963bc288bec97f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

检测更新一下，注意默认协议就是指向你的域名后面加/robots.txt文件

![](http://upload-images.jianshu.io/upload_images/1110736-da5a610c8bb40234.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


### Url持久化
我们可以发现hexo默认生成的文章地址路径是 【网站名称／年／月／日／文章名称】。
比如这种
![](http://upload-images.jianshu.io/upload_images/1110736-6d2261ccbf0dd485.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
这种链接对搜索爬虫是很不友好的，第一它的url结构超过了三层，太深了。第二使用了中文路径。这样会导致一个问题，在某些聊天工具或分享链接的时候会造成url转码变成很长一串难以读写的链接。而且如果你的页面之前被收录或被转载后，当你再次编辑过后可能会造成之前的url失效带来不必要的404 比如下面这样。

![](http://upload-images.jianshu.io/upload_images/1110736-68fc0b4b417f94ac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

别担心，我们有好办法来解决它。

第一种方式：
打开_config.yml配置文件，找到permalink
```
#permalink: :year/:month/:day/:title/ 注释掉
# 改为下面这样
permalink:   /articles/:title.html
```
上面这种方式是去掉了年月日，保持网站最多三层。

第二种方式：(推荐)

 安装  hexo-abbrlink

```javascript
    npm install hexo-abbrlink --save
```

配置_config.yml
```javascript
# permalink: :title/  将之前的注释掉
permalink: archives/:abbrlink.html
abbrlink:
  alg: crc32  # 算法：crc16(default) and crc32
  rep: hex    # 进制：dec(default) and hex
```

ok，到这里你只需要
`hexo g`
`hexo d`
就可以发布了。
而后你的链接就会变成这样的。


![](http://upload-images.jianshu.io/upload_images/1110736-304fbe58dcced8dd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

超赞的有没有？

### nofollow
>nofollow 是HTML页面中a标签的属性值。这个标签的意义是告诉搜索引擎"不要追踪此网页上的链接或不要追踪此特定链接"

简单来说，就是为了防止搜索引擎的爬虫爬去页面的时候给爬走了到别的页面后就不回来了。所以针对链接标签我们可以添加nofollow属性。

例如这样的：
```xml
      <a  rel="external nofollow" >
```
这需要在主题里的html模版中找。具体有哪些自己找找吧。（我也是搜的 :p）

### 页面关键字优化

#### title

文件路径是your-hexo-site\themes\next\layout\index.swig,打开编辑：

原文件
```javascript
{% block title %}{{ config.title }}{% if theme.index_with_subtitle and config.subtitle %} - {{config.subtitle }}
```

修改后的
```javascript
{% block title %}{{ config.title }}{% if theme.index_with_subtitle and config.subtitle %} - {{config.subtitle }}{% endif %}{{ theme.description }} {% endblock %}
```
其实你也可以添加你自己想要的关键字。只要不太过分就行啦。

#### keywords

>Keywords又叫关键词、[关键字](https://baike.baidu.com/item/%E5%85%B3%E9%94%AE%E5%AD%97)。keywords密度是指keywords出现的次数和keywords关联字词出现的次数与整个网页文本之比，keywords在文本中出现的次数越多，那么keywords密度越高,反之就越低。主题，代表的是当前页面或者栏目内容的主体。


keywords在你_config.yml配置文件中就有。注意的是除了根目录上的要修改以外还有主题里的。否则就会出现默认的keywords。

比如简书的keywords是这样的：
```xml
<meta name="keywords"  content="简书,简书官网,图文编辑软件,简书下载,图文创作,创作软件,原创社区,小说,散文,写作,阅读">
```
#### description
description就是这个页面的描述，你想写啥就写啥。比如

-  简书是一个优质的创作社区，在这里，你可以任性地创作，一篇短文、一张照片、一首诗、一幅。。。
- XXX的博客
-  关注最前沿的移动端高新技术
-  **我是一只香脆的大鸡排，你想要咬一口吗？**

---

好了，今天就到这里，我已经把我知道的都告诉大家啦。SEO我不是专家。只是零零碎碎的折腾了一下。这样配置下来一般搜索属于你的关键字还是会出来排在前几的。当然我的博客在百度搜不到的，因为**大鸡排**已经被广告商承包了，听起来好像挺打脸的，写一篇SEO的文章，居然自己不被收录。。。。

最后欢迎大家去我的博客做客，欢迎互换友情链接啥的，给我留言噢。
大鸡排 [www.dajipai.cc](http://www.dajipai.cc)
