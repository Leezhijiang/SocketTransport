package top.jjust.bean;

import java.io.File;
import java.util.ArrayList;

import top.jjust.utils.ConvertUtils;

/**
 * Created by lee on 16-3-28.
 */
public class FileBean extends FileBeanSimple{


    private ArrayList<FileBean> childFile ;

    public ArrayList<FileBean> getChildFile() {
        return childFile;
    }

    public void setChildFile( FileBean mchildFile) {
        if(childFile==null){
            childFile = new ArrayList<FileBean>();
        }
        this.childFile.add(mchildFile);
    }

    @Override
    public String toString() {
        return "fileName=" + fileName + ",fileType=" + fileType +",fileSize" + fileSize +System.getProperty("line.separator") + "    " + childFile+ System.getProperty("line.separator");
    }
    public FileBean(File file){
        super(file);
    }
}
