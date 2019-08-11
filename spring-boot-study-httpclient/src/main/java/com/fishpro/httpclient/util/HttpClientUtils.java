package com.fishpro.httpclient.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 传统的 http get/post 实现
 * */
public class HttpClientUtils {
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
