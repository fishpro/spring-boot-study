有的时候，我们的 Spring Boot 应用需要调用第三方接口，这个接口可能是 Http协议、可能是 WebService、可能是 FTP或其他格式，本章讨论 Http 接口的调用。

通常基于 Http/Https 协议的接口请求动作 POST/GET/PUT/DELETE/PATCH 操作。交互的内容可以是文本、Json 或 Xml。

在 Spring Boot 中使用 Apache HttpClient 类库能够方便快捷地解决 Http 调用问题。



[本项目源码 github 下载 ](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-httpclient)



# 1 新建 Spring Boot Maven 示例工程项目
注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=httpclient
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-httpclient`.
   
# 2 引入依赖 Pom
```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient -->
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.5.3</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

# 3 编写 HttpClient 代码示例
## 3.1 传统的 get/post
大多数情况，第三方提供的接口都是基于 GET/POST，而且一般需要设定 url、http head 的值，所以我们下面的代码是针对只有 GET/POST 的接口设定

```java
/**
 * 传统的 http get/post 实现
 * */
public class HttpClientUtils {
    /**
     * http get
     * @param url 可带参数的 url 链接
     * @param heads http 头信息
     * */
    public String get(String url,Map<String,String> heads){
        org.apache.http.client.HttpClient httpClient= HttpClients.createDefault();
        HttpResponse httpResponse = null;
        String result="";
        HttpGet httpGet=new HttpGet(url);
        if(heads!=null){
            Set<String> keySet=heads.keySet();
            for(String s:keySet){
                httpGet.addHeader(s,heads.get(s));
            }
        }

        try{

            httpResponse=httpClient.execute(httpGet);
            HttpEntity httpEntity=httpResponse.getEntity();
            if(httpEntity!=null){
                result= EntityUtils.toString(httpEntity,"utf-8");

            }

        }catch (IOException e){
            e.printStackTrace();

        }
        return  result;
    }

    /**
     * http post
     * */
    public static String post(String url, String data, Map<String, String> heads){

        org.apache.http.client.HttpClient httpClient= HttpClients.createDefault();

        HttpResponse httpResponse = null;
        String result="";


        HttpPost httpPost=new HttpPost(url);
        if(heads!=null){
            Set<String> keySet=heads.keySet();
            for(String s:keySet){
                httpPost.addHeader(s,heads.get(s));
            }
        }

        try{
            StringEntity s=new StringEntity(data,"utf-8");
            httpPost.setEntity(s);
            httpResponse=httpClient.execute(httpPost);
            HttpEntity httpEntity=httpResponse.getEntity();
            if(httpEntity!=null){
                result= EntityUtils.toString(httpEntity,"utf-8");

            }

        }catch (IOException e){
            e.printStackTrace();

        }
        return  result;

    }
}
```

调用也非常简单

```java

```


## 3.2 基于 REST 接口的操作方法
我们可以单独为 REST 风格接口提供方法，因为 HttpClient 为我们单独提供了针对 GET/POST/PUT/DELETE 的方法。

### 3.2.1 GET 方法

1. 首先创建一个http请求 HttpGet httpGet=new HttpGet(url);
2. 自定义一个 Response Handler
3. 执行 httpclient.execute(请求，Handler)
4. 处理返回

```
/**
     * http get
     * @param url 可带参数的 url 链接
     * @param heads http 头信息
     * */
    public  static String get(String url,Map<String,String> heads){
        org.apache.http.client.HttpClient httpClient= HttpClients.createDefault();
        HttpResponse httpResponse = null;
        String result="";
        HttpGet httpGet=new HttpGet(url);
        if(heads!=null){
            Set<String> keySet=heads.keySet();
            for(String s:keySet){
                httpGet.addHeader(s,heads.get(s));
            }
        }

        try{

            httpResponse=httpClient.execute(httpGet);
            HttpEntity httpEntity=httpResponse.getEntity();
            if(httpEntity!=null){
                result= EntityUtils.toString(httpEntity,"utf-8");

            }

        }catch (IOException e){
            e.printStackTrace();

        }
        return  result;
    }
```


### 3.2.2 POST 方法

1. 首先创建一个http请求 HttpPost httpPost = new HttpPost(url);
2. 自定义一个 Response Handler，向POST中添加数据（JSON 信息）和 Header信息
   ```java
    httpPost.setHeader("Accept", "application/json");
    httpPost.setHeader("Content-type", "application/json");
   ```
3. 执行 httpclient.execute(请求，Handler)
4. 处理返回

```java
 /**
     * post 方法
     * @param url post 的 url
     * @param data 数据 application/json 的时候 为json格式
     * @param heads Http Head 参数
     * */
    public  static String post(String url,String data,Map<String,String> heads) throws IOException{
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            if(heads!=null){
                Set<String> keySet=heads.keySet();
                for(String s:keySet){
                    httpPost.addHeader(s,heads.get(s));
                }
            } 
            StringEntity stringEntity = new StringEntity(data);
            httpPost.setEntity(stringEntity);

            System.out.println("Executing request " + httpPost.getRequestLine());

            // Create a custom response handler
            ResponseHandler < String > responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String responseBody = httpclient.execute(httpPost, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            return responseBody;
        }
    }
```


### 3.2.3 PUT 方法

1. 首先创建一个http请求 HttpPut httpPut = new HttpPut(url);
2. 自定义一个 Response Handler，向POST中添加数据（JSON 信息）和 Header信息
   ```java
    httpPut.setHeader("Accept", "application/json");
    httpPut.setHeader("Content-type", "application/json");
   ```
3. 执行 httpclient.execute(请求，Handler)
4. 处理返回

```java
/**
     * put 方法
     * @param url put 的 url
     * @param data 数据 application/json 的时候 为json格式
     * @param heads Http Head 参数
     * */
    public  static String put(String url,String data,Map<String,String> heads) throws IOException{
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPut httpPut = new HttpPut(url);
            if(heads!=null){
                Set<String> keySet=heads.keySet();
                for(String s:keySet){
                    httpPut.addHeader(s,heads.get(s));
                }
            } 
            StringEntity stringEntity = new StringEntity(data);
            httpPut.setEntity(stringEntity);

            System.out.println("Executing request " + httpPut.getRequestLine());

            // Create a custom response handler
            ResponseHandler < String > responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            };
            String responseBody = httpclient.execute(httpPut, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            return  responseBody;
        }
    }

```


### 3.2.4 DELETE 方法

1. 首先创建一个http请求  HttpDelete httpDelete = new HttpDelete(url);
2. 自定义一个 Response Handler
3. 执行 httpclient.execute(请求，Handler)
4. 处理返回

```java
/**
     * delete 方法
     * @param url delete 的 url
     * @param heads Http Head 参数
     * */
    public static String delete(String url,Map<String,String> heads) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            HttpDelete httpDelete = new HttpDelete(url);
            if(heads!=null){
                Set<String> keySet=heads.keySet();
                for(String s:keySet){
                    httpDelete.addHeader(s,heads.get(s));
                }
            }
            System.out.println("Executing request " + httpDelete.getRequestLine());

            // Create a custom response handler
            ResponseHandler < String > responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            };
            String responseBody = httpclient.execute(httpDelete, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            return responseBody;
        }
    }
```



[本项目源码 github 下载 ](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-httpclient)

----
参考链接
https://www.javaguides.net/2018/10/apache-httpclient-get-post-put-and-delete-methods-example.html