上一篇我们通过源码分析了Handler的消息流程原理，如果对`handler`的原理还不够明白的同学可以先学习[上篇](http://www.jianshu.com/p/6f25729ef62a)。我们今天的主角是`HandlerThread`。此乃我android大军一员猛将也。


## 目录
- HandlerThread简单介绍
- 如果没有Handler和HandlerThread以前
- HandlerThread源码分析
- 原生线程间通信

------

## HandlerThread
从名字上来看，这厮肯定和线程有扯不开的关系。只闻起名还未使用过的同学别担心，我们先说说他的做用，再分析源码的实现。

先思考这样一个场景，我们知道在android中主线程中是不能做复杂的耗时操作。然而可不可以有一种机制是主线程通知子线程来做某件事呢？

**注意：** *这里说的是通知子线程来做某件事，不是说在主线程中另起一个子线程来做某件事。两者是有区别的。*

这个还真可以有，并且`HandlerThread`就是做这件事情的。

首先的确`HandlerThread`本身就是一个线程，他的设计可以用来将事件流在不同的线程中进行切换。恰巧Android主线程可以利用它来做一些耗时的操作。

### 如果没有Handler和HandlerThread以前
我们先来看一个没有这种机制以前是怎么处理的。

下面是伪代码：

```
Thread mTh1 =new Thread(()->{
        ·····
    //我做完了，现在想通知mainTh怎么办？？？
   });

Thread mainTh=new Thread(()->{
     //做一些事情
        ·····
   //做完了调用
    mTh1.start();
   });

mainTh.start(); //开始做事

```

这段代码很简单，我们有两个线程，`mainTh`做完某些事情以后将启动子线程`mTh1`来执行。这些都没有问题，但当`mTh1`完成任务以后，它想再次回到主线程中告知`mainTh`怎么办？是不是思路瞬间短路了。
为什么？因为线程总是顺序执行的，而且是并行顺序执行，一旦执行就没有回路。


### 当HandlerThread出现以后

准确来说是当`Handler、Looper、Message、MessageQueue`出现以后，`HandlerThread`基于此。再次提醒读者先弄清楚Handler通信机制才能搞明我们下面要分析的。
还是上面这个功能，我们看伪代码。

```
    protected void onCreate(Bundle savedInstanceState) {
     mHThread = new HandlerThread( "mHThread") ;
      mHThread .start();
      mMainHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
            //这里收到的消息将会在主线程中执行

          }
        };

        mHandler = new Handler(mHThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
            //这里收到的消息将会在mHThread子线程中处理
                        ·····
            //我做完了，现在将消息通知回主线程
             mMainHandler.sendEmptyMessage(2) ;
           }
        };

      //主线程做一些事情
        ········
     //做完了调用mHandler通知子线程mHThread
       mHandler.sendEmptyMessage(1) ;
}

```
没毛病，我们在`Activity`的`onCreate`方法中创建了:
- 两个 `Handler`
- 一个`HandlerThread`

我们前面说过`HandlerThread`其实就是一个线程。其中`mMainHandler`使用了无参构造函数，那么它将获取主线程中的`Looper`(见Handler源码)。mHandler传入了`mHThread.getLooper()  `，即`HandlerThread`中的子线程中的`Looper`。**两个`Handler`分别持有了主线程的`Looper`和子线程的`Looper`。重点就在此处。**

然后呢，主线程使用`mHandler`发送消息给`HandlerThread`的`Looper`进行处理。此时这件事交由子线程来完成。随后子线程做完事情以后将调用`mMainHandler`来通知主线程中的`Looper`完成主线程中该有的操作。至此顺利进行了一把线程之间的通信。

### HandlerThread源码分析

为了不占用篇幅，下面的`HandlerThread`源码是我精简过的，本身源码也不复杂。读者可以自行打开as查看。
```
public class HandlerThread extends Thread {
    Looper mLooper;

    @Override
    public void run() {
        mTid = Process.myTid();
        Looper.prepare();
        synchronized (this) {
            mLooper = Looper.myLooper();//创建Looper
            notifyAll();
        }
        Process.setThreadPriority(mPriority);
        onLooperPrepared();
        Looper.loop();//开启消息轮训
        mTid = -1;
    }
    public Looper getLooper() {  return mLooper;}
    public boolean quit() {    looper.quit();    }
    public boolean quitSafely() {   looper.quitSafely(); }
}
```
我们可以看到`HandlerThread `继承了`Thread `所以本质上它就是一个普通的线程。那么其中`run方法`里创建`Looper`并开启了循环队列。整个源码极其简单。前面我的使用场景中就是通过了`getLooper方法`来获取当前线程中的`Looper`，所以`handler`才能在在线程之间将消息灵活的处理。

## 原生线程间通信
略提一下，抛开`Handler`在原生Java中其实也有办法做线程间通信。只是方法要么不够优雅，要么会造成cpu运算负荷超高或死锁等情况。而且经常因为技术不到位，导致翻车的情况。所以android中的`Hanlder`通信机制非常巧妙的避开这样的问题，提供一下原生方法参考学习。
- 共享内存变量  使用同步锁（容易死锁）
-  共享内存变量 判断变量状态 （极其消耗内存）
- 管道通信

### 题外话

给各位正在进阶的同学们提个醒，现在大部分工作中都在使用优雅的轮子来解决线程切换的问题如`rxJava、rxAndroid`等，确实它们非常好用和方便，但不代表`handler`这种原生的对象就可以摈弃。那些优秀框架的实现都离不android原生的特性，如果要往上进阶学习，就不能只是每天看看这里的大神新出一个轮子，那里大厂又开源一个框架，问其原理一概不知，自己去看他们的源码也是云雾朦胧的。这样以来自己的技术深度很难有较大的进步。而如果是先从基础开始早一点摸清他们的套路，再去看人家的框架是怎么写的。学习那些新东西是很快的。而我自己就曾经犯下这样的错误，走了很多弯路。希望能给各位同学带来帮助。愿君与共勉。


下一篇我们将分析[IntentService的源码](http://www.jianshu.com/p/83d9a3e09f0a)。

---
# 如何下次找到我?
- 关注我的简书
- 本篇同步Github仓库:[https://github.com/BolexLiu/MyNote](https://github.com/BolexLiu/MyNote)  (可以关注)
![](http://upload-images.jianshu.io/upload_images/1110736-f0a700624e0723ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)