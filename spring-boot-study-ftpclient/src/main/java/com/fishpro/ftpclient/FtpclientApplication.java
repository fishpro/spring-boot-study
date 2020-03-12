package com.fishpro.ftpclient;

import com.fishpro.ftpclient.util.FtpContinueClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FtpclientApplication {

    public static void main(String[] args) {

        SpringApplication.run(FtpclientApplication.class, args);

        /*FtpContinueClient*/
        FtpContinueClient ftp=new FtpContinueClient();
        String filePath="";
        String filename="";
        try{
            ftp.connect("192.168.0.1",21,"administrator","xxdfsdfs^");
            FtpContinueClient.UploadStatus uploadStatus= ftp.upload(filePath,"/"+filename);
            ftp.disconnect();

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {

        }
    }

}
