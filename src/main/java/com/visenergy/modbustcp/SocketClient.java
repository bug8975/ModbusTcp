package com.visenergy.modbustcp;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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

    public SocketClient() throws IOException {
         socket = new Socket(remoteIp,port);
         is = socket.getInputStream();
         os = socket.getOutputStream();
         write();
         read();
    }

    public void register() throws IOException {

    }

    public void read() throws IOException {

        while (true){
            int data = 0;
            if ((data=is.read())!=-1){
                byte[] head = new byte[6];
                head[0] = (byte) data;
                is.read(head,1,5);
                int length = (int) head[5];
                byte[] overData = new byte[length];
                for (int i = 0; i < length; i++) {
                    overData[i] = (byte) is.read();
                }
                byte[] newData = new byte[head.length+overData.length];
                System.arraycopy(head,0,newData,0,head.length);
                System.arraycopy(overData,0,newData,head.length,overData.length);
                System.out.println(Utils.addSpace(Utils.toHexString(newData)));
                try {
                   // ModbusReceiveAnalysis.analysis(Utils.addSpace(Utils.toHexString(newData)));
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
                        os.write(Utils.hexStringToBytes(queryBefoer));
                        sleep(1000);
                        os.write(Utils.hexStringToBytes(queryMiddle));
                        sleep(1000);
                        os.write(Utils.hexStringToBytes(queryAfter));
                        System.out.println("send："+Utils.addSpace(queryBefoer));
                        System.out.println("send："+Utils.addSpace(queryMiddle));
                        System.out.println("send："+Utils.addSpace(queryAfter));
                        sleep(5000);
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
        }
    }

}
