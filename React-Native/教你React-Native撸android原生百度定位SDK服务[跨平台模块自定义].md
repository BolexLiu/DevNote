## 通过此文章你将获得如下升职加薪装逼技能
 - 动手封装native接口提供reactNative调用
 - 集成百度定位sdk 定位服务

----

## 问题?
  哈喽,最近一直在研究reactNative没有碰原生的东西已经很久了.不知道大家是否想过一个问题呢?reactNative开发中用到的语言是Js.但是我们如果想做百度定位或者微博分享等第三方sdk的集成时,会不会碰到以下问题.


 - 第三方sdk是否有reactNative的sdk.
 - reactNative是否支持调用原生sdk

### 第三方sdk是否有reactNative的sdk.
 暂不支持,很抱歉,由于reactNative技术太新,而且更新较快.目前暂未发现第三sdk有做这块的.

![](http://upload-images.jianshu.io/upload_images/1110736-29adb336f5ceeff8.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
 既然他们还没来得及做,是否又有民间艺人做了这事?没错确实有.如果你在github上去搜索确实能找到一些.不过有的时候我们会发现一个问题.他们的项目可能断更了.也就是说sdk更新后,作者并未能及时更新.导致你集成进来后压根就不能用.

![](http://upload-images.jianshu.io/upload_images/1110736-a11c1cc1511f9bea.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


###reactNative是否支持调用原生sdk

 支持,是的官方说了他们支持的.下面请看原话.
![](http://upload-images.jianshu.io/upload_images/1110736-0420bcd99db8bc75.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/800)
  - 你需要一个翻译吗下面就是哦:

  有时候App需要访问平台API，但React Native可能还没有相应的模块包装；或者你需要复用一些Java代码，而不是用Javascript重新实现一遍；又或者你需要实现某些高性能的、多线程的代码，譬如图片处理、数据库、或者各种高级扩展等等。
我们把React Native设计为可以在其基础上编写真正的原生代码，并且可以访问平台所有的能力。这是一个相对高级的特性，我们并不认为它应当在日常开发的过程中经常出现，但具备这样的能力是很重要的。如果React Native还不支持某个你需要的原生特性，你应当可以自己实现该特性的封装。

![](http://upload-images.jianshu.io/upload_images/1110736-4abf462d86c84a05.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
  没有错,官方都说了,它支持的.那还怕个J*,那么我们现在就撸起袖子干他丫的.搞起.

----

## 集成百度定位

这是温柔的一条提示:题纲带星号的是你应该关注的重点

 - 初始化项目
 - 申请认证成为百度开发者
 - 下载基础开发包并导入android Studio
 - 编写Native模块*
 - reactNative调用Native模块*
 - Run起来

###初始化项目

我们打开命令行,输入 react-native init [你的项目名称] 不含括弧,如下图所示

![](http://upload-images.jianshu.io/upload_images/1110736-cf6374c319a000c8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/800)

然后你就会看到下面这个样子的. 一切顺利的话只等他结束.

![](http://upload-images.jianshu.io/upload_images/1110736-23193a93a7f025f0.gif?imageMogr2/auto-orient/strip)

为了验证项目是否正常init,现在你可以使用react-native run-android 运行你的应用.在这之前你要确保adb是否都已经链接好哦.

###申请认证成为百度开发者
 http://lbsyun.baidu.com/ 打开它,然后注册一个账号.简单的认证后找到[API控制台](http://lbsyun.baidu.com/apiconsole/key),然后你会看到如下界面.


![](http://upload-images.jianshu.io/upload_images/1110736-edcb32a6cbf018dc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/800)

那么我们首先来创建一个应用吧.点击创建应用.

![](http://upload-images.jianshu.io/upload_images/1110736-f5201c6cf1837217.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/800)

注意以下几点:
- 应用类型选择android sdk
- SHA1可以参考官方的方法生成. http://lbsyun.baidu.com/index.php?title=androidsdk/guide/key
- 包名不要填错了 包名需要在文件build.gradle中查询 applicationId，并确保 applicationId 与在 AndroidManifest.xml 中定义的包名一致

随后你会获得一个叫ak的key.这个key后面要配置到你的AndroidManifest.xml .后面会讲解到.
你可以点击应用列表中的设置,看到你自己应用的ak

![](http://upload-images.jianshu.io/upload_images/1110736-ddaa39a38e45b2fd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


###下载基础开发包并导入android Studio

我想你正常开发reactnative的时候可能用的是atom编辑器或者webStorm之类的,但是因为我们的sdk基础包是native的,所以如果你写java的时候用那些不支持提示或自动导java包的ide会很蛋疼的.所以在此请你先确保要安装好哦.
 我们先在官方找到自定义下载包地址.http://lbsyun.baidu.com/sdk/download?selected=mapsdk_basicmap,mapsdk_searchfunction,mapsdk_lbscloudsearch,mapsdk_calculationtool,mapsdk_radar 也就是这个链接了.勾选你需要的服务.这里我只选了基础定位.然后点击开发包下载.
![](http://upload-images.jianshu.io/upload_images/1110736-a5b6688d9ce9ccee.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


下载后的解压包里有一个叫libs的文件夹.

![](http://upload-images.jianshu.io/upload_images/1110736-1c77dad9d782c2b8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/800)

现在我们打开androidStudio,找到这个选项.
![](http://upload-images.jianshu.io/upload_images/1110736-1afe4a491ce58fa4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/800)

找到你的项目路径,打开项目下的android目录.

注意是打开android目录.
注意是打开android目录.
注意是打开android目录.
重要的事情说三遍.
![](http://upload-images.jianshu.io/upload_images/1110736-984b21fa76e26639.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/800)

运行起来后,将项目切换成Project模式.(默认是android模式)

![](http://upload-images.jianshu.io/upload_images/1110736-dda65e928fcccb47.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/800)

将刚刚下载的压缩文件中的libs复制到项目中的App文件夹下.

![Paste_Image.png](http://upload-images.jianshu.io/upload_images/1110736-f7c74edeea5c75c5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/800)

打开APP文件夹下的build.Gradle,在加入如下代码.
```
android {
     .... 这是其他的代码,你别动它.这一行不需要,不要复制进来了.
   sourceSets {
        main {
            jniLibs.srcDirs = ['libs']
        }
    }
}
```
打开AndroidManifest文件,在application标签中添加服务和ak秘钥.
```
    <service
            android:name="com.baidu.location.f"
            android:enabled="true"
            android:process=":remote"></service>
        <meta-data
            android:name="com.baidu.lbsapi.API_KEY"
            android:value="GN1lCGAHdh1rrkMeEY4I5Azs" />

```
添加权限
```
   <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
    <!-- 这个权限用于进行网络定位-->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"></uses-permission>
    <!-- 这个权限用于访问GPS定位-->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"></uses-permission>
    <!-- 用于访问wifi网络信息，wifi信息会用于进行网络定位-->
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
    <!-- 获取运营商信息，用于支持提供运营商信息相关的接口-->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
    <!-- 这个权限用于获取wifi的获取权限，wifi信息会用来进行网络定位-->
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"></uses-permission>
    <!-- 用于读取手机当前的状态-->
    <uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission>
    <!-- 写入扩展存储，向扩展卡写入数据，用于写入离线定位数据-->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
    <!-- 访问网络，网络定位需要上网-->
    <!-- SD卡读取权限，用户写入离线定位数据-->
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"></uses-permission>
```
至此恭喜你已经完成一个基础的定位环境了.

![](http://upload-images.jianshu.io/upload_images/1110736-37e7faa575d12875.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/200)


###编写Native模块*

 编写一个Native模块分为三步
- 1.建一个中间访问对象,
- 2.建一个ReactPackage对象.
- 3.将ReactPackage对象加载到MainApplication中.

接下来我们就来一步一步做.

 - 新建一个BaiduLBS类继承ReactContextBaseJavaModule.  声明一个startLocation()的方法.供给RN调用,参数Callback是用于回调js的对象.我们再定位成功后要调用它.注意方法的上面必须加上 @ReactMethod注解.具体的调用定位请参考代码吧.然后重写GetName方法.return的值就是rn中调用的组件.

```
package com.baidumapnr;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.List;

/**
 * author：LiuShenEn on 2017/1/18 14:57
 */
public class BaiduLBS  extends ReactContextBaseJavaModule {
    public LocationClient mLocationClient = null;
    private Callback locationCallback;

    public BaiduLBS(ReactApplicationContext reactContext) {
        super(reactContext);
    }
    @ReactMethod
    public void startLocation( Callback locationCallback) {
       this.locationCallback = locationCallback;
        mLocationClient = new LocationClient(getReactApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        Log.e("tag","ok");
    }

    /**
     * 定位回掉
     */
    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            Log.e("TGA", "回掉+1");
            //Receive Location
            final StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(bdLocation.getTime());
            sb.append("\nerror code : ");
            sb.append(bdLocation.getLocType());
            sb.append("\nlatitude : ");
            sb.append(bdLocation.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(bdLocation.getLongitude());
            sb.append("\nradius : ");
            sb.append(bdLocation.getRadius());
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(bdLocation.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(bdLocation.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(bdLocation.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(bdLocation.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(bdLocation.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(bdLocation.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(bdLocation.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(bdLocation.getLocationDescribe());// 位置语义化信息
            List<Poi> list = bdLocation.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            locationCallback.invoke(sb.toString());
        }
    };

    @Override
    public String getName() {
        return "MyLBS";
    }
}
```

- 新建一个AnExampleReactPackage类实现ReactPackage接口.并在createNativeModules方法中加载BaiduLBS类.

```
package com.baidumapnr;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class AnExampleReactPackage implements ReactPackage {

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    @Override
    public List<NativeModule> createNativeModules(
            ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();

        modules.add(new BaiduLBS(reactContext));

        return modules;
    }
}
```

- 在MainApplication中的getPackages方法中添加AnExampleReactPackage.代码如下

```
package com.baidumapnr;

import android.app.Application;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    protected boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
              new AnExampleReactPackage()
      );
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
  }
}

```

让我们喘一会气歇一下,理一下思路吧.

![](http://upload-images.jianshu.io/upload_images/1110736-d1d6b60c7837eaff.gif?imageMogr2/auto-orient/strip)

至此你已经编写完成所有的native模块.简单来说当MainApplication 实现了ReactApplication 接口以后就会通过getPackages来加载你自定义的模块和原生reactNative的模块.其中MainReactPackage就是原生的模块.那么我们自定位的AnExampleReactPackage中就加载了我们自定义的百度定位sdk.然后rn中才能调用到.


### reactNative调用Native模块*

保存你的代码,现在可以关掉AS了.打开你写JS的IDE吧.让我们愉快的调用刚刚自定义的sdk接口.

- 打开你rn的目录,新建一个BaiduLbs.js文件.然后使用NativeModules对象将刚刚原生中定位的模块加载进来.注意其中 NativeModules.MyLBS中的MyLBS就是我们写原生中BaiduLBS 类的getName的返回值.

```
'use strict';

import { NativeModules } from 'react-native';

export default NativeModules.MyLBS;
```

- 找到index.android.js 文件.import刚刚我们声明的模块. 代码如下:

```

import React, {Component} from 'react';
import {
    AppRegistry,
    StyleSheet,
    Text,
    View
} from 'react-native';
import MyLBS from './BaiduLbs'

export default class BaiduMapNR extends Component {
    constructor(props) {
        super(props);
        this.state = {
            location: null,
        }
    }

    componentDidMount() {
        MyLBS.startLocation((location)=> {
            this.setState({location: location})
        });
    }

    render() {

        if (this.state.location) {
            return <Text >
                { this.state.location}
            </Text>
        } else {
            return <View>
               <Text>你好!</Text>
            </View>
        }

        ;
    }

}
AppRegistry.registerComponent('BaiduMapNR', () => BaiduMapNR);
```

###Run起来

  在你项目的目录下打开命令行.输入
```
react-native run-android .
```
下面就是见证奇迹的时刻啦.有木有小激动呢?

duang!


![](http://upload-images.jianshu.io/upload_images/1110736-1393639d791f2f2e.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![](http://upload-images.jianshu.io/upload_images/1110736-9ec5b050b6168498.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


ok.成功出现我们定位的地址啦.哈哈 是不是很简单.相信你又学会了一招.(⊙v⊙)嗯,加油哦.相信这么爱学习的你,肯定是整天沉迷在其中.那么下面的官方教程你也要抽时间学习哦.

- [原生模块](http://reactnative.cn/docs/0.39/native-modules-android.html#content)
- [原生UI组件](http://reactnative.cn/docs/0.39/native-component-android.html#content)

---

# 感谢&打赏
- 如果本文对您的工作或学习有帮助,您可以打赏支持我.这样我会更有动力.都说2017年是为内容付费的元年.O(∩_∩)O哈哈~
![](http://upload-images.jianshu.io/upload_images/1110736-c57defa4b04682f6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 支付宝
![](http://upload-images.jianshu.io/upload_images/1110736-b0ea390490081c5b.png?imageMogr2/auto-orient/strip%7CimageView2/2/h/300)

## 微信
![](http://upload-images.jianshu.io/upload_images/1110736-a3a7f345e0cc17ad.png?imageMogr2/auto-orient/strip%7CimageView2/2/h/300)