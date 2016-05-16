package top.jjust.sockettransport;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

import top.jjust.bean.DataBean;
import top.jjust.bean.FileBean;
import top.jjust.bean.FileBeanSimple;
import top.jjust.common.Loger;
import top.jjust.common.StaticValue;
import top.jjust.filemanager.FileManager;

public class FileActivity extends AppCompatActivity {
    private Handler handler= new Handler();
    private ListView lv = null;
    private FileAdapter mAdapter = new FileAdapter();
    ArrayList<FileBeanSimple> files;
    ArrayList<ArrayList<FileBeanSimple>> filePath = new ArrayList<ArrayList<FileBeanSimple>>();
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //拦截返回见，返回上级目录
            if(filePath.size()!=1) {
                changeFiles(filePath.get(filePath.size() - 2));
                filePath.remove(filePath.size()-1);
                return true;
            }
            else
                return  super.onKeyDown(keyCode, event);
        }
        return  super.onKeyDown(keyCode, event);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        lv = (ListView)findViewById(R.id.lv_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //判断是否是文件夹如果是文件夹则进入下一目录
                final FileBeanSimple file = files.get(position);
                if (file.getFileType().equals("dir")) {
                    changeFiles(FileManager.getFileList(file));
                    //增加目录深度
                    filePath.add(files);

                } else
                //如果是文件则存储起来
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(FileActivity.this, files.get(position).getFile().getAbsolutePath(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.putExtra("file",file);
                            setResult(0,intent);
                            finish();
                        }
                    });
                }


            }
        });
        (new Thread(new LoadListThread())).start();
    }
    class LoadListThread implements Runnable{

        @Override
        public void run() {
            //初始目录,后期优化，send加载传入file
            files = FileManager.getFileList(new FileBeanSimple(new File(StaticValue.fileRootPath)));
            filePath.add(files);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
            handler.post(new Runnable() {
                @Override
                public void run() {
                    lv.setAdapter(mAdapter);
                }
            });
        }
    }
    class FileAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return files.size();
        }

        @Override
        public Object getItem(int position) {
            return files.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FileBeanSimple file = files.get(position);
            if(file!=null) {
                ViewHolder holder = null;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = View.inflate(FileActivity.this, R.layout.item_file, null);
                    holder.item_im = (ImageView) convertView.findViewById(R.id.item_im);
                    holder.tv_name = (TextView) convertView.findViewById(R.id.item_name);
                    holder.tv_size = (TextView) convertView.findViewById(R.id.item_size);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (file.getFileType().equals("dir")){
                    holder.item_im.setImageResource(R.drawable.mdir18);
                    holder.tv_size.setText("  ");
                }
                else {
                    holder.item_im.setImageResource(R.drawable.mfile18);
                    holder.tv_size.setText(file.getFileSize());
                }
                holder.tv_name.setText(file.getFileName());

            }
            return convertView;
        }

    }
    private static class ViewHolder{
        ImageView item_im;
        TextView tv_name;
        TextView tv_size;
    }

    /**
     * 根据files变更listview
     * @param files
     */
    public void changeFiles (ArrayList<FileBeanSimple> files){
        this.files = files;
        if(mAdapter!=null)
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 返回activity
     * @param file
     */
    public void onResult(FileBeanSimple file){

    }
}
