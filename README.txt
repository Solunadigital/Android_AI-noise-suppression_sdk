1.将audio文件夹拷贝到 手机/sdcard 下；
2.将res文件夹拷贝到 手机/sdcard 下；
3.audio/before.pcm 为测试音频，可以替换成自己的，文件格式必须为pcm或raw,音频文件为16k采样率；相应的需要修改
./app/src/main/java/com/twirling/sdk/sdk_test/MainActivity.java 文件；
4.例子程序需要文件读写权限和网络权限；
5.项目试用请前往官网(http://yun.twirlingvr.com/index.php/home/sdkdownload/addTestPro.html)创建AI降噪SDK试用项目,并且在MainActivity.java修改成为自己对应的appid和appsecret
6.配置完成后，点击按钮进行AI降噪，生成的降噪后文件为：手机/sdcard/audio/after.pcm，生成文件名根据需求自行修改。