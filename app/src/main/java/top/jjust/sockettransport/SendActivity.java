package top.jjust.sockettransport;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.LogRecord;

import top.jjust.bean.FileBeanSimple;
import top.jjust.common.Loger;
import top.jjust.common.StaticValue;
import top.jjust.conn.ConnManager;
import top.jjust.conn.WifiAPControl;
import top.jjust.filemanager.FileManager;
import top.jjust.socketmanager.SocketManager;
import top.jjust.transport.Transport;

public class SendActivity extends AppCompatActivity {
    private boolean exit = false;
    private TextView state_desc = null;
    private TextView file_desc = null;
    private TextView process_desc = null;
    private Thread apOpenThread = null;
    private Button btn_send = null;
    private Button btn_file = null;
    private FileBeanSimple sendFile;
    private WifiManager wifimanager;
    private Socket socket = new Socket();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StaticValue.SEND_CHANGE_DESC:
                    state_desc.setText((String)msg.obj);
                    break;
                case StaticValue.PROCESS_CHANGED:
                    //改变进度条
                    //process_desc.setText((int)msg.obj+"%");
                    process_desc.setText(String.valueOf(msg.obj)+"%");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send);

        setTitle("发送文件");
        wifimanager = (WifiManager) SendActivity.this.getSystemService(WIFI_SERVICE);
        state_desc = (TextView) findViewById(R.id.state_desc);
        file_desc = (TextView) findViewById(R.id.file_desc);
        process_desc = (TextView) findViewById(R.id.process_desc);
        btn_send = (Button)findViewById(R.id.btn_send);
        btn_file = (Button) findViewById(R.id.btn_file);
        btn_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendActivity.this, FileActivity.class);
                SendActivity.this.startActivityForResult(intent, 0);
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendFile != null) {
                    sendFile(sendFile);
                }
            }
        });
        apOpenThread = new Thread(new TestApOpenThread());
        apOpenThread.start();
    }


    //wifiap打开检测
    class TestApOpenThread implements Runnable {
        private Message msg  = new Message();
        @Override
        public void run() {
            while (!WifiAPControl.WIFI_AP_STATE.WIFI_AP_STATE_ENABLED.equals(WifiAPControl.getWifiApState(wifimanager))) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ConnManager.handlerSendMsg("成功开启热点，等待接收端连接", handler, StaticValue.SEND_CHANGE_DESC);
            while(!exit) {
                //开启socket监听
                try {
                    Loger.takeLog("开启socket监听");
                    socket = SocketManager.getInstance().listenSocket();
                    Loger.takeLog("成功链接sokcet");
                    ConnManager.handlerSendMsg("成功与接收端连接，可以发送文件", handler, StaticValue.SEND_CHANGE_DESC);
                } catch (IOException e) {
                    e.printStackTrace();
                    Loger.takeLog("开启socket失败");
                    ConnManager.handlerSendMsg("开启失败，请重试", handler, StaticValue.SEND_CHANGE_DESC);
                }
            }
        }
    }

    /**
     * 发送文件的包装方法
     * @param file
     */
    public void sendFile(final FileBeanSimple file){
        if(socket!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Transport.sendFile(file,socket,handler);
                }
            }).start();

        }
        else
            Toast.makeText(SendActivity.this,"接收端尚未链接,请等待接受端链接后再次发送",Toast.LENGTH_LONG).show();
    }
    public void onFinish(){
        exit=true;
        if(socket!=null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        WifiAPControl.closeWifiAP(wifimanager);
    }
    //接受FIleactivity的选择文件
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null) {
            sendFile = (FileBeanSimple) data.getSerializableExtra("file");
            file_desc.setText("选择文件：" + sendFile.getFile());
        }
    }

    @Override
    protected void onDestroy() {
        onFinish();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //拦截返回见，返回上级目录
                onFinish();
                return  super.onKeyDown(keyCode, event);
        }
        return  super.onKeyDown(keyCode, event);

    }
}
