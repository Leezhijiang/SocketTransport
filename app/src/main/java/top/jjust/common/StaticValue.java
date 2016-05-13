package top.jjust.common;

import android.os.Environment;

/**
 * Created by lee on 16-3-27.
 */
public class StaticValue {
    //file根目录
    public static final String fileRootPath = android.os.Environment.getExternalStorageDirectory()
            .getPath();
    //根据Name排序
    public static final String SORT_BY_NAME = "top.jjust.utils.SortByName";
    //根据size升序排序
    public static final String SORT_BY_SIZE_ASC = "top.jjust.utils.SortBySizeAsc";
    //根据size降序排序
    public static final String SORT_BY_SIZE_DESC = "top.jjust.utils.SortBySizeDesc";
    //根据type排序
    public static final String SORT_BY_TYPE = "top.jjust.utils.SortByType";
    //wifi的名字
    public static final String WIFINAME = "JJUST_WIFI_FILE_";
    //wifi的密码
    public static final String WIFIPD = "12345678";
    //Wifiap的ip地址
    public static final String AP_IP="192.168.43.1";
    //Wifiap的端口地址
    public static final int AP_PORT=2910;
    //传输使用的分割符
    public static final String SPLIT= "@@@@@！！！！J1J1U1S1T1_1W1I1F1I1_1F1I1L1E1！！！！@@@@";
    //接收文件存放位置
    public static final String STORE_PATH = android.os.Environment.getExternalStorageDirectory().getPath()+"/"+"FileShared";
    //handler——send参数
    //wifiap打开
    public static final int SEND_WIFI_AP_OPEN = 1;
    //wifiap链接到了用户
    public static final int SEND_WIFI_AP_CONN = 2;
    //send页面修改desc
    public static final int SEND_CHANGE_DESC = 3;
    //更新process
    public static  final  int PROCESS_CHANGED = 4;
    //get页面修改desc
    public static final int GET_CHANGE_DESC = 5;
    //连接到正确wifi
    public static final int CONN_RIGHT = 6;
    //连接到错误wifi
    public static final int CONN_WRONG = 7;
    //还没有链接到wifi
    public static final int CONN_NULL = 8;
    //发送一个Toast
    public static final int MAKE_TOAST = 9;
    //handler，file修改
    public static final int FILE_CHANGED = 10;
    //buffered大小
    public static final int GET_BUFFER_SIZE  = 1024*1024*6;
    public static final int SEND_BUFFER_SIZE  = 1024*1024;
}
