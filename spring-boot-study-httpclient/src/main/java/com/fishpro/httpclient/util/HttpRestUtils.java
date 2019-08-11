package com.fishpro.httpclient.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class HttpRestUtils {

    /**
     * get 方法
     * @param url 可带参数的 url
     * @param heads Http Head 参数
     * */
    public static String get(String url,Map<String,String> heads) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()){
            //定义一个 httpGet
            HttpGet httpget = new HttpGet(url);
            if(heads!=null){
                Set<String> keySet=heads.keySet();
                for(String s:keySet){
                    httpget.addHeader(s,heads.get(s));
                }
            }
            System.out.println("Executing request " + httpget.getRequestLine());
            //ResponseHandler 事件
            ResponseHandler< String > responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            return responseBody;
        }

    }


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
            //json 示例
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json");
//            String json = "{\r\n" +
//                    "  \"firstName\": \"Ram\",\r\n" +
//                    "  \"lastName\": \"Jadhav\",\r\n" +
//                    "  \"emailId\": \"ramesh1234@gmail.com\",\r\n" +
//                    "  \"createdAt\": \"2018-09-11T11:19:56.000+0000\",\r\n" +
//                    "  \"createdBy\": \"Ramesh\",\r\n" +
//                    "  \"updatedAt\": \"2018-09-11T11:26:31.000+0000\",\r\n" +
//                    "  \"updatedby\": \"Ramesh\"\r\n" +
//                    "}";
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
            //json 示例
//            httpPut.setHeader("Accept", "application/json");
//            httpPut.setHeader("Content-type", "application/json");
//            String json = "{\r\n" + "  \"firstName\": \"Ram\",\r\n" + "  \"lastName\": \"Jadhav\",\r\n" +
//                    "  \"emailId\": \"ramesh1234@gmail.com\",\r\n" +
//                    "  \"createdAt\": \"2018-09-11T11:19:56.000+0000\",\r\n" + "  \"createdBy\": \"Ramesh\",\r\n" +
//                    "  \"updatedAt\": \"2018-09-11T11:26:31.000+0000\",\r\n" + "  \"updatedby\": \"Ramesh\"\r\n" +
//                    "}";
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
}
