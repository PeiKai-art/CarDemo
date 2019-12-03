package com.example.administrator.cardemo1;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;
/**定义血量
 * 为100
 *
 *
 * */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private Button btconnect;//蓝牙连接按钮
    private Button OpenLed,CloseLed;
    private Button up,down,left,right;//方向键
    private Button SaoDi,Stop_SaoDi,ZuoYi,YouYi,ZhuaQu,FuWei;//定义小车的功能按钮
    // 选车
    public   String carmyself;
    private  String carenmy;
    private Spinner spinner;
    // 蓝牙部分
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();// 获取本地蓝牙适配器;
    private final static String uuid = "00001101-0000-1000-8000-00805F9B34FB";
    private InputStream inputStream;// 输入流，用来接受蓝牙发送过来的数据
    BluetoothDevice bluetoothDevice = null;// 蓝牙设备
    BluetoothSocket bluetoothSocket = null;// 蓝牙通信的socket
    boolean bluetooth = false;// 设置蓝牙初始状态为false
    private final static int RESULT_CONNECT_DEVICE = 1;
    //后台部分
    private  PhpConnect phpConnect;//php连接线程
    private  String url="http://192.168.191.1:8080/tunk/function1.php";//后台控制小车的网址；
    private EditText nickname;//EDT输入的信息
    private Button PhpConect1;//后台连接按钮；
    public String phpResult="100";//
    boolean bluetoothThread = false;//蓝牙线程返回结果
    boolean phpThread=false;//php线程返回结果
    private Thread thread1;
    private BPhpconnect bPhpconnect;
    private  String smsg = "";    //显示用数据缓存
    private String mm;//发送指令



    //创建消息处理机制
    private Handler handler=new Handler(){
        public  void  handleMessage(Message msg){
            switch (msg.what){
                case 0x11:
                    if (bluetooth==true){
                        if (phpThread==true){
                            PhpConect1=findViewById(R.id.houtai);
                            PhpConect1.setText("成功");
                            mm=msg.obj.toString();
                            send();
                        }
                    }
                    break;
                case 0x12:
                    //写震动代码
                    break;
                    default:
                        break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_main);
        btconnect=findViewById(R.id.bt);//找到蓝牙id
        PhpConect1=findViewById(R.id.houtai);//找到后台按钮的id
        bluetoothAdapter.enable();//弹出打开蓝牙权限
        nickname=findViewById(R.id.address);//关联输入后台的资源
        //找到方向键资源；
        up=findViewById(R.id.up);
        down=findViewById(R.id.down);
        left=findViewById(R.id.left);
        right=findViewById(R.id.right);
        spinner=findViewById(R.id.sp1);
        OpenLed=findViewById(R.id.OpenLed);
        CloseLed=findViewById(R.id.CloseLed);
        SaoDi=findViewById(R.id.SaoDi);
        Stop_SaoDi=findViewById(R.id.Stop_SaoSi);
        ZuoYi=findViewById(R.id.ZuoYi);
        YouYi=findViewById(R.id.YouYi);
        ZhuaQu=findViewById(R.id.ZhuaQu);
        FuWei=findViewById(R.id.FuWei);
        //连接蓝牙和后台
        btconnect.setOnClickListener(this);
        PhpConect1.setOnClickListener(this);
        up.setOnTouchListener(this);
        down.setOnTouchListener(this);
        left.setOnTouchListener(this);
        right.setOnTouchListener(this);
        OpenLed.setOnTouchListener(this);
        CloseLed.setOnTouchListener(this);
        //对下拉菜单设置监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //选中
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // 第一个是数组资源，第二个是view，第三个是位置，第四个是id
                String [] languages=getResources().getStringArray(R.array.cars);//获取String类型的数组
                //显示小车位置
                Toast.makeText(getApplicationContext(),"点击的是"+languages[position],Toast.LENGTH_SHORT).show();
                carmyself=languages[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt:
                if (bluetoothSocket==null){
                    Intent intent=new Intent(this,BTconnect.class);
                    startActivityForResult(intent,RESULT_CONNECT_DEVICE);
                }else{
                    //关闭socket
                    try {
                        inputStream.close();
                        bluetoothSocket.close();
                        bluetoothSocket=null;
                        Button btn=findViewById(R.id.bt);
                        btn.setText("已连接");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return;

            case R.id.houtai:
                if (phpThread==false){
                    if(nickname.getText().toString().equals("")==false) {//如果nickname为空则
                        String str = nickname.getText().toString();
                        String jieguo = str.substring(str.indexOf("tunk"));
                        if (jieguo.equals("tunk/function1.php")==true) {
                            thread1 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String target = nickname.getText().toString().trim();//保存
                                    String car = carmyself;
                                    phpConnect = new PhpConnect(handler, target, car);
                                    phpConnect.start();
                                }
                            });
                            thread1.start();//开启子线程
                            PhpConect1.setText("连接成功");//打印连接成功
                            phpThread = true;
                        }else {
                            Toast.makeText(getApplicationContext(),"网址输入有误",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "未输入网址",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    phpThread=false;
                    PhpConect1=findViewById(R.id.houtai);
                    PhpConect1.setText("后台");
                    Toast.makeText(this,"未输入网址23333",Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                    break;

        }



    }
    //接收活动结果
    protected  void onActivityResult(int requestCode,int resultCode,Intent data){
    switch (requestCode){
        case  RESULT_CONNECT_DEVICE:
            if(resultCode== Activity.RESULT_OK){
                String address=data.getExtras().getString(BTconnect.DEVICE_ADDRESS);
                bluetoothDevice=bluetoothAdapter.getRemoteDevice(address);
                //得到socket
                try {
                    bluetoothSocket=bluetoothDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(uuid));
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"连接失败",Toast.LENGTH_SHORT).show();
                }
                  Button btn = (Button) findViewById(R.id.bt);
                //建立连接
                try {
                    bluetoothSocket.connect();
                    Toast.makeText(getApplicationContext(),"连接成功",Toast.LENGTH_SHORT).show();
                    btn.setText("断开");
                    bluetooth=true;
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "连接失败",
                            Toast.LENGTH_SHORT).show();
                    try {
                        bluetoothSocket.close();
                        bluetoothSocket=null;

                    } catch (IOException e1) {
                        Toast.makeText(getApplicationContext(), "连接失败",
                                Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                Log.i("bluetooth","已经入");
                //打开数据接收线程
                try {
                    inputStream = bluetoothSocket.getInputStream();// 得到蓝牙输入流
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "接受数据失败",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (bluetoothThread == false) {
                    //bluetoothRead.start();
                    bluetoothThread = true;
                    Log.e("bluetooth", smsg);
                    bluetoothRead.start();//开始数据接收线程
                }
            }
    }
    }
    //蓝牙接收数据线程
    Thread bluetoothRead=new Thread(){
        @Override
        public void run() {
            super.run();
            Log.i("bluetooth","已经入");
            InputStreamReader in=new InputStreamReader(inputStream);//获取读取的内容
            BufferedReader bufferedReader= new BufferedReader(in);//获取输入流的对象
            String inputline;
            try{
                while((inputline = bufferedReader.readLine()) != null){//逐行读取输入流的数据
                     smsg+=inputline+"\n";
                    Log.i("bluetooth",smsg);
                    Message m=handler.obtainMessage();
                    m.obj=smsg;//存放接收来的数据
                    m.what=0x15;//把0x15赋值给m
                    handler.sendMessage(m);
                }
                in.close();//关闭输入流
            }catch (IOException e){
                e.printStackTrace();
            }


        }
    };
    // 蓝牙发送指令
    public void send() {
        try {
            OutputStream os = bluetoothSocket.getOutputStream();
            byte[] send = mm.getBytes();
            os.write(send);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 关闭程序掉用处理部分 退出程序是关闭蓝牙
    public void onDestroy() {
        super.onDestroy();
        if (bluetoothSocket != null) // 关闭连接socket
            try {
                bluetoothSocket.close();// 调用.close方法关闭蓝牙
            } catch (IOException e) {
            }
        bluetoothAdapter.disable(); // 关闭蓝牙服务
    }

/*
*上下左右按键触摸方法
 */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.up:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String string = phpResult;
                        String str = string.trim();
                        int i = Integer.parseInt(str);
                        if (bluetooth == true && i > 0) {
                            mm = "Z";
                            send();
                        } else {
                            Toast.makeText(this, "蓝牙连接失败", Toast.LENGTH_SHORT).show();
                        }
                        phpThread = false;
                        PhpConect1 = findViewById(R.id.houtai);
                        PhpConect1.setText("后台");
                        break;
                    case MotionEvent.ACTION_UP: {
                        if (bluetooth == true) {
                            mm = "F";
                            send();
                        }
                        break;
                    }
                }
                break;
            case R.id.down:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String string = phpResult;
                        String str = string.trim();
                        int i = Integer.parseInt(str);
                        if (bluetooth == true && i > 0) {
                            mm = "B";
                            send();
                        } else {
                            Toast.makeText(this, "连接蓝牙失败", Toast.LENGTH_SHORT).show();
                        }
                        phpThread = false;
                        PhpConect1 = findViewById(R.id.houtai);
                        PhpConect1.setText("后台");
                        break;
                    case MotionEvent.ACTION_UP: {
                        if (bluetooth == true) {
                            mm = "F";
                            send();
                        }
                        break;
                    }
                }
                break;
            case R.id.right:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String string = phpResult;
                        String str = string.trim();//去空格
                        int i = Integer.parseInt(str);
                        if (bluetooth == true && i > 0) {
                            mm = "D";
                            send();
                        } else {
                            Toast.makeText(this, "连接蓝牙失败", Toast.LENGTH_SHORT).show();
                        }
                        phpThread = false;
                        PhpConect1 = findViewById(R.id.houtai);
                        PhpConect1.setText("后台");
                        break;
                    case MotionEvent.ACTION_UP: {
                        if (bluetooth = true) {
                            mm = "F";
                            send();
                        }
                        break;
                    }
                }
                break;
            case R.id.left:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String string = phpResult;
                        String str = string.trim();
                        int i = Integer.parseInt(str);
                        if (bluetooth == true && i > 0) {
                            mm = "C";
                            send();
                        } else {
                            Toast.makeText(this, "连接蓝牙失败", Toast.LENGTH_SHORT).show();
                        }
                        phpThread = false;
                        PhpConect1 = findViewById(R.id.houtai);
                        PhpConect1.setText("后台");
                        break;
                    case MotionEvent.ACTION_UP: {
                        if (bluetooth = true) {
                            mm = "F";
                            send();
                        }
                        break;
                    }

                }
                break;
            case R.id.OpenLed:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String string = phpResult;
                        String str = string.trim();
                        int i = Integer.parseInt(str);
                        if (bluetooth == true && i > 0) {
                            mm = "R";
                            send();
                        } else {
                            Toast.makeText(this, "连接蓝牙失败", Toast.LENGTH_SHORT).show();
                        }
                        phpThread = false;
                        PhpConect1 = findViewById(R.id.houtai);
                        PhpConect1.setText("后台");
                        break;
                    case MotionEvent.ACTION_UP: {
                        if (bluetooth = true) {
                            mm = "R";
                            send();
                        }
                        break;
                    }

                }

                break;
            case R.id.CloseLed:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String string = phpResult;
                        String str = string.trim();
                        int i = Integer.parseInt(str);
                        if (bluetooth == true && i > 0) {
                            mm = "S";
                            send();
                        } else {
                            Toast.makeText(this, "连接蓝牙失败", Toast.LENGTH_SHORT).show();
                        }
                        phpThread = false;
                        PhpConect1 = findViewById(R.id.houtai);
                        PhpConect1.setText("后台");
                        break;
                    case MotionEvent.ACTION_UP: {
                        if (bluetooth = true) {
                            mm = "S";
                            send();
                        }
                        break;
                    }

                }
                break;
            case R.id.SaoDi:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String string = phpResult;
                        String str = string.trim();
                        int i = Integer.parseInt(str);
                        if (bluetooth == true && i > 0) {
                            mm = "I";
                            send();
                        } else {
                            Toast.makeText(this, "连接蓝牙失败", Toast.LENGTH_SHORT).show();
                        }
                        phpThread = false;
                        PhpConect1 = findViewById(R.id.houtai);
                        PhpConect1.setText("后台");
                        break;
                    case MotionEvent.ACTION_UP: {
                        if (bluetooth = true) {
                            mm = "I";
                            send();
                        }
                        break;
                    }

                }
                break;
            case R.id.Stop_SaoSi:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String string = phpResult;
                        String str = string.trim();
                        int i = Integer.parseInt(str);
                        if (bluetooth == true && i > 0) {
                            mm = "V";
                            send();
                        } else {
                            Toast.makeText(this, "连接蓝牙失败", Toast.LENGTH_SHORT).show();
                        }
                        phpThread = false;
                        PhpConect1 = findViewById(R.id.houtai);
                        PhpConect1.setText("后台");
                        break;
                    case MotionEvent.ACTION_UP: {
                        if (bluetooth = true) {
                            mm = "V";
                            send();
                        }
                        break;
                    }

                }
                break;
            case R.id.ZuoYi:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String string = phpResult;
                        String str = string.trim();
                        int i = Integer.parseInt(str);
                        if (bluetooth == true && i > 0) {
                            mm = "W";
                            send();
                        } else {
                            Toast.makeText(this, "连接蓝牙失败", Toast.LENGTH_SHORT).show();
                        }
                        phpThread = false;
                        PhpConect1 = findViewById(R.id.houtai);
                        PhpConect1.setText("后台");
                        break;
                    case MotionEvent.ACTION_UP: {
                        if (bluetooth = true) {
                            mm = "F";
                            send();
                        }
                        break;
                    }

                }
                break;
            case R.id.YouYi:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String string = phpResult;
                        String str = string.trim();
                        int i = Integer.parseInt(str);
                        if (bluetooth == true && i > 0) {
                            mm = "Y";
                            send();
                        } else {
                            Toast.makeText(this, "连接蓝牙失败", Toast.LENGTH_SHORT).show();
                        }
                        phpThread = false;
                        PhpConect1 = findViewById(R.id.houtai);
                        PhpConect1.setText("后台");
                        break;
                    case MotionEvent.ACTION_UP: {
                        if (bluetooth = true) {
                            mm = "F";
                            send();
                        }
                        break;
                    }

                }
                break;
            case R.id.ZhuaQu:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String string = phpResult;
                        String str = string.trim();
                        int i = Integer.parseInt(str);
                        if (bluetooth == true && i > 0) {
                            mm = "N";
                            send();
                        } else {
                            Toast.makeText(this, "连接蓝牙失败", Toast.LENGTH_SHORT).show();
                        }
                        phpThread = false;
                        PhpConect1 = findViewById(R.id.houtai);
                        PhpConect1.setText("后台");
                        break;
                    case MotionEvent.ACTION_UP: {
                        if (bluetooth = true) {
                            mm = "F";
                            send();
                        }
                        break;
                    }

                }
                break;
            case R.id.FuWei:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String string = phpResult;
                        String str = string.trim();
                        int i = Integer.parseInt(str);
                        if (bluetooth == true && i > 0) {
                            mm = "0";
                            send();
                        } else {
                            Toast.makeText(this, "连接蓝牙失败", Toast.LENGTH_SHORT).show();
                        }
                        phpThread = false;
                        PhpConect1 = findViewById(R.id.houtai);
                        PhpConect1.setText("后台");
                        break;
                    case MotionEvent.ACTION_UP: {
                        if (bluetooth = true) {
                            mm = "0";
                            send();
                        }
                        break;
                    }

                }
                break;
                default:
                break;


        }

                return false;
        }

}
