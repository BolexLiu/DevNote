
![](http://upload-images.jianshu.io/upload_images/1110736-be60402e5bc0acfa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


adb的全称为Android Debug Bridge.是android司机经常用到的工具.但是问题是那么多命令写代码已经够费劲了,过段时间在次使用时压根记不住呀.本次的大餐就是为此开篇的.这一次我们不记命令.要用随时过来ctrl+F呀.哇哈哈哈!

本篇ADB集锦不管是常用还是冷门的都有.客观您随意看.记不住没关系,收藏了再说呗.

![](http://upload-images.jianshu.io/upload_images/1110736-02938f4ec9439f7e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



#你能在本篇文章中收获什么?
 -  adb基本指令
 - Shell AM&PM
 - adb模拟用户事件
 - logcat日志
 - 常用节点
 - 远程ADB
 - 常用命令集
----


 #  一. 基本指令

 - 进入指定设备   `adb -s serialNumber shell `
 - 查看版本  `adb version `
 - 查看日志  ` adb logcat`
 - 查看设备  ` adb devices `
 - 连接状态  `adb get-state`
 - 启动ADB服务 `adb start-server`
 - 停止ADB服务  `adb kill-server`
 - 电脑推送到手机 `adb push local remote `
 - 手机拉取到电脑 `adb pull remote local`


---

# 二. adb shell下的am 与 pm


注:am和pm命令必须先切换到adb shell模式下才能使用

## am 
am全称activity manager，你能使用am去模拟各种系统的行为，例如去启动一个activity，强制停止进程，发送广播进程，修改设备屏幕属性等等。当你在adb shell命令下执行am命令:
  ```
  am <command>
  ```
- 启动app ` am start -n {packageName}/.{activityName}`
- 杀app的进程 `am kill <packageName> `
- 强制停止一切 `am force-stop <packageName>`
- 启动服务`am startservice `
- 停止服务 `am stopservice`
- 打开简书 ` am start -a android.intent.action.VIEW -d http://www.jianshu.cn/ `
- 拨打10086 `am start -a android.intent.action.CALL -d tel:10086 `

## pm
pm全称package manager，你能使用pm命令去模拟[Android](http://lib.csdn.net/base/android)行为或者查询设备上的应用等，当你在adb shell命令下执行pm命令：
```
pm <command>
```
- 列出手机所有的包名 `pm list packages `
- 安装/卸载 `pm install/uninstall `

---
# 三. 模拟用户事件

 - 文本输入:` adb shell input text <string>`
   例:`手机端输出demo字符串，相应指令：adb shell input "demo".`
 - 键盘事件：` input keyevent <KEYCODE>，其中KEYCODE见本文结尾的附表 `
   例:`点击返回键，相应指令： input keyevent 4.`
 - 点击事件：` input tap <x> <y>`
  例: `点击坐标（500，500），相应指令： input tap 500 500.`
 - 滑动事件： `input swipe <x1> <y1> <x2> <y2> <time>`
例: `从坐标(300，500)滑动到(100，500)，相应指令： input swipe 300 500 100 500.`
例: `200ms时间从坐标(300，500)滑动到(100，500)，相应指令： input swipe 300 500 100 500 200.`

## 循环 shell命令:
  [android adb shell循环模拟点击](http://www.jianshu.com/p/c2120e27ee4c)

---

# 四. logcat日志

- 显示包含的logcat ` logcat \| grep <str>`
- 显示包含，并忽略大小写的logcat `logcat \| grep -i <str>`
- 读完所有log后返回，而不会一直等待 `logcat -d `
- 清空log并退出 `logcat -c `
- 打印最近的count `logcat -t <count>`
- 格式化输出Log，其中format有如下可选值： ` logcat -v <format>`
>  brief — 显示优先级/标记和原始进程的PID (默认格式)
 process — 仅显示进程PID
tag — 仅显示优先级/标记
 thread — 仅显示进程：线程和优先级/标记
 raw — 显示原始的日志信息，没有其他的元数据字段
 time — 显示日期，调用时间，优先级/标记，PID
long —显示所有的元数据字段并且用空行分隔消息内容

---
# 五. 常用节点
>查看节点值，例如：cat /sys/class/leds/lcd-backlight/brightness
修改节点值，例如：echo 128 > sys/class/leds/lcd-backlight/brightness

- LPM: `echo N > /sys/modue/lpm_levels/parameters/sleep_disabled`
- 亮度：` /sys/class/leds/lcd-backlight/brightness`
- CPU: `/sys/devices/system/cpu/cpu0/cpufreq`
- GPU: `/sys/class/ kgsl/kgsl-3d0/gpuclk`
- 限频：` cat /data/pmlist.config`
- 电流： `cat /sys/class/power_supply/battery/current_now`
- 查看Power： `dumpsys power`
- WIFI :`data/misc/wifi/wpa_supplicant.conf`
- 持有wake_lock: `echo a> sys/power/wake_lock`
- 释放wake_lock:` echo a> sys/power/wake_unlock`
- 查看Wakeup_source: `cat sys/kernel/debug/wakeup_sources`
- Display(关闭AD):` mv /data/misc/display/calib.cfg /data/misc/display/calib.cfg.bak 重启`
- 关闭cabc：` echo 0 > /sys/device/virtual/graphics/fb0/cabc_onoff`
- 打开cabc：`echo 3 > /sys/device/virtual/graphics/fb0/cabc_onoff`
- systrace：` sdk/tools/monitor`
- 限频：` echo /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq 1497600`
- 当出现read-only 且 remount命令不管用时：`adb shell mount -o rw,remount /`
- 进入9008模式： `adb reboot edl`
- 查看高通gpio：`sys/class/private/tlmm 或者 sys/private/tlmm`
- 查看gpio占用情况：`sys/kernle/debug/gpio`


---

 # 六. 远程ADB
为避免使用数据线，可通过wifi通信，前提是手机与PC处于同一局域网

 ## 启动方法：
  ```
adb tcpip 5555  //这一步，必须通过数据线把手机与PC连接后再执行  
adb connect <手机IP>
 ```
 ## 停止方法:

```
adb disconnect //断开wifi连接
adb usb //切换到usb模式
```

---

# 七. 常用操作示例

 -  查看当前 `ls`
 -  打印当前路径 `pwd`
 - 查看当前连接的设备 `adb devices`
 - 终止adb服务进程 `adb kill-server`
 - 重启adb服务进程 `adb start-server`
 - PID是:8607  查看某个进程的日志 `adb logcat -v process |grep 8607   `
 - 清理缓存 `logcat -c`
 - 打印xys标签log `adb logcat -s xys`
 - 打印192.168.56.101:5555设备里的xys标签log `adb -s 192.168.56.101:5555 logcat -s xys`
 - 打印在ActivityManager标签里包含start的日志 `adb logcat -s ActivityManager | findstr "START" `
> "-s"选项 : 设置输出日志的标签, 只显示该标签的日志;
"-f"选项 : 将日志输出到文件, 默认输出到标准输出流中, -f 参数执行不成功;
"-r"选项 : 按照每千字节输出日志, 需要 -f 参数, 不过这个命令没有执行成功;
"-n"选项 : 设置日志输出的最大数目, 需要 -r 参数, 这个执行 感觉 跟 adb logcat 效果一样;
"-v"选项 : 设置日志的输出格式, 注意只能设置一项;
"-c"选项 : 清空所有的日志缓存信息;
"-d"选项 : 将缓存的日志输出到屏幕上, 并且不会阻塞;
"-t"选项 : 输出最近的几行日志, 输出完退出, 不阻塞;
"-g"选项 : 查看日志缓冲区信息;
"-b"选项 : 加载一个日志缓冲区, 默认是 main, 下面详解;
"-B"选项 : 以二进制形式输出日志;

- 重启机器 `adb reboot `
- 获取序列号 `adb get-serialno`
- 重启到bootloader，即刷机模式 `adb reboot bootloader`
- 重启到recovery，即恢复模式 `adb reboot recovery `
- 安装APK：`adb install <apkfile> //比如：adb install baidu.apk`
- 安装apk到sd卡： `adb install -s <apkfile> // 比如：adb install -s baidu.apk`
- 卸载APK：`adb uninstall <package> //比如：adb uninstall com.baidu.search`
- 获取机器MAC地址 `adb shell  cat /sys/class/net/wlan0/address`
- 启动应用：`adb shell am start -n <package_name>/.<activity_class_name>    例如:adb shell am start -n yf.o2o.store/yf.o2o.store.activity.LoginActivity`
- 查看占用内存排序  ` adb shell top`
- 查看占用内存前6的app：`adb shell top -m 6`
- 刷新一次内存信息，然后返回：`adb shell top -n 1`
- 查询各进程内存使用情况：`adb shell procrank`
- 杀死一个进程：`adb shell kill [pid] `
- 查看进程列表：`adb shell ps`
- 查看指定进程状态：`adb shell ps -x [PID] `
- 查看后台services信息： `adb shell service list `
- 查看当前内存占用： `adb shell cat /proc/meminfo`
- 查看IO内存分区：`adb shell cat /proc/iomem`
- 将system分区重新挂载为可读写分区：`adb remount`
- 从本地复制文件到设备： `adb push <local> <remote> `
- 从设备复制文件到本地： `adb pull <remote> <local> `
- 列出目录下的文件和文件夹，等同于dos中的dir命令：`adb shell ls`
- 进入文件夹，等同于dos中的cd 命令：`adb shell cd <folder> `
- 重命名文件： `adb shell rename path/oldfilename path/newfilename `
- 删除system/avi.apk： `adb shell rm /system/avi.apk`
- 删除文件夹及其下面所有文件：`adb shell rm -r <folder> `
- 移动文件：`adb shell mv path/file newpath/file`
- 设置文件权限：`adb shell chmod 777 /system/fonts/DroidSansFallback.ttf`
-  新建文件夹：`adb shell mkdir path/foldelname`
- 查看文件内容：`adb shell cat <file> `
- 查看wifi密码：`adb shell cat /data/misc/wifi/*.conf `
- 清除log缓存：`adb logcat -c`
- 查看bug报告：`adb bugreport`
- 获取设备名称：`adb shell cat /system/build.prop`
- 查看ADB帮助：`adb help`
- 跑monkey：
`adb shell monkey -v -p your.package.name 500 ` 
`adb -s 192.168.244.151:5555 shell monkey -v -p com.bolexim 500`


---

# 八.附表

下表中， 箭头左边为keycode值，箭头右边为keycode的含义，部分用中文标注

```
0 –> “KEYCODE_UNKNOWN”
1 –> “KEYCODE_MENU”
2 –> “KEYCODE_SOFT_RIGHT”
3 –> “KEYCODE_HOME” //Home键
4 –> “KEYCODE_BACK” //返回键
5 –> “KEYCODE_CALL” 
6 –> “KEYCODE_ENDCALL” 
7 –> “KEYCODE_0” //数字键0
8 –> “KEYCODE_1” 
9 –> “KEYCODE_2” 
10 –> “KEYCODE_3”
11 –> “KEYCODE_4” 
12 –> “KEYCODE_5” 
13 –> “KEYCODE_6” 
14 –> “KEYCODE_7” 
15 –> “KEYCODE_8” 
16 –> “KEYCODE_9” 
17 –> “KEYCODE_STAR” 
18 –> “KEYCODE_POUND” 
19 –> “KEYCODE_DPAD_UP” 
20 –> “KEYCODE_DPAD_DOWN” 
21 –> “KEYCODE_DPAD_LEFT”
22 –> “KEYCODE_DPAD_RIGHT” 
23 –> “KEYCODE_DPAD_CENTER” 
24 –> “KEYCODE_VOLUME_UP” //音量键+
25 –> “KEYCODE_VOLUME_DOWN” //音量键-
26 –> “KEYCODE_POWER” //Power键
27 –> “KEYCODE_CAMERA” 
28 –> “KEYCODE_CLEAR”
29 –> “KEYCODE_A” //字母键A
30 –> “KEYCODE_B” 
31 –> “KEYCODE_C” 
32 –> “KEYCODE_D” 
33 –> “KEYCODE_E” 
34 –> “KEYCODE_F” 
35 –> “KEYCODE_G”
36 –> “KEYCODE_H”
37 –> “KEYCODE_I”
38 –> “KEYCODE_J” 
39 –> “KEYCODE_K” 
40 –> “KEYCODE_L” 
41 –> “KEYCODE_M”
42 –> “KEYCODE_N” 
43 –> “KEYCODE_O” 
44 –> “KEYCODE_P” 
45 –> “KEYCODE_Q” 
46 –> “KEYCODE_R”
47 –> “KEYCODE_S”
48 –> “KEYCODE_T” 
49 –> “KEYCODE_U” 
50 –> “KEYCODE_V” 
51 –> “KEYCODE_W” 
52 –> “KEYCODE_X”
53 –> “KEYCODE_Y” 
54 –> “KEYCODE_Z”
55 –> “KEYCODE_COMMA” 
56 –> “KEYCODE_PERIOD”
57 –> “KEYCODE_ALT_LEFT” 
58 –> “KEYCODE_ALT_RIGHT” 
59 –> “KEYCODE_SHIFT_LEFT” 
60 –> “KEYCODE_SHIFT_RIGHT”
61 -> “KEYCODE_TAB” 
62 –> “KEYCODE_SPACE” 
63 –> “KEYCODE_SYM” 
64 –> “KEYCODE_EXPLORER” 
65 –> “KEYCODE_ENVELOPE” 
66 –> “KEYCODE_ENTER” //回车键
67 –> “KEYCODE_DEL” 
68 –> “KEYCODE_GRAVE” 
69 –> “KEYCODE_MINUS” 
70 –> “KEYCODE_EQUALS” 
71 –> “KEYCODE_LEFT_BRACKET” 
72 –> “KEYCODE_RIGHT_BRACKET” 
73 –> “KEYCODE_BACKSLASH” 
74 –> “KEYCODE_SEMICOLON” 
75 –> “KEYCODE_APOSTROPHE”
76 –> “KEYCODE_SLASH” 
77 –> “KEYCODE_AT” 
78 –> “KEYCODE_NUM” 
79 –> “KEYCODE_HEADSETHOOK” 
80 –> “KEYCODE_FOCUS”
81 –> “KEYCODE_PLUS”
82 –> “KEYCODE_MENU”
83 –> “KEYCODE_NOTIFICATION”
84 –> “KEYCODE_SEARCH”
```
---

本篇有部分内容参考了JackPeng的笔记,感谢.
# 如何下次找到我?
- 关注我的简书
- 本篇同步Github仓库:[https://github.com/BolexLiu/MyNote](https://github.com/BolexLiu/MyNote)  (欢迎star)
- 加入大鸡排QQ群一起撸码成长:110801914
![](http://upload-images.jianshu.io/upload_images/1110736-f0a700624e0723ae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
