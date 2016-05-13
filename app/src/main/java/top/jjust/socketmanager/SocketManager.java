package top.jjust.socketmanager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import top.jjust.common.Loger;
import top.jjust.common.StaticValue;

/**
 * Created by lee on 16-3-27.
 */
public class SocketManager {
    //单例socket
    private Socket socket;
    private ServerSocket sSocket;
    private SocketManager() throws IOException {
        sSocket = new ServerSocket(StaticValue.AP_PORT);
        }

    public static SocketManager instance =null;
    public static SocketManager getInstance(){
        if(instance==null){
            try {
                instance = new SocketManager();
            } catch (IOException e) {
                Loger.takeLog(e.getMessage());
                e.printStackTrace();
            }
        }
        return  instance;
    }
    /**
     * 根据ip链接socket
     */
    public Socket connSocket() throws IOException {
            //新建socket。链接服务器
//        SocketAddress remoteAddr = new InetSocketAddress(StaticValue.AP_IP,StaticValue.AP_PORT);
//            socket.connect(remoteAddr,3000);
            if(socket!=null){
                socket.close();
            }
            socket = new Socket();
            SocketAddress remoteAddr = new InetSocketAddress(StaticValue.AP_IP,StaticValue.AP_PORT);
            socket.connect(remoteAddr,3000);
        Loger.takeLog("socket创建成功");
        return socket;
    }
    /**
     * 持续监听端口，直到返回socket
     * @return
     */
    public Socket listenSocket() throws IOException {
        //持续监听端口
        Socket acSocket = sSocket.accept();
        return acSocket;
    }
    /**
     * 关闭socket
     * @return true=成功
     */
    public boolean closeSocket(){
        if (socket!=null){
            try {
                socket.close();
                return  true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }
        return  true;
    }
    public boolean closeSScoket(){
        if(sSocket!=null){
            try {
                sSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                return  false;
            }
        }
        return  true;
    }
    public boolean destory(){
        if(closeSocket()&&closeSScoket()){
            return true;
        }
        return false;
    }
}
