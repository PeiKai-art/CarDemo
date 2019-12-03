package com.example.administrator.cardemo1;

import android.os.Message;
import android.os.Handler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class BPhpconnect extends Thread {
    private Handler handler;
    private String blood;
    private String url;
    public BPhpconnect(String blood,String url,Handler handler){
        this.blood=blood;
        this.url=url;
        this.handler=handler;
    }
    @Override
    public void run(){
        System.out.println("你好");
        String result=null;
        boolean isSendSucceed=false;
        HttpClient httpClient=new DefaultHttpClient();
        HttpPost httpRequest=new HttpPost(url);
        List<NameValuePair>parmas=new ArrayList<NameValuePair>();
        parmas.add(new BasicNameValuePair("blood", blood));
        System.out.println("---------血量"+blood);
        try {
            httpRequest.setEntity(new UrlEncodedFormEntity(parmas, HTTP.UTF_8));
            HttpResponse httpResponse=httpClient.execute(httpRequest);
            int stadteCode=httpResponse.getStatusLine().getStatusCode();
            if (stadteCode==200){
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);
            }
            if (result.equals(isSendSucceed)){
                isSendSucceed=true;
            }
            Message m=handler.obtainMessage();
            m.what=0x16;
            m.obj=isSendSucceed;
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
