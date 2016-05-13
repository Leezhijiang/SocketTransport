package top.jjust.sockettransport;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import top.jjust.bean.FileBean;
import top.jjust.common.StaticValue;
import top.jjust.filemanager.FileManager;
import top.jjust.transport.Transport;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        File fromFile = new File("/home/lee/文档/test.txt");
//        File aimFile = null;
//        try {
//            aimFile = Transport.creatFile("/home/lee/文档", "aim.txt");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Transport.sendFileTest(aimFile,fromFile);
        File file = new File("/home/lee/文档/aim.txt");
        InputStream is = new FileInputStream(file);
        Transport.getFileTest(is);
    }
}