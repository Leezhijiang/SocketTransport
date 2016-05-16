package top.jjust.sockettransport;

import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;

import top.jjust.MaterialDesign.MaterialButton;
import top.jjust.MaterialDesign.MaterialSwitch;
import top.jjust.common.Loger;
import top.jjust.common.StaticValue;
import top.jjust.conn.WifiAPControl;
import top.jjust.conn.WifiControl;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout send = (LinearLayout) findViewById(R.id.send);
        LinearLayout get = (LinearLayout) findViewById(R.id.get);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // 打开wifi热点_ok
                                         if (WifiAPControl.openWifiAP((WifiManager) MainActivity.this.getSystemService(WIFI_SERVICE))) {
                                             //跳转页面
                                             Intent intent = new Intent(MainActivity.this, SendActivity.class);
                                             MainActivity.this.startActivity(intent);
                                         }
            }
        });
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 链接wifi
                if (WifiControl.connWIFI((WifiManager) MainActivity.this.getSystemService(WIFI_SERVICE))) {
                    //跳转页面
                    Intent intent = new Intent(MainActivity.this, GetActivity.class);
                    MainActivity.this.startActivity(intent);
                }
            }
        });
//        MaterialSwitch btn = (MaterialSwitch) this.findViewById(R.id.btn_send);
//
//        btn.setOnUpClickListener(new MaterialSwitch.OnUpClickListener() {
//                                     @Override
//                                     public void onclick() {
//                                         //打开wifi热点_ok
//                                         if (WifiAPControl.openWifiAP((WifiManager) MainActivity.this.getSystemService(WIFI_SERVICE))) {
//                                             //跳转页面
//                                             Intent intent = new Intent(MainActivity.this, SendActivity.class);
//                                             MainActivity.this.startActivity(intent);
//                                         }
//                                     }
//                                 }
//        );
//        btn.setOnDownClickListener(new MaterialSwitch.OnDownClickListener() {
//            @Override
//            public void onclick() {
//                // 链接wifi
//                if (WifiControl.connWIFI((WifiManager) MainActivity.this.getSystemService(WIFI_SERVICE))) {
//                    //跳转页面
//                    Intent intent = new Intent(MainActivity.this, GetActivity.class);
//                    MainActivity.this.startActivity(intent);
//                }
//            }
//        });
        // MaterialSwitch btnGet = (MaterialSwitch)this.findViewById(R.id.btn_get);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //打开wifi热点_ok
//                if (WifiAPControl.openWifiAP((WifiManager) MainActivity.this.getSystemService(WIFI_SERVICE))) {
//                    //跳转页面
//                    Intent intent = new Intent(MainActivity.this, SendActivity.class);
//                    MainActivity.this.startActivity(intent);
//                }
//            }
//        });
//        btnGet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //链接wifi
//               if(WifiControl.connWIFI((WifiManager) MainActivity.this.getSystemService(WIFI_SERVICE))){
//                   //跳转页面
//                   Intent intent = new Intent(MainActivity.this,GetActivity.class);
//                   MainActivity.this.startActivity(intent);
//               }
//
//            }
//        });
        createStoreDir();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createStoreDir() {
        File file = new File(StaticValue.STORE_PATH);
        Loger.takeLog(StaticValue.STORE_PATH);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://top.jjust.sockettransport/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://top.jjust.sockettransport/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
