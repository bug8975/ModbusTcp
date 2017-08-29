package com.visenergy.modbustcp;

import com.rabbitmq.client.*;
import com.visenergy.rabbitmq.RabbitMqUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
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
            //InputStreamReader isr = new InputStreamReader(is,"UTF-8");
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
                        String address = prop.getProperty("一路负载投入");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("FZ1_OFF")) {
                        String address = prop.getProperty("一路负载切除");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("FZ2_ON")) {
                        String address = prop.getProperty("二路负载投入");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("FZ2_OFF")) {
                        String address = prop.getProperty("二路负载切除");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("FZ3_ON")) {
                        String address = prop.getProperty("三路负载投入");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("FZ3_OFF")) {
                        String address = prop.getProperty("三路负载切除");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("FZ4_ON")) {
                        String address = prop.getProperty("四路负载投入");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("FZ4_OFF")) {
                        String address = prop.getProperty("四路负载切除");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("FZ5_ON")) {
                        String address = prop.getProperty("五路负载投入");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("FZ5_OFF")) {
                        String address = prop.getProperty("五路负载切除");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("FZ6_ON")) {
                        String address = prop.getProperty("六路负载投入");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("FZ6_OFF")) {
                        String address = prop.getProperty("六路负载切除");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("SD_ON")) {
                        String address = prop.getProperty("市电投入");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("SD_OFF")) {
                        String address = prop.getProperty("市电切除");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("GF_ON")) {
                        String address = prop.getProperty("光伏投入");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("GF_OFF")) {
                        String address = prop.getProperty("光伏切除");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("CN_ON")) {
                        String address = prop.getProperty("储能投入");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else if (message.equals("CN_OFF")) {
                        String address = prop.getProperty("储能切除");
                        SocketClient.os.write(ConvertUtils.hexStringToBytes(GenerateCommand(address)));
                    } else {
                        log.error("开关参数异常！");
                    }
                }
            };
            channel.basicConsume("SWITCH",true,consumer);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开关控制命令公共部分处理
     * @param val2 寄存器地址
     * @return
     */
    public static String GenerateCommand(String val2){
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
}
