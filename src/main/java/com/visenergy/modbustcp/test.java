package com.visenergy.modbustcp;

import com.visenergy.utils.ConvertUtils;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author WuSong
 * @Date 2017-08-23
 * @Time 18:35:51
 */
public class test {

    static InputStream is = null;
    static OutputStream os = null;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3000);
<<<<<<< HEAD
            Socket socket = serverSocket.accept();
            is = socket.getInputStream();
            os = socket.getOutputStream();
=======


        Socket socket = serverSocket.accept();
        System.out.println("连接：" + socket.getInetAddress());
        is = socket.getInputStream();
        os = socket.getOutputStream();

>>>>>>> master
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    os.write(ConvertUtils.hexStringToBytes("00000000003901034E0267000100DD000000DC000000DD0000000000010001FFCBFFF803D90000000001F30000FFF800000001000500000002000000EA0000"));
                    Thread.currentThread().sleep(1000);
                    os.write(ConvertUtils.hexStringToBytes("00000000005D00000000000000000000000000000000000000000000000000000000000000000000000000000000010001000200020002000300010003000400000000000000000000000000000000000000000000000000000000000000"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable,0,5, TimeUnit.SECONDS);
        new test().read();
    }
    public void read() throws IOException {
new Thread(){
    @Override
    public void run() {
        while (true){
            try {
                int data = 0;
                if ((data=is.read())!=-1){
                    byte[] head = new byte[6];
                    head[0] = (byte) data;
                    is.read(head,1,5);
                    int length = Integer.parseInt((Integer.toHexString(head[4])).concat(Integer.toHexString(head[5])),16);
                    byte[] overData = new byte[length];
                    for (int i = 0; i < length; i++) {
                        overData[i] = (byte) is.read();
                    }
                    byte[] newData = new byte[head.length+overData.length];
                    System.arraycopy(head,0,newData,0,head.length);
                    System.arraycopy(overData,0,newData,head.length,overData.length);
                    System.out.println(ConvertUtils.addSpace(ConvertUtils.toHexString(newData)));
                }
            }catch (Exception e){

            }
        }
    }
}.start();

    }
}
