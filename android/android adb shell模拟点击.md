##模拟点击shell命令
 - 连接adb后输入 adb shell input tap X坐标  Y坐标
>  左边 adb shell input tap 279 1897
 右边 adb shell input tap 1085 1880


```
 @echo off
set var=0
rem ************循环开始了
:continue
set /a var+=1
echo 第%var%次循环
adb shell input tap 279 1897
adb shell input tap 1085 1880
if %var% lss 0 goto continue
rem ************循环结束了
echo 循环执行完毕
pause
```
-------------

- 执行shell以后的命令
adb shell  <123.txt