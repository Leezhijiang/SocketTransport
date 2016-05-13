package top.jjust.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import top.jjust.bean.FileBean;
import top.jjust.bean.FileBeanSimple;

/**
 * Created by lee on 16-3-28.
 */
public class ReorderUtils {
    /**
     * 根据type对list进行排序
     *
     * @param beanList
     * @param type
     * @return
     */
    public static  ArrayList<FileBeanSimple> ReorderFileBean(ArrayList<FileBeanSimple> beanList, String type) {
        //根据参数判断排序方式
        try {
            Class c = Class.forName(type);
            Collections.sort(beanList,(Comparator)c.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return beanList;
    }

}

class SortByName implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {
        FileBeanSimple o1 = (FileBeanSimple) lhs;
        FileBeanSimple o2 = (FileBeanSimple) rhs;
        return o1.getFileName().compareTo(o2.getFileName());
    }
}

class SortBySizeAsc implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {
        FileBeanSimple o1 = (FileBeanSimple) lhs;
        FileBeanSimple o2 = (FileBeanSimple) rhs;
        return (int) (o1.getRawSize() - o2.getRawSize());
    }

}
class SortBySizeDesc implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {
        FileBeanSimple o1 = (FileBeanSimple) lhs;
        FileBeanSimple o2 = (FileBeanSimple) rhs;
        return -(int) (o1.getRawSize() - o2.getRawSize());
    }

}

class SortByType implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {
        FileBeanSimple o1 = (FileBeanSimple) lhs;
        FileBeanSimple o2 = (FileBeanSimple) rhs;
        return o1.getFileType().compareTo(o2.getFileType());
    }

}

