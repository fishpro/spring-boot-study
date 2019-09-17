当我们在服务器部署Java程序，特别是使用了 `Spring Boot` 生成单一 Jar 文件部署的时候，单一文件为我们开发单来的极大的便利性，保障程序的完整性。但同时对我们修改程序中的任何一处都带来重启服务的麻烦。如何解决这个问题呢？

# 1 问题分析

为了能够解决这个问题，我们来分析下，为什么要重启服务，因为 Jar 中的内容发生了改变，大部分应用程式都加载了内存中，需要重新启动服务才能使用新的内容生效。实际上就是修改前访问的老版本的，修改后访问了新版本。我们使用 nginx 能够很好的解决这个问题。

# 2 nginx 解决方案

在 nginx 中使用 upstream 负载均衡机制实现热部署，也叫灰度发布，即在不停止用户访问的前提下进行系统更新。

## 2.1 nginx upstream 分发策略
1. 轮询
2. 权重
3. ip哈希算法
   
## 2.2 nginx 配置负载均衡和分发
### 在服务器建立两个一模一样的站点分为为 

- 127.0.0.1:8085
- 127.0.0.1:8086
  
### 配置 nginx.conf 文件
注意 hotstart.fishpro.com.cn 是我配置的，你可以配置为你自己的域名
```vm
upstream hotstart{
        server 127.0.0.1:8085;
        server 127.0.0.1:8086;
    }
    server{
        listen 80;
        server_name hotstart.fishpro.com.cn;
        location / {
            proxy_pass http://hotstart;
            index index.html index.htm;
        }
    }
```

### 执行 nignx 更新命令

```cmd
>ningx -s reload
```

## 编写测试用代码

[测试用代码 github 下载 ](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-hotstart)

执行

```cmd
java -Xms256m -Xmx512m  -jar hotstart-0.0.1-SNAPSHOT.jar  --server.port=8085
```

```cmd
java -Xms256m -Xmx512m  -jar hotstart-0.0.1-SNAPSHOT.jar  --server.port=8086
```

### 访问站点 hotstart.fishpro.com.cn

查看结果后，我们关闭当前查看结果的站点 再次刷新,我们可以看出当一个站点停止服务，nginx 则访问另一个站点，这样我们就可以逐个站点来实现不需要中断用户访问来更新服务。

# 3 其他优化点

## 3.1 利用 nginx 把静态文件独立 jar 文件之外
我们知道静态文件使用 nginx 直接提供服务则更快更稳定，所以我们构件站点的时候直接把静态文件包括 css、js、image 等放在 jar 文件夹之外使用域名独立访问。也可以理解为 **动静分离**

