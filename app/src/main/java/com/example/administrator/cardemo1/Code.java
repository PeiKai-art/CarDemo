package com.example.administrator.cardemo1;

import android.os.Handler;
import android.os.Message;

public class Code  extends  Thread {
    Handler handler;

    public Code(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {//判断线程是否中断
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message m = handler.obtainMessage();
            m.what = 0x13;
            handler.sendMessage(m);//发送信息

        }
    }
}
