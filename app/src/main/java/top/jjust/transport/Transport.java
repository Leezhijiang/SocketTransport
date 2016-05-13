package top.jjust.transport;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;

import top.jjust.bean.DataBean;
import top.jjust.bean.FileBeanSimple;
import top.jjust.common.Loger;
import top.jjust.common.StaticValue;
import top.jjust.conn.ConnManager;
import top.jjust.utils.ConvertUtils;

/**
 * Created by lee on 16-3-29.
 */
public class Transport {
    /**
     * 发送文件到socket的另一端
     * @param socket
     * @return
     */

    public static boolean sendFile(FileBeanSimple file,Socket socket,Handler handler){
        OutputStream os =null;
        try {
            os = socket.getOutputStream();
            FileInputStream fis = new FileInputStream(file.getFile());
            os.write((file.getFileName() + StaticValue.SPLIT+String.valueOf(file.getRawSize())+StaticValue.SPLIT).getBytes());
            os.flush();
            byte[] buffer = new byte[StaticValue.SEND_BUFFER_SIZE];
            double sendSize = 0;
            int length = 0;

            while((length=fis.read(buffer))!=-1){
                os.write(buffer,0,length);
                sendSize+=length;
                Loger.takeLog(sendSize + "/" + length);
                //修改界面
                ConnManager.handlerSendMsg(new DecimalFormat("###0.00").format((sendSize / file.getRawSize()) * 100), handler, StaticValue.PROCESS_CHANGED);
                os.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 从另一端读取socket的传送对象
     * @param socket
     * @return
     */
    public static boolean getFile(Socket socket, android.os.Handler handler){

        InputStream is = null;
        File file = null;
        OutputStream fos = null;
        try {
            is = socket.getInputStream();
            String name="";//文件名
            int count = 0;//分离次数
            int cacheLength = 0;//缓存大小
            int tempLength  = 0;//暂时存储空间
            byte[] temp = new byte[1024];//暂时存储空间
            double size = 0;//文件大小
            double getsize = 0;//已经得到的文件大小
            byte[] cache = new byte[1024];//缓存
            byte[] buffer = new byte[StaticValue.GET_BUFFER_SIZE];
            int length = 0;
//            while(()!=-1){
//                //进行读取，存入缓存，分析缓存
//                //进行读取到buffer
//                length=is.read(buffer);
//                if(count<2) {
//                    //还没有分离出文件名和大小
//                    allLength += length;
//                    //偏移赋值,存入缓存
//                    for (int i = 0; i < length; i++) {
//                        cache[i + allLength] = buffer[i];
//                    }
//                    String values[] = String.valueOf(cache).split(StaticValue.SPLIT);
//                    //分析缓存
//                    //只分出了一种
//                    if (values.length == 1) {
//                        if(count==0){
//                            //文件名
//                            name = values[0];
//                            //新建文件夹
//                            file = new File(StaticValue.STORE_PATH,name);
//                            fos = new FileOutputStream(file);
//                        }else {
//                            //大小
//                            size = Double.valueOf(values[0]);
//                        }
//                        count++;
//                    } else if (values.length == 2) {//分除了文件名和文件长度
//                        name = values[0];
//                        file = new File(StaticValue.STORE_PATH,name);
//                        size = Double.valueOf(values[1]);
//                        count += 2;
//                    }
//                    if(count==2){
//                        //count=2说明下次全是程序，清理cache数组(加长getsize大小，把遗留的部分存入文件内)
//                        byte[] temp = (name+StaticValue.SPLIT+size+String.valueOf(size)+StaticValue.SPLIT).getBytes();
//                        int remainLength = allLength - temp.length;
//                        getsize = remainLength;
//                        fos.write(cache,temp.length,allLength);
//                    }
//                }else{
//                    //已经分离出文件名和大小
//
//                }
//            }
            //
            //

            cacheLength = is.read(cache);//读取数据进如缓存进行分析处理
            //分割处理
            String[] values = (new String(cache)).split(StaticValue.SPLIT);
            Loger.takeLog(cacheLength+"");
            Loger.takeLog(new String(cache));
            //发送段没有关闭
            if(cacheLength!=-1) {
                while (values.length < 3) {
                    tempLength = is.read(temp);
                    for (int i = cacheLength; i < 1024; i++) {
                        cache[i] = temp[i];
                        cacheLength += tempLength;
                    }
                    values = (new String(cache)).split(StaticValue.SPLIT);
                    Loger.takeLog(cacheLength + "");
                    Loger.takeLog(new String(cache));
                    if (values.length >= 3) {
                        break;
                    }
                    if (cacheLength >= 1000) {
                        Loger.takeLog("不要传那么大的文件名,都超过1k字了");
                    }
                }
                name = values[0];
                size = new Double(values[1]);
                ConnManager.handlerSendMsg("正在接收文件", handler, StaticValue.GET_CHANGE_DESC);
                ConnManager.handlerSendMsg(name,handler,StaticValue.FILE_CHANGED);
                //新建文件
                Loger.takeLog("开始处理文件");
                file = creatFile(StaticValue.STORE_PATH, name);
                fos = new FileOutputStream(file);
                //处理缓存中的正文数据
                int headLength = (name + StaticValue.SPLIT + values[1] + StaticValue.SPLIT).getBytes().length;
                int remainLength = cacheLength - headLength;
                getsize = remainLength;
                fos.write(cache, headLength, (int) getsize);
                fos.flush();
                Loger.takeLog("继续接受文件");
                //正文处理完毕，开始继续接受

                Loger.takeLog(getsize + "/" + size);
                while (getsize < size) {
                    length = is.read(buffer);
                    getsize += length;
                    Loger.takeLog(length + "");
                    if (length == -1)
                        break;
                    fos.write(buffer, 0, length);
                    //修改界面
                    ConnManager.handlerSendMsg(new DecimalFormat("###0.00").format((getsize / size) * 100), handler, StaticValue.PROCESS_CHANGED);

                    fos.flush();
                }
                fos.close();
                Loger.takeLog("传输完了");
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        Loger.takeLog("客户端已经关闭链接了");
        return false;
    }
    public static File creatFile(String path,String name) throws IOException {
        File file = new File(path,name);
        if(file.exists()){
            Loger.takeLog("name:"+name);
            String[] names = name.split("\\.");
            Loger.takeLog("namesl"+names.length);
            if(names.length==2){
                Loger.takeLog("changname");
                name = names[0]+"_"+"."+names[1];
            }
            else
                name+="_";
            return creatFile(path,name);
        }else {
            file.createNewFile();
        }
        return file;
    }
    //****************************************************测试用例＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊

    /**
     * 从另一端读取socket的传送对象
     * @return
     */
    public static boolean getFileTest(InputStream is){
        File file = null;
        OutputStream fos = null;
        try {
            String name="";//文件名
            int count = 0;//分离次数
            int cacheLength = 0;//缓存大小
            double size = 0;//文件大小
            double getsize = 0;//已经得到的文件大小
            byte[] cache = new byte[1024];//缓存
            byte[] buffer = new byte[1024];
            int length = 0;
            cacheLength = is.read(cache);//读取数据进如缓存进行分析处理
            //分割处理
            String value = new String(cache);
            System.out.println("value:"+value);
            String[] values = value.split(StaticValue.SPLIT);
            System.out.println("chagndu"+values.length);
            if(values.length>=3){
                name = values[0];
                size = new Double(values[1]);
                System.out.println("size:"+size);
                System.out.println("name:"+name);
            }else{
                System.out.println("不要传那么大的文件名,都超过1k字了");
                return false;
            }
            //新建文件
            file = creatFile("/home/lee/文档",name);
            //修改界面
            fos = new FileOutputStream(file);
            //处理缓存中的正文数据
            int headLength = (name+StaticValue.SPLIT+values[1]+StaticValue.SPLIT).getBytes().length;
            int remainLength = cacheLength - headLength;
            getsize = remainLength;
            System.out.println("getsize:"+getsize);
            System.out.println("cacheLength:"+cacheLength);
            System.out.println("headLength:"+headLength);
            fos.write(cache,headLength,(int)getsize);
            fos.flush();
            //正文处理完毕，开始继续接受
            while(getsize<size) {
                System.out.println("继续传输:");
                length = is.read(buffer);
                getsize += length;
                fos.write(buffer, 0, length);
                fos.flush();
            }
            fos.close();
            System.out.println("传输完了");
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
    public static boolean sendFileTest(File aimFile,File formFile){
        OutputStream os =null;
        try {
            os = new FileOutputStream(aimFile);
            FileInputStream fis = new FileInputStream(formFile);
            FileBeanSimple file = new FileBeanSimple(formFile);
            os.write(file.getFileName().getBytes());
            os.write(StaticValue.SPLIT.getBytes());
            os.write(String.valueOf(file.getRawSize()).getBytes());
            os.write(StaticValue.SPLIT.getBytes());
            byte[] buffer = new byte[1024];
            int length = 0;
            while((length=fis.read(buffer))!=-1){
                os.write(buffer,0,length);
                os.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

}
