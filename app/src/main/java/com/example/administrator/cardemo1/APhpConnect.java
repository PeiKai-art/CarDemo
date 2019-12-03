package com.example.administrator.cardemo1;

import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class APhpConnect extends Thread{
    public Handler handler;
    String result1;
    String target;
    String car;
    String p;
    public  APhpConnect(Handler handler,String target,String car,String p){
        this.handler = handler;
        this.target = target;
        this.car = car;
        this.p = p;
    }
    public  void run(){
        HttpClient httpClient=new DefaultHttpClient();
        HttpPost   httpRequest =new HttpPost(target);
        List<NameValuePair>parmas=new ArrayList<NameValuePair>();
        if (car.equals("car01")){
            parmas.add(new BasicNameValuePair("car","p"));
            parmas.add(new BasicNameValuePair("car01","0"));
            parmas.add(new BasicNameValuePair("car02","0"));
            parmas.add(new BasicNameValuePair("car03","0"));
            parmas.add(new BasicNameValuePair("car04","0"));
            parmas.add(new BasicNameValuePair("car05","0"));
            parmas.add(new BasicNameValuePair("car06","0"));
            parmas.add(new BasicNameValuePair("car07","0"));
            parmas.add(new BasicNameValuePair("car08","0"));

        }else if (car.equals("car02")){
            parmas.add(new BasicNameValuePair("car","p"));
            parmas.add(new BasicNameValuePair("car01","0"));
            parmas.add(new BasicNameValuePair("car02","0"));
            parmas.add(new BasicNameValuePair("car03","0"));
            parmas.add(new BasicNameValuePair("car04","0"));
            parmas.add(new BasicNameValuePair("car05","0"));
            parmas.add(new BasicNameValuePair("car06","0"));
            parmas.add(new BasicNameValuePair("car07","0"));
            parmas.add(new BasicNameValuePair("car08","0"));
        }else if (car.equals("car03")){
            parmas.add(new BasicNameValuePair("car","p"));
            parmas.add(new BasicNameValuePair("car01","0"));
            parmas.add(new BasicNameValuePair("car02","0"));
            parmas.add(new BasicNameValuePair("car03","0"));
            parmas.add(new BasicNameValuePair("car04","0"));
            parmas.add(new BasicNameValuePair("car05","0"));
            parmas.add(new BasicNameValuePair("car06","0"));
            parmas.add(new BasicNameValuePair("car07","0"));
            parmas.add(new BasicNameValuePair("car08","0"));
        }else if (car.equals("car03")){
            parmas.add(new BasicNameValuePair("car","p"));
            parmas.add(new BasicNameValuePair("car01","0"));
            parmas.add(new BasicNameValuePair("car02","0"));
            parmas.add(new BasicNameValuePair("car03","0"));
            parmas.add(new BasicNameValuePair("car04","0"));
            parmas.add(new BasicNameValuePair("car05","0"));
            parmas.add(new BasicNameValuePair("car06","0"));
            parmas.add(new BasicNameValuePair("car07","0"));
            parmas.add(new BasicNameValuePair("car08","0"));
        }else if (car.equals("car04")){
            parmas.add(new BasicNameValuePair("car","p"));
            parmas.add(new BasicNameValuePair("car01","0"));
            parmas.add(new BasicNameValuePair("car02","0"));
            parmas.add(new BasicNameValuePair("car03","0"));
            parmas.add(new BasicNameValuePair("car04","0"));
            parmas.add(new BasicNameValuePair("car05","0"));
            parmas.add(new BasicNameValuePair("car06","0"));
            parmas.add(new BasicNameValuePair("car07","0"));
            parmas.add(new BasicNameValuePair("car08","0"));
        }else if (car.equals("car05")){
            parmas.add(new BasicNameValuePair("car","p"));
            parmas.add(new BasicNameValuePair("car01","0"));
            parmas.add(new BasicNameValuePair("car02","0"));
            parmas.add(new BasicNameValuePair("car03","0"));
            parmas.add(new BasicNameValuePair("car04","0"));
            parmas.add(new BasicNameValuePair("car05","0"));
            parmas.add(new BasicNameValuePair("car06","0"));
            parmas.add(new BasicNameValuePair("car07","0"));
            parmas.add(new BasicNameValuePair("car08","0"));
        }else if (car.equals("car06")){
            parmas.add(new BasicNameValuePair("car","p"));
            parmas.add(new BasicNameValuePair("car01","0"));
            parmas.add(new BasicNameValuePair("car02","0"));
            parmas.add(new BasicNameValuePair("car03","0"));
            parmas.add(new BasicNameValuePair("car04","0"));
            parmas.add(new BasicNameValuePair("car05","0"));
            parmas.add(new BasicNameValuePair("car06","0"));
            parmas.add(new BasicNameValuePair("car07","0"));
            parmas.add(new BasicNameValuePair("car08","0"));
        }else if (car.equals("car07")){
            parmas.add(new BasicNameValuePair("car","p"));
            parmas.add(new BasicNameValuePair("car01","0"));
            parmas.add(new BasicNameValuePair("car02","0"));
            parmas.add(new BasicNameValuePair("car03","0"));
            parmas.add(new BasicNameValuePair("car04","0"));
            parmas.add(new BasicNameValuePair("car05","0"));
            parmas.add(new BasicNameValuePair("car06","0"));
            parmas.add(new BasicNameValuePair("car07","0"));
            parmas.add(new BasicNameValuePair("car08","0"));
        }else if (car.equals("car08")){
            parmas.add(new BasicNameValuePair("car","p"));
            parmas.add(new BasicNameValuePair("car01","0"));
            parmas.add(new BasicNameValuePair("car02","0"));
            parmas.add(new BasicNameValuePair("car03","0"));
            parmas.add(new BasicNameValuePair("car04","0"));
            parmas.add(new BasicNameValuePair("car05","0"));
            parmas.add(new BasicNameValuePair("car06","0"));
            parmas.add(new BasicNameValuePair("car07","0"));
            parmas.add(new BasicNameValuePair("car08","0"));
        }
        try {
            //第一个参数是list对象，第二个是字符集 一般都为utf-8;
            httpRequest.setEntity(new UrlEncodedFormEntity(parmas,"utf-8"));
            HttpResponse httpResponse=httpClient.execute(httpRequest);//httpClient执行传数据
            if (httpResponse.getStatusLine().getStatusCode()== HttpStatus.SC_OK){//后台是否连接成功
                result1="";
                result1+= EntityUtils.toString(httpResponse.getEntity());//转成字符创
            }else {
                result1="请求失败";
            }
            Message m=handler.obtainMessage();
            m.what=0x12;
            m.obj=result1;
            handler.sendMessage(m);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
