package top.jjust.conn;

import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.Socket;

import top.jjust.socketmanager.SocketManager;

/**
 * Created by lee on 16-3-29.
 */
public class ConnManager {
    private WifiManager wifiManager = null;
    //single-instance
    private ConnManager(WifiManager wifiManager){
        this.wifiManager = wifiManager;
    }
    private static ConnManager instance;
    public static ConnManager getInstance(WifiManager wifiManager){
        if(instance == null){
            instance = new ConnManager(wifiManager);
        }
        return  instance;
    }

    /**
     * 传输端开启AP
     */
    public Socket sendStart(){
        WifiAPControl.openWifiAP(wifiManager);
        Socket socket = null;
        //循环等待开启ap完成
        while(WifiAPControl.getWifiApState(wifiManager)!= WifiAPControl.WIFI_AP_STATE.WIFI_AP_STATE_ENABLED){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            socket = SocketManager.getInstance().listenSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  socket;

    }

    /**
     * 接受端开启wifi链接到指定ap
     */
    public Socket getStart(){
        WifiControl.connWIFI(wifiManager);
        //循环等待wifi链接完成
        while(WifiControl.getWifiState(wifiManager)!= WifiControl.WIFI_STATE.WIFI_STATE_ENABLED){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            return SocketManager.getInstance().connSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void sendClose(){
        WifiAPControl.closeWifiAP(wifiManager);
    }
    public void getClose(){
        WifiControl.closeWifi(wifiManager);
    }

    /**
     * handler传送
     * @param obj
     * @param handler
     * @param what
     */
    public static void handlerSendMsg(Object obj,Handler handler,int what){
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

}
