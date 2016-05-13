package top.jjust.utils;

import java.math.BigDecimal;
import java.util.ArrayList;

import top.jjust.bean.DataBean;
import top.jjust.bean.FileBeanSimple;

/**
 * Created by lee on 16-3-28.
 */
public class ConvertUtils {
    public static String getFormatSize(double size) {
        double kiloByte = size/1024;
        if(kiloByte < 1) {
            return size + "Byte(s)";
        }

        double megaByte = kiloByte/1024;
        if(megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte/1024;
        if(gigaByte < 1) {
            BigDecimal result2  = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte/1024;
        if(teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }
    public static ArrayList<DataBean> fileList2DataList(ArrayList<FileBeanSimple> fileList) throws Exception {
        ArrayList<DataBean> dataList = new ArrayList<DataBean>();
        for(FileBeanSimple file:fileList){
            dataList.add(new DataBean(file));
        }
        return dataList;
    }
}
