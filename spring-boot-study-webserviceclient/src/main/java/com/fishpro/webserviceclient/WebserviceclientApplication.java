package com.fishpro.webserviceclient;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebserviceclientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebserviceclientApplication.class, args);

        JaxWsDynamicClientFactory dcflient=JaxWsDynamicClientFactory.newInstance();

        Client client=dcflient.createClient("http://localhost:8080/ws/user?wsdl");
        try{
            Object[] objects=client.invoke("getUserById","1");
            System.out.println("getUserById 调用结果："+objects[0].toString());

            Object[] objectall=client.invoke("getUsers");
            System.out.println("getUsers调用部分结果："+objectall[0].toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
