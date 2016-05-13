package top.jjust.filemanager;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

import top.jjust.bean.FileBean;
import top.jjust.bean.FileBeanSimple;
import top.jjust.common.Loger;
import top.jjust.common.StaticValue;
import top.jjust.utils.ReorderUtils;

/**
 * Created by lee on 16-3-28.
 */
public class FileManager {
    /**
     * 获取当前目录下的所有子文件bean
     * @param prefile
     * @param rootBean
     * @return
     */
    public static FileBean getFileList_R(File prefile,FileBean rootBean){
        File[] fileArray = prefile.listFiles();
        FileBean bean;
        //遍历根目录文件列表
        for(File mfile:fileArray) {
            bean = new FileBean(mfile);
            rootBean.setChildFile(bean);
            //如果是文件夹则递归
            if(mfile.isDirectory()){
                getFileList_R(mfile,bean);
            }
        }
        return rootBean;
    }

    /**
     * 获取当前filebean目录下的文件bean
     * @param rootBean
     * @return
     */
    public static ArrayList<FileBeanSimple> getFileList(FileBeanSimple rootBean){
        ArrayList<FileBeanSimple> beanList = new ArrayList<FileBeanSimple>();
        String path = rootBean.getPath();
        File file = new File(path);
        Loger.takeLog(path);
        if(file.isDirectory()) {
            File[] fileArray = file.listFiles();
            //遍历根目录文件列表
            for (File mfile : fileArray) {
                //去除android隐藏文件
                if(!mfile.getName().startsWith(".")) {
                    beanList.add(new FileBeanSimple(mfile));
                }
            }
        }
        Loger.takeLog(beanList.toString());
        return  ReorderUtils.ReorderFileBean(beanList,StaticValue.SORT_BY_NAME);
    }

    /**
     * 文件获取inputsream
     * @param path
     * @return
     * @throws Exception
     */
    public static InputStream getFileInputStream(String path) throws Exception {
        File file = new File(path);
        InputStream in = null;
        if(!file.isDirectory()){
             in= new FileInputStream(file);
        }else {
            throw new Exception("选择了一个文件夹上传");
        }
        return  in;
    }

    /**
     * 接受inputsream存入文件中
     * @param in
     * @param file
     * @return
     */
    public static boolean iutPutStream2File(InputStream in,File file){
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }catch (Exception e){
            return  false;
        }finally {
            try {
                os.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                return  false;
            }

        }
        return true;

    }
}
