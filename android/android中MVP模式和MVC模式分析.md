###先说mvc模式:
>　　1) 视图层（View）：一般采用XML文件进行界面的描述，使用的时候可以非常方便的引入。当然，如何你对Android了解的比较的多了话，就一定可以想到在Android中也可以使用JavaScript+HTML等的方式作为View层，当然这里需要进行Java和JavaScript之间的通信，幸运的是，Android提供了它们之间非常方便的通信实现。
　　2) 控制层（Controller）：Android的控制层的重任通常落在了众多的Acitvity的肩上，这句话也就暗含了不要在Acitivity中写代码，要通过Activity交割Model业务逻辑层处理，这样做的另外一个原因是Android中的Acitivity的响应时间是5s，如果耗时的操作放在这里，程序就很容易被回收掉。
　　3) 模型层（Model）：对数据库的操作、对网络等的操作都应该在Model里面处理，当然对业务计算等操作也是必须放在的该层的。就是应用程序中二进制的数据。

######这里是以前的设计将 xml布局当做view层,activity当做控制层.复杂操作当做模型层. 通过activity实现模型层的接口.然后操作数据通过模型层接口回调的方式通知activity并响应获取到的数据再填充到view上.****这样既可避免activity逻辑过复杂,所有事实都交给model来做,然后回调响应.

---------------------------
###我们再来看MVP模式:
参考:[android中MVP模式（一）](http://blog.csdn.net/knxw0001/article/details/39637273)       [在Android开发中使用MVP模式](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0202/2397.html)
>(1)View:负责绘制UI元素、与用户进行交互(在Android中体现为Activity);
(2)View interface:需要View实现的接口，View通过View interface与Presenter进行交互，降低耦合，方便进行单元测试;
(3)Model:负责存储、检索、操纵数据(有时也实现一个Model interface用来降低耦合);
(4)Presenter:作为View与Model交互的中间纽带，处理与用户交互的负责逻辑。
简单的就是最好的,简单来说mvp模式就是将mvc更佳解耦,从activity中再抽出一个presenter层来做纽带,同时抽出两个接口层一个负责把数据返回给view,一个负责通知model来配合使用.具体代码的实现可参考[android中MVP模式（一）](http://blog.csdn.net/knxw0001/article/details/39637273)    这篇文章.