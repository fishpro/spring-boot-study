上传文件是我们日常使用最为广泛的功能之一，比如App端上传头像。本章演示如何从客户端上传到 Spring Boot 开发的 Api中。

[本项目源码 github 下载 ](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-upload)

# 1 新建 Spring Boot Maven 示例工程项目

注意：本示例是用 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=upload
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-upload`.

文件上传不需要引入第三方组件。

# 2 依赖引入 Pom.xml
为了演示代码，这里引入 thymeleaf
```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```
# 3 编写上传示例
本章代码主要演示单文件上传和多文件上传，前端采用 `thymeleaf` 模板，实际上就是一个html文件。文件清单包括
1. uploadfile.html 前端文件
2. FileController.java 控制层文件
3. FileUtil.java 文件常用类

## 3.1 控制层代码
主要使用 `MultipartFile` 来实现，如下代码 /upload 和 /uploads 分别为单文件上传和多文件上传。其中 @Value("${fishpro.uploadPath}") 是配置文件中设置的。

```
server.port=8086
fishpro.uploadPath=/Users/jiaojunkang/Desktop/upload/
```

```java
@Controller
public class FileController {

    @Value("${fishpro.uploadPath}")
    private String uploadPath;

    @GetMapping("/uploadfile")
    public String uploadfile(){
        return "uploadfile";
    }


    /**
     * 单文件
     * */
    @PostMapping("/upload")
    @ResponseBody
    Object upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Map<String,Object> map=new HashMap();
        map.put("status",0);
        String fileName = file.getOriginalFilename();
        fileName = UUID.randomUUID().toString(); //对文件名称重命名

        try {
            FileUtil.uploadFile(file.getBytes(), uploadPath, fileName);
            map.put("filename",fileName);
        } catch (Exception e) {
           map.put("status",-1);
           map.put("message",e.getMessage());

        }


        return map;
    }

    /**
     * 多文件
     * */
    @PostMapping("/uploads")
    @ResponseBody
    Object uploads(@RequestParam("files") MultipartFile [] files, HttpServletRequest request) {
        Map<String,Object> map=new HashMap();
        map.put("status",0);
        List<String> filenames=new ArrayList<>();
        for (MultipartFile file: files
             ) {
            String ext = file.getOriginalFilename().split("\\.")[1];
            String fileName = file.getOriginalFilename();
            //fileName = UUID.randomUUID().toString()+"."+ext; //对文件名称重命名

            try {
                FileUtil.uploadFile(file.getBytes(), uploadPath, fileName);
                filenames.add(fileName);
            } catch (Exception e) {
                map.put("status",-1);
                map.put("message",e.getMessage());
                return  map;

            }
        }

        map.put("filename",filenames);
        return map;
    }
}
```

## 3.2 前端文件
前端文件这里演示的比较简单，可以有多中形态，这里使用 form 来提交。

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<hr/>
<div>单位文件</div>
<form enctype="multipart/form-data" method="post" action="/upload">
    文件 <input type="file" name="file"/>
    <input type="submit" value="上传"/>
</form>
<hr/>
<div>多文件</div>

<form enctype="multipart/form-data" method="post" action="/uploads">
    <p>文件1<input type="file" name="files"/></p>
    <p>文件2<input type="file" name="files"/></p>
    <p>文件3<input type="file" name="files"/></p>
    <p><input type="submit" value="上传"/></p>
</form>
</body>
</html>
```

## 3.3 文件保存类
```java
public class FileUtil {

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String renameToUUID(String fileName) {
        return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}

```


[本项目源码 github 下载 ](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-upload)