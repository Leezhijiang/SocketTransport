package top.jjust.bean;

import java.io.InputStream;
import java.util.ArrayList;

import top.jjust.filemanager.FileManager;

/**
 * Created by lee on 16-3-29.
 */
//数据传输对象
public class DataBean implements java.io.Serializable{
    private String dataName;
    private double dataSize;
    private InputStream dataStream;



    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public double getDataSize() {
        return dataSize;
    }

    public void setDataSize(double dataSize) {
        this.dataSize = dataSize;
    }

    public InputStream getDataStream() {
        return dataStream;
    }

    public void setDataStream(InputStream dataStream) {
        this.dataStream = dataStream;
    }

    public DataBean(FileBeanSimple file) throws Exception {
        this.dataName = file.getFileName();
        this.dataSize = file.getRawSize();
        this.dataStream = FileManager.getFileInputStream(file.getPath());
    }
}
