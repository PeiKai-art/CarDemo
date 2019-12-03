package com.example.administrator.cardemo1;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BTconnect extends Activity {
    //蓝牙部分
    private BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    private ArrayAdapter<String> oldBlueToothAdapter;//保存新蓝牙
    private ArrayAdapter<String>newBlueToothAdapter;//保存旧蓝牙
 //   private ArrayList<String> btlist = new ArrayList<String>();//存放listview
    public  static  String DEVICE_ADDRESS="设备地址";
    //布局界面的按钮
    private Button search,cancel;
    private ListView newBluetooth,oldBluetooth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btconnect);
        //关联资源
        search=findViewById(R.id.search);
        cancel=findViewById(R.id.cancle);

        newBluetooth=findViewById(R.id.list1);//未配对设备
        oldBluetooth=findViewById(R.id.list2);//已配对设备
        //按钮设置监听 搜索
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            /*
             *搜索附近的设备
             */
            public void onClick(View v) {
              search();
            }
        });
        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

       //初始化adapter
        newBlueToothAdapter=new ArrayAdapter<String>(this,R.layout.list_item );
        oldBlueToothAdapter=new ArrayAdapter<String>(this,R.layout.list_item);
        //设置已配对的列表
        newBluetooth.setAdapter(newBlueToothAdapter);
        oldBluetooth.setAdapter(oldBlueToothAdapter);
        //设置列表监听事件
        newBluetooth.setOnItemClickListener(list);//传入下面写的方法
        oldBluetooth.setOnItemClickListener(list);
        //注册广播接收器
        IntentFilter intentFilter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(broadcastReceiver,intentFilter);//第一个参数传入下面注册的广播
        intentFilter=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(broadcastReceiver,intentFilter);
    }

    //搜索附近设备
    private void search(){
        Toast.makeText(this,"正在搜索设备.....",Toast.LENGTH_SHORT).show();
         findViewById(R.id.tv1).setVisibility(View.VISIBLE);
         findViewById(R.id.tv2).setVisibility(View.VISIBLE);
        if (bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
        //重新开始查找
        bluetoothAdapter.startDiscovery();
    }

    /*
    *listview 设置点击事件
     */
    private AdapterView.OnItemClickListener list=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("tag", "1");
            bluetoothAdapter.cancelDiscovery();
            //得到mac地址
            String mac=((TextView)view).getText().toString();//得到bluetoothname的textview里的mac
            String address=mac.substring(mac.length()-17);
            Intent intent=new Intent();
            intent.putExtra(DEVICE_ADDRESS,address);
            //设置返回数据
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    };
    //广播接收器,搜索设备和广播是同步的在点击搜索的同时进入广播。
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                //得到蓝牙设备
                BluetoothDevice bluetoothDevice=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //判断是否配过对
                if (bluetoothDevice.getBondState()!=BluetoothDevice.BOND_BONDED){
                    newBlueToothAdapter.add(bluetoothDevice.getName()+"\n"
                    +bluetoothDevice.getAddress());
                }else{
                    oldBlueToothAdapter.add(bluetoothDevice.getName()+"\n"
                    +bluetoothDevice.getAddress());
                }
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                setProgressBarIndeterminateVisibility(true);
                setTitle("选择要连接的设备");
            }
        }
    };
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        if (bluetoothAdapter!=null){
            bluetoothAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(broadcastReceiver);
    }

}
