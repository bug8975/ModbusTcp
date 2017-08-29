package com.visenergy.modbustcp;

import com.visenergy.utils.ConvertUtils;

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


            Socket socket = serverSocket.accept();
            is = socket.getInputStream();
            os = socket.getOutputStream();


        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    os.write(ConvertUtils.hexStringToBytes("00000000003901034E0267000100DD000000DC000000DD0000000000010001FFCBFFF803D90000000001F30000FFF800000001000500000002000000EA0000000000000000000100000000000000640000000300DC0000"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable,0,5, TimeUnit.SECONDS);

    }
}
