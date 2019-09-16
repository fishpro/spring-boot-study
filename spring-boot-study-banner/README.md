我们在启动 SpringBoot 时，控制台会打印 SpringBoot Logo 以及版本信息。有的时候我们需要自己弄个有个性的文本图片。Spring Boot 为我们提供了自定义接口。
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.8.RELEASE)
```
实际上自定义 banner 非常简单，只要在 resource （src/main/resources）目录下新建 banner.txt 把我们自定义的内容写到这个 banner.txt 中就可以了,具体见下面的代码演示。


[本项目源码 github 下载 ](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-banner)


# 1 新建 Spring Boot Maven 示例工程项目

注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=banner
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-banner`.

# 2 编写自定义 Banner
## 2.1 首先创建一个 Banner.txt 
首先创建一个 Banner.txt，哪里可以去自动搞个呢，请自行百度或谷歌。
```
 ____  __  ____  _  _  ____  ____   __  
(  __)(  )/ ___)/ )( \(  _ \(  _ \ /  \ 
 ) _)  )( \___ \) __ ( ) __/ )   /(  O )
(__)  (__)(____/\_)(_/(__)  (__\_) \__/ 

```

下面给出一些网上收集的工具
- http://patorjk.com/software/taag
- http://www.network-science.de/ascii/
- http://www.degraeve.com/img2txt.php

给终端文字加点颜色和特效
- https://mozillazg.com/2013/08/ansi-escape-sequences.html

## 2.2 自定义颜色版本等信息
在 banner.txt 中设置
```
 ____  __  ____  _  _  ____  ____   __
(  __)(  )/ ___)/ )( \(  _ \(  _ \ /  \
 ) _)  )( \___ \) __ ( ) __/ )   /(  O )
(__)  (__)(____/\_)(_/(__)  (__\_) \__/

${AnsiColor.BRIGHT_RED}
Application Version: ${application.version}${application.formatted-version}
Spring Boot Version: ${spring-boot.version}${spring-boot.formatted-version}
```
参数说明：
- ${AnsiColor.BRIGHT_RED}：设置控制台中输出内容的颜色，可以自定义，具体参考org.springframework.boot.ansi.AnsiColor
- ${application.version}：用来获取MANIFEST.MF文件中的版本号，这就是为什么要在Application.java中指定 SpringVersion.class
- {application.formatted-version}：格式化后的{application.version}版本信息
- ${spring-boot.version}：Spring Boot的版本号
- {spring-boot.formatted-version}：格式化后的{spring-boot.version}版本信息

## 2.3 测试
再次运行程序，那么控制台就打印从了我们自定义的图片



