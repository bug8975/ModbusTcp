package com.visenergy.modbustcp;

import com.rabbitmq.client.*;
import com.visenergy.rabbitmq.RabbitMqUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author WuSong
 * @Date 2017-08-29
 * @Time 14:02:10
 */
public class RequestPowerCurve {
    private static Connection conn = null;
    private static Channel channel = null;
    private static Logger log = Logger.getLogger(RequestPowerCurve.class);

    public RequestPowerCurve() throws IOException, TimeoutException {
        RabbitMqUtils.reg(getChannel2(),"SWITCH_STATUS","");
        RabbitMqUtils.reg(getChannel2(),"SWITCH","");
        RabbitMqUtils.reg(getChannel2(),"REQUEST","");
    }
    static {
        try {
            Connection conn  = RabbitMqUtils.newConnection();
            Channel channel = RabbitMqUtils.createRabbitMqChannel(conn);
            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.add(0,ModbusReceiveAnalysis.arraySDGL);
                    jsonArray.add(1,ModbusReceiveAnalysis.arrayCNGL);
                    jsonArray.add(2,ModbusReceiveAnalysis.arrayGFGL);
                    jsonArray.add(3,ModbusReceiveAnalysis.arrayFZGL1);
                    jsonArray.add(4,ModbusReceiveAnalysis.arrayFZGL2);
                    jsonArray.add(5,ModbusReceiveAnalysis.arrayFZGL3);
                    jsonArray.add(6,ModbusReceiveAnalysis.arrayFZGL4);
                    jsonArray.add(7,ModbusReceiveAnalysis.arrayFZGL5);
                    jsonArray.add(8,ModbusReceiveAnalysis.arrayFZGL6);
                    try {
                        RabbitMqUtils.sendMq(getChannel2(),"POWER_ARRAY",jsonArray.toString());
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            };
            channel.basicConsume("REQUEST",true,consumer);
        } catch (IOException e) {
            log.error("初始化RabbitMQ失败",e);
        } catch (TimeoutException e) {
            log.error("初始化RabbitMQ失败",e);
        }

        try {
            Connection conn  = RabbitMqUtils.newConnection();
            Channel channel = RabbitMqUtils.createRabbitMqChannel(conn);
            Consumer consumer = new DefaultConsumer(channel){
                String message = null;
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    message = new String(body,"UTF-8");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("BLWZT",ModbusReceiveAnalysis.BLWZT);
                    try {
                        RabbitMqUtils.sendMq(getChannel2(),"SWITCH_STATUS_RETURN",jsonObject.toString());
                        log.debug(jsonObject.toString());
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            };
            channel.basicConsume("SWITCH_STATUS",true,consumer);
        } catch (IOException e) {
            log.error("初始化RabbitMQ失败",e);
        } catch (TimeoutException e) {
            log.error("初始化RabbitMQ失败",e);
        }
    }

    public static Channel getChannel2() {
        try {
            if(conn == null){
                conn = RabbitMqUtils.newConnection();
            }
            if(channel == null){
                channel = conn.createChannel();
            }
        } catch (IOException e) {
            log.error("初始化RabbitMQ失败",e);
        } catch (TimeoutException e) {
            log.error("初始化RabbitMQ失败",e);
        }
        return channel;
    }

    public static void main(String[] args) throws IOException, TimeoutException {

    }
}
