package com.visenergy.modbustcp;

import com.rabbitmq.client.*;
import com.visenergy.rabbitmq.RabbitMqUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import com.visenergy.utils.*;
/**
 * @Author WuSong
 * @Date 2017-08-22
 * @Time 13:54:24
 */
public class ModbusSwitchControl {
    private static Logger log = Logger.getLogger(ModbusSwitchControl.class);
    protected static Properties prop = new Properties();
    static {
        InputStream is = ModbusSwitchControl.class.getClassLoader().getResourceAsStream("registerAddress.properties");
        try {
//            InputStreamReader isr = new InputStreamReader(is,"UTF-8");
            prop.load(is);
        } catch (IOException e) {
            log.error("加载配置文件出错",e);
        }
    }

    /**
     * 九路开关控制
     */
    static {
        try {
            Connection conn  = RabbitMqUtils.newConnection();
            Channel channel = RabbitMqUtils.createRabbitMqChannel(conn);
            Consumer consumer = new DefaultConsumer(channel){
                String message = null;
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    message = new String(body,"UTF-8");
                    if (message.equals("FZ1_ON")){
                        String address = prop.getProperty("FZTR1");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("FZ1_OFF")) {
                        String address = prop.getProperty("FZQC1");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("FZ2_ON")) {
                        String address = prop.getProperty("FZTR2");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("FZ2_OFF")) {
                        String address = prop.getProperty("FZQC2");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("FZ3_ON")) {
                        /*String address = prop.getProperty("FZTR3");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));*/
                        String address = prop.getProperty("GZFW");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("FZ3_OFF")) {
                        /*String address = prop.getProperty("FZQC3");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));*/
                        String address = prop.getProperty("GZFW");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address,"","")));
                    } else if (message.equals("FZ4_ON")) {
                        /*String address = prop.getProperty("FZTR4");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));*/
                        String address = prop.getProperty("QDTZ");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("FZ4_OFF")) {
                        /*String address = prop.getProperty("FZQC4");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));*/
                        String address = prop.getProperty("QDTZ");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address,"","")));
                    } else if (message.equals("FZ5_ON")) {
                        String address = prop.getProperty("FZTR5");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("FZ5_OFF")) {
                        String address = prop.getProperty("FZQC5");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("FZ6_ON")) {
                        String address = prop.getProperty("FZTR6");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("FZ6_OFF")) {
                        String address = prop.getProperty("FZQC6");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("SD_ON")) {
                        String address = prop.getProperty("SDTR");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("SD_OFF")) {
                        String address = prop.getProperty("SDQC");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("GF_ON")) {
                        String address = prop.getProperty("GFTR");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("GF_OFF")) {
                        String address = prop.getProperty("GFQC");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("CN_ON")) {
                        String address = prop.getProperty("CNTR");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("CN_OFF")) {
                        String address = prop.getProperty("CNQC");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address)));
                    } else if (message.equals("BWZT")) {
                        String address = prop.getProperty("GZMSSZ");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address,message)));
                    } else if (message.equals("LWZT")) {
                        String address = prop.getProperty("GZMSSZ");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(generateCommand(address,message)));
                    }else {
                        log.error("开关及工作模式设置参数异常！");
                    }
                }
            };
            channel.basicConsume("SWITCH",true,consumer);
        } catch (TimeoutException e) {
            log.error("控制开关出现异常",e);
        } catch (IOException e) {
            log.error("控制开关出现异常",e);
        }
    }

    /**
     * 开关控制命令公共部分处理
     * @param val2 寄存器地址
     * @return
     */
    public static String generateCommand(String val2){
        int val = Integer.parseInt(val2);
        String address = Integer.toHexString(val).length() == 1 ? "0".concat(Integer.toHexString(val)) : Integer.toHexString(val);
        StringBuffer sbr = new StringBuffer(ModbusSendGenerate.TcpHead);
        sbr.append("01");
        sbr.append("06");
        sbr.append("00");
        sbr.append(address);
        sbr.append("00");
        sbr.append("01");
        return sbr.toString();
    }

    public static String generateCommand(String val2,String va,String bs){
        int val = Integer.parseInt(val2);
        String address = Integer.toHexString(val).length() == 1 ? "0".concat(Integer.toHexString(val)) : Integer.toHexString(val);
        StringBuffer sbr = new StringBuffer(ModbusSendGenerate.TcpHead);
        sbr.append("01");
        sbr.append("06");
        sbr.append("00");
        sbr.append(address);
        sbr.append("00");
        sbr.append("00");
        return sbr.toString();
    }

    public static String generateCommand(String val2,String workMode){
        int val = Integer.parseInt(val2);
        String address = Integer.toHexString(val).length() == 1 ? "0".concat(Integer.toHexString(val)) : Integer.toHexString(val);
        StringBuffer sbr = new StringBuffer(ModbusSendGenerate.TcpHead);
        sbr.append("01");
        sbr.append("06");
        sbr.append("00");
        sbr.append(address);
        sbr.append("00");
        if (workMode.equals("BWZT")){
            sbr.append("01");
        }else {
            sbr.append("05");
        }
        return sbr.toString();
    }
}
