package com.example.administrator.cardemo1;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Entity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class PhpConnect extends Thread {
    public Handler handler;
    String result1;
    String target;
    String car;
    public PhpConnect(Handler handler,String target,String car){
        this.car=car;
        this.handler=handler;
        this.target=target;
    }
    @Override
    public  void  run(){
        while (true){
            HttpClient httpClient= new DefaultHttpClient();
            HttpPost httpRequest=new HttpPost(target);//传target值
            List<NameValuePair> params=new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name",car));
            try {
                httpRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
                HttpResponse httpResponse=httpClient.execute(httpRequest);
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    result1="";
                    result1+= EntityUtils.toString(httpResponse.getEntity());
                    Message m=handler.obtainMessage();
                    m.what=0x11;
                    m.obj=result1;
                    Log.e("aabbcc",result1);
                    handler.sendMessage(m);
                    Log.d("tag","444");
                    }else {
                    result1="请求失败";

                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
