package top.jjust.bean;

import java.io.File;
import java.io.Serializable;

import top.jjust.utils.ConvertUtils;

/**
 * Created by lee on 16-3-28.
 */
public class FileBeanSimple implements Serializable{
    protected String fileName;
    protected String fileType;
    protected String fileSize;
    protected String path;
    protected double rawSize;

    public double getRawSize() {
        return rawSize;
    }

    public void setRawSize(double rawSize) {
        this.rawSize = rawSize;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    @Override
    public String toString() {
        return "fileName=" + fileName + ",fileType=" + fileType +",fileSize=" + fileSize +System.getProperty("line.separator");
    }
    public void setTypeAuto(File file){
        if(file.isDirectory()){
            this.setFileType("dir");
        }else{
            String[] temp = this.fileName.split("\\.");
            if(temp.length>=2) {
                this.setFileType(temp[temp.length-1]);
            }else
                this.setFileType("");
        }

    }
    public File getFile(){
        return new File(this.getPath());
    }
    public FileBeanSimple(File file){
        this.setFileName(file.getName());
        this.setPath(file.getAbsolutePath());
        this.setRawSize(file.length());
        this.setFileSize(ConvertUtils.getFormatSize(file.length()));
        this.setTypeAuto(file);
    }
}
