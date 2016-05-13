package top.jjust.sockettransport;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.File;

import top.jjust.MaterialDesign.MaterialButton;
import top.jjust.common.Loger;
import top.jjust.common.StaticValue;
import top.jjust.conn.WifiAPControl;
import top.jjust.conn.WifiControl;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MaterialButton btnSend = (MaterialButton) this.findViewById(R.id.btn_send);
        MaterialButton btnGet = (MaterialButton)this.findViewById(R.id.btn_get);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开wifi热点_ok
                if(WifiAPControl.openWifiAP((WifiManager) MainActivity.this.getSystemService(WIFI_SERVICE))){
                    //跳转页面
                    Intent intent = new Intent(MainActivity.this,SendActivity.class);
                    MainActivity.this.startActivity(intent);
                }
            }
        });
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //链接wifi
               if(WifiControl.connWIFI((WifiManager) MainActivity.this.getSystemService(WIFI_SERVICE))){
                   //跳转页面
                   Intent intent = new Intent(MainActivity.this,GetActivity.class);
                   MainActivity.this.startActivity(intent);
               }

            }
        });
        createStoreDir();
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
    public void createStoreDir(){
        File file = new File(StaticValue.STORE_PATH);
        Loger.takeLog(StaticValue.STORE_PATH);
        if(!file.exists()||!file.isDirectory()){
            file.mkdirs();
        }
    }
}
