package top.jjust.conn;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Method;


import top.jjust.common.Loger;
import top.jjust.common.StaticValue;

/**
 * Created by lee on 16-3-29.
 */
public class WifiAPControl {
    public enum WIFI_AP_STATE {
        WIFI_AP_STATE_DISABLING, WIFI_AP_STATE_DISABLED, WIFI_AP_STATE_ENABLING, WIFI_AP_STATE_ENABLED, WIFI_AP_STATE_FAILED

    }

    /**
     * 打开手机热点
     * @param wifiManager
     */
    public static boolean openWifiAP(WifiManager wifiManager){
        //关闭wifi，打开热点
        WifiControl.closeWifi(wifiManager);
        if(getWifiApState(wifiManager)==WIFI_AP_STATE.WIFI_AP_STATE_DISABLED) {
            Loger.takeLog("open_ap");
            return setWifiAPEnable(wifiManager, true);
        }else{
            closeWifiAP(wifiManager);
            return setWifiAPEnable(wifiManager, true);
        }
    }

    /**
     * 关闭手机热点
     * @param wifiManager
     * @return
     */
    public static boolean closeWifiAP(WifiManager wifiManager){
        if(getWifiApState(wifiManager)==WIFI_AP_STATE.WIFI_AP_STATE_ENABLED||getWifiApState(wifiManager)==WIFI_AP_STATE.WIFI_AP_STATE_ENABLING) {
            return setWifiAPEnable(wifiManager, false);
        }
        return  true;
    }

    /**
     * 反射设置ap
     * @param wifiManager
     * @param bool
     * @return
     */
    private static Boolean setWifiAPEnable(WifiManager wifiManager,boolean bool){
        try {
            //热点的配置类
            WifiConfiguration apConfig = new WifiConfiguration();
            //配置热点的名称(可以在名字后面加点随机数什么的)
            apConfig.SSID = StaticValue.WIFINAME;
            //配置热点的密码
            apConfig.preSharedKey= StaticValue.WIFIPD;
            apConfig.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            apConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            apConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            apConfig.allowedKeyManagement
                    .set(WifiConfiguration.KeyMgmt.WPA_PSK);
            apConfig.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            apConfig.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            apConfig.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.CCMP);
            apConfig.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.TKIP);
            //通过反射调用设置热点
            Method method = WifiManager.class.getDeclaredMethod(
                    "setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.setAccessible(true);
            //返回热点打开状态
            return (boolean) method.invoke(wifiManager,apConfig,bool);
        } catch (Exception e) {
            Loger.takeLog("open_ap_false");
           Loger.takeLog(Log.getStackTraceString(e));

        }
        return false;
    }



    /**
     * 获得ap状态
     * @param wifiManager
     * @return
     */
    public static  WIFI_AP_STATE getWifiApState(WifiManager wifiManager){
        int tmp;
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApState");
            tmp = ((Integer) method.invoke(wifiManager));
            // Fix for Android 4
            if (tmp > 10) {
                tmp = tmp - 10;
            }
            return WIFI_AP_STATE.class.getEnumConstants()[tmp];
        } catch (Exception e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
            return WIFI_AP_STATE.WIFI_AP_STATE_FAILED;
        }
    }


}
