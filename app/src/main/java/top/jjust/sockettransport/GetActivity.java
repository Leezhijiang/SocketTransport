package top.jjust.sockettransport;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.LogRecord;

import top.jjust.common.Loger;
import top.jjust.common.StaticValue;
import top.jjust.conn.ConnManager;
import top.jjust.conn.WifiControl;
import top.jjust.socketmanager.SocketManager;
import top.jjust.transport.Transport;

public class GetActivity extends AppCompatActivity {
    private boolean exit = false;
    private Thread connSocketThread = null;
    private Socket socket = null;
    private TextView file_desc = null;
    private TextView process_desc = null;
    private TextView state_desc = null;
    private WifiManager wifiManager = null;
    private Message msg = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticValue.GET_CHANGE_DESC:
                    state_desc.setText((String) msg.obj);
                    break;
                case StaticValue.PROCESS_CHANGED:
                    process_desc.setText(String.valueOf(msg.obj)+"%");
                    break;
                case StaticValue.MAKE_TOAST:
                    Toast.makeText(GetActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case StaticValue.FILE_CHANGED:
                    file_desc.setText(String.valueOf(msg.obj));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        setTitle("接收文件");
        wifiManager = (WifiManager) this.getSystemService(WIFI_SERVICE);
        file_desc = (TextView) findViewById(R.id.get_file_desc);
        process_desc = (TextView) findViewById(R.id.get_process_desc);
        state_desc = (TextView) findViewById(R.id.get_state_desc);
        connSocketThread = new Thread(new ConnSocketThread());
        connSocketThread.start();


    }


    class ConnSocketThread implements Runnable {

        @Override
        public void run() {
            while (WifiManager.WIFI_STATE_ENABLED != wifiManager.getWifiState()) {
                try {
                    Loger.takeLog("jiance300");
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            if(WifiControl.checkConnBySSID(wifiManager)==StaticValue.CONN_WRONG){
//                //链接到了错误的wifi
//                Loger.takeLog("链接到了错误的wifi");
//            }
            Loger.takeLog(wifiManager.getWifiState() + "");
            Loger.takeLog("wifi已经打开，开始连接发送端");
            ConnManager.handlerSendMsg("wifi已经打开，开始发送端",handler,StaticValue.GET_CHANGE_DESC);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            try {
//                Socket m = new Socket(StaticValue.AP_IP, StaticValue.AP_PORT);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Loger.takeLog("已经链接socket");
            while(!connSocket()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
                Loger.takeLog("重新连接");
                ConnManager.handlerSendMsg("连接失败，正在重新连接", handler, StaticValue.GET_CHANGE_DESC);
            }

        }
    }


    private boolean connSocket() {
        try {
            socket = SocketManager.getInstance().connSocket();
            //失败重连
            if (socket == null) {
                Loger.takeLog("socket为空,restart socket");
                return false;
            }

        } catch (IOException e) {
            Loger.takeLog(e.getMessage());
            e.printStackTrace();
            return false;
        }

        ConnManager.handlerSendMsg("连接发送端成功，等待发送文件", handler, StaticValue.GET_CHANGE_DESC);
        //持续接受文件
        Loger.takeLog("开始接受文件");
        while (Transport.getFile(socket, handler) && !exit) {
            ConnManager.handlerSendMsg("文件接收完毕，存放在"+StaticValue.STORE_PATH,handler,StaticValue.MAKE_TOAST);
            Loger.takeLog("再次接受文件");
            ConnManager.handlerSendMsg("等待发送端发送文件", handler, StaticValue.GET_CHANGE_DESC);
        }
        if(!exit) {
            Loger.takeLog("发送段关闭，传输结束");
            msg = new Message();
            msg.what = StaticValue.MAKE_TOAST;
            msg.obj = "发送端已经关闭了传输";
            handler.sendMessage(msg);
            GetActivity.this.finish();
        }
        return true;
    }

    public void onFinish() {
        exit = true;
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        WifiControl.closeWifi(wifiManager);
    }

    @Override
    protected void onDestroy() {
        onFinish();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //拦截返回见，返回上级目录
            onFinish();
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);

    }

}
