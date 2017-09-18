package com.visenergy.modbustcp;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeoutException;

import com.visenergy.utils.*;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @Author WuSong
 * @Date 2017-08-04
 * @Time 15:05:13
 */
public class SocketClient {
    private final String remoteIp = "192.168.100.104";
    private final int port = 3000;
    Socket socket = null;
    static InputStream is = null;
    static OutputStream os = null;
    private static Logger log = Logger.getLogger(SocketClient.class);
    public SocketClient() throws IOException, TimeoutException {

        new ModbusSwitchControl();
        new RequestPowerCurve();
         socket = new Socket(remoteIp,port);
         is = socket.getInputStream();
         os = socket.getOutputStream();
         write();
         read();

    }



    public void read() throws IOException {

        while (true){
            int data = 0;
            if ((data=is.read())!=-1){
                byte[] head = new byte[6];
                head[0] = (byte) data;
                is.read(head,1,5);
//                int length = Integer.parseInt((Integer.toHexString(head[4])).concat(Integer.toHexString(head[5])),16);
                int length = head[5] < 0 ? head[5] + 256 : head[5];
                System.out.println(length);
                byte[] overData = new byte[length];
                for (int i = 0; i < length; i++) {
                    overData[i] = (byte) is.read();
                }
                byte[] newData = new byte[head.length+overData.length];
                System.arraycopy(head,0,newData,0,head.length);
                System.arraycopy(overData,0,newData,head.length,overData.length);
                System.out.println(ConvertUtils.addSpace(ConvertUtils.toHexString(newData)));

                try {
                    ModbusReceiveAnalysis.analysis(ConvertUtils.addSpace(ConvertUtils.toHexString(newData)));
                    log.debug("开始解析");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void write(){
        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        String queryBefoer = ModbusSendGenerate.queryBefoer();
                        String queryMiddle = ModbusSendGenerate.queryMiddle();
                        String queryAfter = ModbusSendGenerate.queryAfter();
                        os.write(ConvertUtils.hexStringToBytes(queryBefoer));
                        sleep(1000);
                        os.write(ConvertUtils.hexStringToBytes(queryMiddle));
                        sleep(1000);
                        os.write(ConvertUtils.hexStringToBytes(queryAfter));
                        System.out.println("send："+ConvertUtils.addSpace(queryBefoer));
                        System.out.println("send："+ConvertUtils.addSpace(queryMiddle));
                        System.out.println("send："+ConvertUtils.addSpace(queryAfter));
                        sleep(8000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();
    }

    public static void main(String[] args) {
        try {
            new SocketClient();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
