package top.jjust.conn;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.util.List;

import top.jjust.common.Loger;
import top.jjust.common.StaticValue;

/**
 * Created by lee on 16-3-29.
 */
public class WifiControl {
    public enum WIFI_STATE {
        WIFI_STATE_ENABLED,WIFI_STATE_ENABLING,WIFI_STATE_DISABLING,WIFI_STATE_DISABLED,WIFI_STATE_UNKNOWN
        }
    /**
     *链接指定WIfi
     */
    public static boolean connWIFI(WifiManager wifiManager){
        boolean a = openWifi(wifiManager);
        Thread thread = new Thread(new ConnectRunnable(wifiManager));
        thread.start();
        return a;
    }
    /**
     * 打开wifi
     *
     * @param wifiManager
     */
    private static boolean openWifi(WifiManager wifiManager) {
        //关闭热点
        WifiAPControl.closeWifiAP(wifiManager);
        if (!wifiManager.isWifiEnabled()) {
            //wifi打开状态
            return wifiManager.setWifiEnabled(true);
        }
        return true;
    }
    /**
     * 每秒检测一下当前wifi的名称，根据名声判断
     * @return
     * @throws InterruptedException
     */
    public static int checkConnBySSID(WifiManager wifiManager){
        Loger.takeLog("检测wifi名称");
        while(!wifiManager.getConnectionInfo().getSSID().contains(StaticValue.WIFINAME)){
            if(wifiManager.getConnectionInfo().getSSID().contains("unknown ssid")){
                try {
                    Loger.takeLog("wifi没有链接");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }else {
                Loger.takeLog(wifiManager.getConnectionInfo().getSSID());
                return StaticValue.CONN_WRONG;
            }

        }
        return StaticValue.CONN_RIGHT;
    }
    /**
     * 关闭wifi
     *
     * @param wifiManager
     */
    public static void closeWifi(WifiManager wifiManager) {
        if (wifiManager.isWifiEnabled()) {
            //wifi打开状态
            Loger.takeLog("close_wifi");
            wifiManager.setWifiEnabled(false);
        }
    }
    public static WIFI_STATE getWifiState(WifiManager wifiManager){
        return  WIFI_STATE.class.getEnumConstants()[wifiManager.getWifiState()];
    }

    /**
     * 创建wifi配置
     * @param SSID
     * @param Password
     * @return
     */
    private static WifiConfiguration createWifiInfo(String SSID, String Password) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        //全部采用WPA加密
        config.preSharedKey = "\"" + Password + "\"";
        config.hiddenSSID = true;
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        // 此处需要修改否则不能自动重联
        // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.status = WifiConfiguration.Status.ENABLED;
        return config;
    }

    /**
     * 查看wifi是否配置过
     *
     * @param SSID
     * @param wifiManager
     * @return
     */
    public static WifiConfiguration isExsits(String SSID, WifiManager wifiManager) {
        Loger.takeLog("isExsites?");
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        if(existingConfigs!=null) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                    return existingConfig;
                }
            }
        }else
        Loger.takeLog("EXSITSS——null");

        return null;
    }

    /**
     * 异步链接wifi
     */
    static class ConnectRunnable implements Runnable {
        private String ssid = StaticValue.WIFINAME;

        private String password =StaticValue.WIFIPD;
        private WifiManager wifiManager;

        public ConnectRunnable(WifiManager wifiManager) {
            this.wifiManager = wifiManager;
        }


        @Override
        public void run() {
            // 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
            // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
            while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                try {
                    // 为了避免程序一直while循环，让它睡个100毫秒检测……
                    Thread.sleep(100);
                    Loger.takeLog("wait_wifi_open");
                } catch (InterruptedException ie) {
                }
            }
            Loger.takeLog("find_wifi");
            WifiConfiguration wifiConfig = createWifiInfo(ssid, password);
            //
            if (wifiConfig == null) {
                return;
            }

            WifiConfiguration tempConfig = isExsits(ssid, wifiManager);

            if (tempConfig != null) {
                wifiManager.removeNetwork(tempConfig.networkId);
            }
            Loger.takeLog("配置wifi");
            int netID = wifiManager.addNetwork(wifiConfig);
            wifiManager.enableNetwork(netID, true);
        }
    }

}
