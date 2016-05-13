package top.jjust.sockettransport;

import android.test.InstrumentationTestCase;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import top.jjust.bean.FileBean;
import top.jjust.bean.FileBeanSimple;
import top.jjust.common.StaticValue;
import top.jjust.filemanager.FileManager;
import top.jjust.transport.Transport;

/**
 * Created by lee on 16-3-28.
 */
public class Test extends InstrumentationTestCase {
    public void test(){
        File fromFile = new File("/home/lee/文档/test.txt");
        File aimFile = null;
        try {
            aimFile = Transport.creatFile("/home/lee/文档","aim.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Transport.sendFileTest(aimFile,fromFile);
    }
}
