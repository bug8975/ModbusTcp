package com.visenergy.modbustcp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.visenergy.rabbitmq.RabbitMqUtils;
import net.sf.json.JSONArray;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author WuSong
 * @Date 2017-08-29
 * @Time 14:02:10
 */
public class RequestPowerCurve {
    private  Connection conn = null;
    private  Channel channel = null;
    public RequestPowerCurve(){

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
            RabbitMqUtils.sendMq(getChannel(),"POWER",jsonArray.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    public  Channel getChannel() {
        try {
            if(conn == null){
                conn = RabbitMqUtils.newConnection();
            }
            if(channel == null){
                channel = conn.createChannel();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return channel;
    }
}
