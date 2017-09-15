package com.visenergy.modbustcp;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.flying.jdbc.data.Parameter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.visenergy.rabbitmq.RabbitMqUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Author WuSong
 * @Date 2017-08-07
 * @Time 09:15:53
 */
public class ModbusReceiveAnalysis {
    private static Connection conn = null;
    private static Channel channel = null;
    private static Logger log = Logger.getLogger(ModbusReceiveAnalysis.class);
    private static DataHandle dataHandle = new DataHandle();
    protected static Integer[] arraySDGL = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    protected static Integer[] arrayCNGL = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    protected static Integer[] arrayGFGL = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    protected static Integer[] arrayFZGL1 = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    protected static Integer[] arrayFZGL2 = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    protected static Integer[] arrayFZGL3 = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    protected static Integer[] arrayFZGL4 = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    protected static Integer[] arrayFZGL5 = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    protected static Integer[] arrayFZGL6 = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    protected static int SDKG;//市电开关
    protected static int CNKG;//储能开关
    protected static int GFKG;//光伏开关
    protected static int FZKG1;//负载1开关
    protected static int FZKG2;//负载2开关
    protected static int FZKG3;//负载3开关
    protected static int FZKG4;//负载4开关
    protected static int FZKG5;//负载5开关
    protected static int FZKG6;//负载6开关
    protected static int BLWZT;//并离网状态


    /**
     * 数据解析
     *
     * @param data 传入命令
     * @throws Exception
     */
    public static void analysis(String data) throws IOException, TimeoutException {

        String[] answer = data.split(" ");
//        int lenth = new BigInteger(answer[5],16).intValue() <0 ? new BigInteger(answer[5],16).intValue()+256:new BigInteger(answer[5],16).intValue();
        int lenth = Integer.parseInt(answer[5], 16) < 0 ? Integer.parseInt(answer[5], 16) + 256 : Integer.parseInt(answer[5], 16);
        if (lenth == 57) {
            int DCU = Integer.parseInt(answer[9].concat(answer[10]), 16);
            // int DCI = answer[11].contains("FF") ? new BigInteger("FFFF".concat(answer[11].concat(answer[12])),16).intValue():Integer.parseInt(answer[11].concat(answer[12]),16);
            int DCI = (byte) Integer.parseInt(answer[11].concat(answer[12]), 16);
            int CNUA = Integer.parseInt(answer[13].concat(answer[14]), 16);
//            int CNIA = Integer.parseInt(answer[15].concat(answer[16]),16);
            int CNUB = Integer.parseInt(answer[17].concat(answer[18]), 16);
//            int CNIB = Integer.parseInt(answer[19].concat(answer[20]),16);
            int CNUC = Integer.parseInt(answer[21].concat(answer[22]), 16);
//            int CNIC = Integer.parseInt(answer[23].concat(answer[24]),16);
            log.debug("直流电压：" + DCU);
            log.debug("直流电流：" + DCI);

            int SDUA = Integer.parseInt(answer[25].concat(answer[26]), 16);
            int SDUB = Integer.parseInt(answer[27].concat(answer[28]), 16);
            int SDUC = Integer.parseInt(answer[29].concat(answer[30]), 16);
//            BigDecimal GLYS = new BigDecimal((byte)Integer.parseInt(answer[31].concat(answer[32]),16)).multiply(new BigDecimal("0.01"));
//            BigDecimal WGGL = new BigDecimal((byte)Integer.parseInt(answer[33].concat(answer[34]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal NBXL = new BigDecimal(Integer.parseInt(answer[35].concat(answer[36]), 16)).multiply(new BigDecimal("0.1"));
            BLWZT = Integer.parseInt(answer[39].concat(answer[40]), 16);
            BigDecimal SDPL = new BigDecimal(Integer.parseInt(answer[43].concat(answer[44]), 16)).multiply(new BigDecimal("0.1"));
//            BigDecimal YGGL = new BigDecimal(Integer.parseInt(answer[45].concat(answer[46]),16)).multiply(new BigDecimal("0.1"));
            int GZDM = Integer.parseInt(answer[47].concat(answer[48]), 16);
            int QTBZ = Integer.parseInt(answer[49].concat(answer[50]), 16);
            int GZMS = Integer.parseInt(answer[51].concat(answer[52]), 16);
            int ZCDL = Integer.parseInt(answer[53].concat(answer[54]).concat(answer[55]).concat(answer[56]), 16);
            int ZFDL = Integer.parseInt(answer[57].concat(answer[58]).concat(answer[59]).concat(answer[60]), 16);
            int SZDMS = Integer.parseInt(answer[61].concat(answer[62]), 16);
            log.debug("市电A相电压：" + SDUA);
            log.debug("市电B相电压：" + SDUB);
            log.debug("市电C相电压：" + SDUC);
            log.debug("储能A相电压：" + CNUA);
            log.debug("储能B相电压：" + CNUB);
            log.debug("储能C相电压：" + CNUC);
            log.debug("逆变效率：" + NBXL + "%");
            //log.debug("预留："+Integer.parseInt(answer[37].concat(answer[38]),16));
            log.debug("并离网状态：" + BLWZT);
            log.debug("逆变频率：" + new BigDecimal(Integer.parseInt(answer[41].concat(answer[42]), 16)).multiply(new BigDecimal("0.1")));
            log.debug("市电频率：" + SDPL);
            log.debug("故障代码：" + GZDM);
            log.debug("启停标志：" + QTBZ);
            log.debug("工作模式：" + GZMS);
            log.debug("总充电量：" + ZCDL + "kwh");
            log.debug("总放电量：" + ZFDL + "kwh");
            log.debug("手自动模式：" + SZDMS);

            Map map = new HashMap();
            map.put("DCU", DCU);
            map.put("DCI", DCI);
            map.put("SDUA", SDUA);
            map.put("SDUB", SDUB);
            map.put("SDUC", SDUC);
            map.put("SDPL", SDPL);
            map.put("NBXL", NBXL);
            map.put("BLWZT", BLWZT);
            map.put("GZDM", GZDM);
            map.put("QTBZ", QTBZ);
            map.put("GZMS", GZMS);
            map.put("ZCDL", ZCDL);
            map.put("ZFDL", ZFDL);
            map.put("SZDMS", SZDMS);
            dataHandle.setDCU(DCU);
            dataHandle.setDCI(DCI);
            dataHandle.setSDUA(SDUA);
            dataHandle.setSDUB(SDUB);
            dataHandle.setSDUC(SDUC);
            dataHandle.setSDPL(SDPL.doubleValue());
            dataHandle.setBLWZT(BLWZT);
            dataHandle.setGZDM(GZDM);
            dataHandle.setQTBZ(QTBZ);
            dataHandle.setGZMS(GZMS);
            dataHandle.setZCDL(ZCDL);
            dataHandle.setZFDL(ZFDL);
            dataHandle.setSZDMS(SZDMS);
            JSONObject jsonObject = JSONObject.fromObject(map);
            RabbitMqUtils.sendMq(getChannel(), "STATUS", jsonObject.toString());
        } else if (lenth == 93) {
            int SDGL = ((byte) Integer.parseInt(answer[45].concat(answer[46]), 16)) + ((byte) Integer.parseInt(answer[47].concat(answer[48]), 16)) + ((byte) Integer.parseInt(answer[49].concat(answer[50]), 16));
            int CNGL = ((byte) Integer.parseInt(answer[51].concat(answer[52]), 16)) + ((byte) Integer.parseInt(answer[53].concat(answer[54]), 16)) + ((byte) Integer.parseInt(answer[55].concat(answer[56]), 16));
            int GFGL = ((byte) Integer.parseInt(answer[57].concat(answer[58]), 16)) + ((byte) Integer.parseInt(answer[59].concat(answer[60]), 16)) + ((byte) Integer.parseInt(answer[61].concat(answer[62]), 16));
            int FZGL1 = ((byte) Integer.parseInt(answer[63].concat(answer[64]), 16)) + ((byte) Integer.parseInt(answer[65].concat(answer[66]), 16)) + ((byte) Integer.parseInt(answer[67].concat(answer[68]), 16));
            int FZGL2 = ((byte) Integer.parseInt(answer[69].concat(answer[70]), 16)) + ((byte) Integer.parseInt(answer[71].concat(answer[72]), 16)) + ((byte) Integer.parseInt(answer[73].concat(answer[74]), 16));
            int FZGL3 = ((byte) Integer.parseInt(answer[75].concat(answer[76]), 16)) + ((byte) Integer.parseInt(answer[77].concat(answer[78]), 16)) + ((byte) Integer.parseInt(answer[79].concat(answer[80]), 16));
            int FZGL4 = ((byte) Integer.parseInt(answer[81].concat(answer[82]), 16)) + ((byte) Integer.parseInt(answer[83].concat(answer[84]), 16)) + ((byte) Integer.parseInt(answer[85].concat(answer[86]), 16));
            int FZGL5 = ((byte) Integer.parseInt(answer[87].concat(answer[88]), 16)) + ((byte) Integer.parseInt(answer[89].concat(answer[90]), 16)) + ((byte) Integer.parseInt(answer[91].concat(answer[92]), 16));
            int FZGL6 = ((byte) Integer.parseInt(answer[93].concat(answer[94]), 16)) + ((byte) Integer.parseInt(answer[95].concat(answer[96]), 16)) + ((byte) Integer.parseInt(answer[97].concat(answer[98]), 16));

            for (int i = 0; i < arraySDGL.length - 1; i++) {
                arraySDGL[i] = arraySDGL[i + 1];
                arrayCNGL[i] = arrayCNGL[i + 1];
                arrayGFGL[i] = arrayGFGL[i + 1];
                arrayFZGL1[i] = arrayFZGL1[i + 1];
                arrayFZGL2[i] = arrayFZGL2[i + 1];
                arrayFZGL3[i] = arrayFZGL3[i + 1];
                arrayFZGL4[i] = arrayFZGL4[i + 1];
                arrayFZGL5[i] = arrayFZGL5[i + 1];
                arrayFZGL6[i] = arrayFZGL6[i + 1];
            }
            arraySDGL[arraySDGL.length - 1] = SDGL;
            arrayCNGL[arrayCNGL.length - 1] = CNGL;
            arrayGFGL[arrayGFGL.length - 1] = GFGL;
            arrayFZGL1[arrayFZGL1.length - 1] = FZGL1;
            arrayFZGL2[arrayFZGL2.length - 1] = FZGL2;
            arrayFZGL3[arrayFZGL3.length - 1] = FZGL3;
            arrayFZGL4[arrayFZGL4.length - 1] = FZGL4;
            arrayFZGL5[arrayFZGL5.length - 1] = FZGL5;
            arrayFZGL6[arrayFZGL6.length - 1] = FZGL6;

            SDKG = Integer.parseInt(answer[9].concat(answer[10]), 16);
            CNKG = Integer.parseInt(answer[13].concat(answer[14]), 16);
            GFKG = Integer.parseInt(answer[17].concat(answer[18]), 16);
            FZKG1 = Integer.parseInt(answer[21].concat(answer[22]), 16);
            FZKG2 = Integer.parseInt(answer[25].concat(answer[26]), 16);
            FZKG3 = Integer.parseInt(answer[29].concat(answer[30]), 16);
            FZKG4 = Integer.parseInt(answer[33].concat(answer[34]), 16);
            FZKG5 = Integer.parseInt(answer[37].concat(answer[38]), 16);
            FZKG6 = Integer.parseInt(answer[41].concat(answer[42]), 16);
            log.debug("市电投入状态：" + Integer.parseInt(answer[9].concat(answer[10]), 16));
            log.debug("储能投入状态：" + Integer.parseInt(answer[13].concat(answer[14]), 16));
            log.debug("光伏投入状态：" + Integer.parseInt(answer[17].concat(answer[18]), 16));
            log.debug("1负载投入状态：" + Integer.parseInt(answer[21].concat(answer[22]), 16));
            log.debug("2负载投入状态：" + Integer.parseInt(answer[25].concat(answer[26]), 16));
            log.debug("3负载投入状态：" + Integer.parseInt(answer[29].concat(answer[30]), 16));
            log.debug("4负载投入状态：" + Integer.parseInt(answer[33].concat(answer[34]), 16));
            log.debug("5负载投入状态：" + Integer.parseInt(answer[37].concat(answer[38]), 16));
            log.debug("6负载投入状态：" + Integer.parseInt(answer[41].concat(answer[42]), 16));
            log.debug("市电A相有功功率：" + (byte) Integer.parseInt(answer[45].concat(answer[46]), 16));
            log.debug("市电B相有功功率：" + (byte) Integer.parseInt(answer[47].concat(answer[48]), 16));
            log.debug("市电C相有功功率：" + (byte) Integer.parseInt(answer[49].concat(answer[50]), 16));
            log.debug("储能A相有功功率：" + (byte) Integer.parseInt(answer[51].concat(answer[52]), 16));
            log.debug("储能B相有功功率：" + (byte) Integer.parseInt(answer[53].concat(answer[54]), 16));
            log.debug("储能C相有功功率：" + (byte) Integer.parseInt(answer[55].concat(answer[56]), 16));
            log.debug("光伏A相有功功率：" + (byte) Integer.parseInt(answer[57].concat(answer[58]), 16));
            log.debug("光伏B相有功功率：" + (byte) Integer.parseInt(answer[59].concat(answer[60]), 16));
            log.debug("光伏C相有功功率：" + (byte) Integer.parseInt(answer[61].concat(answer[62]), 16));
            log.debug("一负载A相有功功率：" + (byte) Integer.parseInt(answer[63].concat(answer[64]), 16));
            log.debug("一负载B相有功功率：" + (byte) Integer.parseInt(answer[65].concat(answer[66]), 16));
            log.debug("一负载C相有功功率：" + (byte) Integer.parseInt(answer[67].concat(answer[68]), 16));
            log.debug("二负载A相有功功率：" + (byte) Integer.parseInt(answer[69].concat(answer[70]), 16));
            log.debug("二负载B相有功功率：" + (byte) Integer.parseInt(answer[71].concat(answer[72]), 16));
            log.debug("二负载C相有功功率：" + (byte) Integer.parseInt(answer[73].concat(answer[74]), 16));
            log.debug("三负载A相有功功率：" + (byte) Integer.parseInt(answer[75].concat(answer[76]), 16));
            log.debug("三负载B相有功功率：" + (byte) Integer.parseInt(answer[77].concat(answer[78]), 16));
            log.debug("三负载C相有功功率：" + (byte) Integer.parseInt(answer[79].concat(answer[80]), 16));
            log.debug("四负载A相有功功率：" + (byte) Integer.parseInt(answer[81].concat(answer[82]), 16));
            log.debug("四负载B相有功功率：" + (byte) Integer.parseInt(answer[83].concat(answer[84]), 16));
            log.debug("四负载B相有功功率：" + (byte) Integer.parseInt(answer[85].concat(answer[86]), 16));
            log.debug("五负载A相有功功率：" + (byte) Integer.parseInt(answer[87].concat(answer[88]), 16));
            log.debug("五负载B相有功功率：" + (byte) Integer.parseInt(answer[89].concat(answer[90]), 16));
            log.debug("五负载C相有功功率：" + (byte) Integer.parseInt(answer[91].concat(answer[92]), 16));
            log.debug("六负载A相有功功率：" + (byte) Integer.parseInt(answer[93].concat(answer[94]), 16));
            log.debug("六负载B相有功功率：" + (byte) Integer.parseInt(answer[95].concat(answer[96]), 16));
            log.debug("六负载C相有功功率：" + (byte) Integer.parseInt(answer[97].concat(answer[98]), 16));
            Map map = new HashMap();
            map.put("SDKG", SDKG);
            map.put("CNKG", CNKG);
            map.put("GFKG", GFKG);
            map.put("FZKG1", FZKG1);
            map.put("FZKG2", FZKG2);
            map.put("FZKG3", FZKG3);
            map.put("FZKG4", FZKG4);
            map.put("FZKG5", FZKG5);
            map.put("FZKG6", FZKG6);
            map.put("SDGL", SDGL);
            map.put("CNGL", CNGL);
            map.put("GFGL", GFGL);
            map.put("FZGL1", FZGL1);
            map.put("FZGL2", FZGL2);
            map.put("FZGL3", FZGL3);
            map.put("FZGL4", FZGL4);
            map.put("FZGL5", FZGL5);
            map.put("FZGL6", FZGL6);
            dataHandle.setSDGL(SDGL);
            dataHandle.setCNGL(CNGL);
            dataHandle.setGFGL(GFGL);
            dataHandle.setFZGL1(FZGL1);
            dataHandle.setFZGL2(FZGL2);
            dataHandle.setFZGL3(FZGL3);
            dataHandle.setFZGL4(FZGL4);
            dataHandle.setFZGL5(FZGL5);
            dataHandle.setFZGL6(FZGL6);
            JSONObject jsonObject = JSONObject.fromObject(map);
            RabbitMqUtils.sendMq(getChannel(), "POWER", jsonObject.toString());
        } else if (lenth == 207) {
            BigDecimal GFUA = new BigDecimal(Integer.parseInt(answer[9].concat(answer[10]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal GFUB = new BigDecimal(Integer.parseInt(answer[11].concat(answer[12]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal GFUC = new BigDecimal(Integer.parseInt(answer[13].concat(answer[14]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUA1 = new BigDecimal(Integer.parseInt(answer[15].concat(answer[16]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUB1 = new BigDecimal(Integer.parseInt(answer[17].concat(answer[18]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUC1 = new BigDecimal(Integer.parseInt(answer[19].concat(answer[20]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUA2 = new BigDecimal(Integer.parseInt(answer[21].concat(answer[22]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUB2 = new BigDecimal(Integer.parseInt(answer[23].concat(answer[24]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUC2 = new BigDecimal(Integer.parseInt(answer[25].concat(answer[26]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUA3 = new BigDecimal(Integer.parseInt(answer[27].concat(answer[28]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUB3 = new BigDecimal(Integer.parseInt(answer[29].concat(answer[30]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUC3 = new BigDecimal(Integer.parseInt(answer[31].concat(answer[32]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUA4 = new BigDecimal(Integer.parseInt(answer[33].concat(answer[34]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUB4 = new BigDecimal(Integer.parseInt(answer[35].concat(answer[36]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUC4 = new BigDecimal(Integer.parseInt(answer[37].concat(answer[38]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUA5 = new BigDecimal(Integer.parseInt(answer[39].concat(answer[40]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUB5 = new BigDecimal(Integer.parseInt(answer[41].concat(answer[42]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUC5 = new BigDecimal(Integer.parseInt(answer[43].concat(answer[44]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUA6 = new BigDecimal(Integer.parseInt(answer[45].concat(answer[46]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUB6 = new BigDecimal(Integer.parseInt(answer[47].concat(answer[48]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUC6 = new BigDecimal(Integer.parseInt(answer[49].concat(answer[50]), 16)).multiply(new BigDecimal("0.1"));
            BigDecimal SDIA = new BigDecimal(Integer.parseInt(answer[51].concat(answer[52]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal SDIB = new BigDecimal(Integer.parseInt(answer[53].concat(answer[54]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal SDIC = new BigDecimal(Integer.parseInt(answer[55].concat(answer[56]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal CNIA = new BigDecimal(Integer.parseInt(answer[57].concat(answer[58]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal CNIB = new BigDecimal(Integer.parseInt(answer[59].concat(answer[60]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal CNIC = new BigDecimal(Integer.parseInt(answer[61].concat(answer[62]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal GFIA = new BigDecimal(Integer.parseInt(answer[63].concat(answer[64]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal GFIB = new BigDecimal(Integer.parseInt(answer[65].concat(answer[66]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal GFIC = new BigDecimal(Integer.parseInt(answer[67].concat(answer[68]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIA1 = new BigDecimal(Integer.parseInt(answer[69].concat(answer[70]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIB1 = new BigDecimal(Integer.parseInt(answer[71].concat(answer[72]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIC1 = new BigDecimal(Integer.parseInt(answer[73].concat(answer[74]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIA2 = new BigDecimal(Integer.parseInt(answer[75].concat(answer[76]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIB2 = new BigDecimal(Integer.parseInt(answer[77].concat(answer[78]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIC2 = new BigDecimal(Integer.parseInt(answer[79].concat(answer[80]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIA3 = new BigDecimal(Integer.parseInt(answer[81].concat(answer[82]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIB3 = new BigDecimal(Integer.parseInt(answer[83].concat(answer[84]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIC3 = new BigDecimal(Integer.parseInt(answer[85].concat(answer[86]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIA4 = new BigDecimal(Integer.parseInt(answer[87].concat(answer[88]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIB4 = new BigDecimal(Integer.parseInt(answer[89].concat(answer[90]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIC4 = new BigDecimal(Integer.parseInt(answer[91].concat(answer[92]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIA5 = new BigDecimal(Integer.parseInt(answer[93].concat(answer[94]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIB5 = new BigDecimal(Integer.parseInt(answer[95].concat(answer[96]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIC5 = new BigDecimal(Integer.parseInt(answer[97].concat(answer[98]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIA6 = new BigDecimal(Integer.parseInt(answer[99].concat(answer[100]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIB6 = new BigDecimal(Integer.parseInt(answer[101].concat(answer[102]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZIC6 = new BigDecimal(Integer.parseInt(answer[103].concat(answer[104]), 16)).divide(new BigDecimal(25), 2, BigDecimal.ROUND_HALF_UP);
            log.debug("光伏A相电压：" + GFUA);
            log.debug("光伏B相电压：" + GFUB);
            log.debug("光伏C相电压：" + GFUC);
            log.debug("一负载A相电压：" + FZUA1);
            log.debug("一负载B相电压：" + FZUB1);
            log.debug("一负载C相电压：" + FZUC1);
            log.debug("二负载A相电压：" + FZUA2);
            log.debug("二负载B相电压：" + FZUB2);
            log.debug("二负载C相电压：" + FZUC2);
            log.debug("三负载A相电压：" + FZUA3);
            log.debug("三负载B相电压：" + FZUB3);
            log.debug("三负载C相电压：" + FZUC3);
            log.debug("四负载A相电压：" + FZUA4);
            log.debug("四负载B相电压：" + FZUB4);
            log.debug("四负载C相电压：" + FZUC4);
            log.debug("五负载A相电压：" + FZUA5);
            log.debug("五负载B相电压：" + FZUB5);
            log.debug("五负载C相电压：" + FZUC5);
            log.debug("六负载A相电压：" + FZUA6);
            log.debug("六负载B相电压：" + FZUB6);
            log.debug("六负载C相电压：" + FZUC6);
            log.debug("市电A相电流：" + SDIA);
            log.debug("市电B相电流：" + SDIB);
            log.debug("市电C相电流：" + SDIC);
            log.debug("储能A相电流：" + CNIA);
            log.debug("储能B相电流：" + CNIB);
            log.debug("储能C相电流：" + CNIC);
            log.debug("光伏A相电流：" + GFIA);
            log.debug("光伏B相电流：" + GFIB);
            log.debug("光伏C相电流：" + GFIC);
            log.debug("一负载A相电流：" + FZIA1);
            log.debug("一负载B相电流：" + FZIB1);
            log.debug("一负载C相电流：" + FZIC1);
            log.debug("二负载A相电流：" + FZIA2);
            log.debug("二负载B相电流：" + FZIB2);
            log.debug("二负载C相电流：" + FZIC2);
            log.debug("三负载A相电流：" + FZIA3);
            log.debug("三负载B相电流：" + FZIB3);
            log.debug("三负载C相电流：" + FZIC3);
            log.debug("四负载A相电流：" + FZIA4);
            log.debug("四负载B相电流：" + FZIB4);
            log.debug("四负载C相电流：" + FZIC4);
            log.debug("五负载A相电流：" + FZIA5);
            log.debug("五负载B相电流：" + FZIB5);
            log.debug("五负载C相电流：" + FZIC5);
            log.debug("六负载A相电流：" + FZIA6);
            log.debug("六负载B相电流：" + FZIB6);
            log.debug("六负载C相电流：" + FZIC6);

            BigDecimal SDWG = new BigDecimal((byte) Integer.parseInt(answer[105].concat(answer[106]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP);
            BigDecimal CNWG = new BigDecimal((byte) Integer.parseInt(answer[111].concat(answer[112]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP);
            BigDecimal GFWG = new BigDecimal((byte) Integer.parseInt(answer[117].concat(answer[118]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZWG1 = new BigDecimal((byte) Integer.parseInt(answer[123].concat(answer[124]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZWG2 = new BigDecimal((byte) Integer.parseInt(answer[129].concat(answer[130]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZWG3 = new BigDecimal((byte) Integer.parseInt(answer[135].concat(answer[136]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZWG4 = new BigDecimal((byte) Integer.parseInt(answer[141].concat(answer[142]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZWG5 = new BigDecimal((byte) Integer.parseInt(answer[147].concat(answer[148]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP);
            BigDecimal FZWG6 = new BigDecimal((byte) Integer.parseInt(answer[153].concat(answer[154]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP);
            log.debug("市电A相无功：" + new BigDecimal((byte) Integer.parseInt(answer[105].concat(answer[106]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("市电B相无功：" + new BigDecimal((byte) Integer.parseInt(answer[107].concat(answer[108]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("市电C相无功：" + new BigDecimal((byte) Integer.parseInt(answer[109].concat(answer[110]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("储能A相无功：" + new BigDecimal((byte) Integer.parseInt(answer[111].concat(answer[112]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("储能B相无功：" + new BigDecimal((byte) Integer.parseInt(answer[113].concat(answer[114]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("储能C相无功：" + new BigDecimal((byte) Integer.parseInt(answer[115].concat(answer[116]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("光伏A相无功：" + new BigDecimal((byte) Integer.parseInt(answer[117].concat(answer[118]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("光伏B相无功：" + new BigDecimal((byte) Integer.parseInt(answer[119].concat(answer[120]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("光伏C相无功：" + new BigDecimal((byte) Integer.parseInt(answer[121].concat(answer[122]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("一负载A相无功：" + new BigDecimal((byte) Integer.parseInt(answer[123].concat(answer[124]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("一负载B相无功：" + new BigDecimal((byte) Integer.parseInt(answer[125].concat(answer[126]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("一负载C相无功：" + new BigDecimal((byte) Integer.parseInt(answer[127].concat(answer[128]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("二负载A相无功：" + new BigDecimal((byte) Integer.parseInt(answer[129].concat(answer[130]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("二负载B相无功：" + new BigDecimal((byte) Integer.parseInt(answer[131].concat(answer[132]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("二负载C相无功：" + new BigDecimal((byte) Integer.parseInt(answer[133].concat(answer[134]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("三负载A相无功：" + new BigDecimal((byte) Integer.parseInt(answer[135].concat(answer[136]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("三负载B相无功：" + new BigDecimal((byte) Integer.parseInt(answer[137].concat(answer[138]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("三负载C相无功：" + new BigDecimal((byte) Integer.parseInt(answer[139].concat(answer[140]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("四负载A相无功：" + new BigDecimal((byte) Integer.parseInt(answer[141].concat(answer[142]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("四负载B相无功：" + new BigDecimal((byte) Integer.parseInt(answer[143].concat(answer[144]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("四负载C相无功：" + new BigDecimal((byte) Integer.parseInt(answer[145].concat(answer[146]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("五负载A相无功：" + new BigDecimal((byte) Integer.parseInt(answer[147].concat(answer[148]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("五负载B相无功：" + new BigDecimal((byte) Integer.parseInt(answer[149].concat(answer[150]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("五负载C相无功：" + new BigDecimal((byte) Integer.parseInt(answer[151].concat(answer[152]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("六负载A相无功：" + new BigDecimal((byte) Integer.parseInt(answer[153].concat(answer[154]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("六负载B相无功：" + new BigDecimal((byte) Integer.parseInt(answer[155].concat(answer[156]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));
            log.debug("六负载C相无功：" + new BigDecimal((byte) Integer.parseInt(answer[157].concat(answer[158]), 16)).divide(new BigDecimal(25), 1, BigDecimal.ROUND_HALF_UP));

            BigDecimal SDYS = new BigDecimal(Convert(answer[159], (answer[160]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN);
            BigDecimal CNYS = new BigDecimal(Convert(answer[165], (answer[166]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN);
            BigDecimal GFYS = new BigDecimal(Convert(answer[171], (answer[172]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN);
            BigDecimal FZYS1 = new BigDecimal(Convert(answer[177], (answer[178]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN);
            BigDecimal FZYS2 = new BigDecimal(Convert(answer[183], (answer[184]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN);
            BigDecimal FZYS3 = new BigDecimal(Convert(answer[189], (answer[190]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN);
            BigDecimal FZYS4 = new BigDecimal(Convert(answer[195], (answer[196]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN);
            BigDecimal FZYS5 = new BigDecimal(Convert(answer[201], (answer[202]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN);
            BigDecimal FZYS6 = new BigDecimal(Convert(answer[207], (answer[208]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN);
            log.debug("市电A相功率因素：" + new BigDecimal(Convert(answer[159], (answer[160]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("市电B相功率因素：" + new BigDecimal(Convert(answer[161], (answer[162]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("市电C相功率因素：" + new BigDecimal(Convert(answer[163], (answer[164]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("储能A相功率因素：" + new BigDecimal(Convert(answer[165], (answer[166]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("储能B相功率因素：" + new BigDecimal(Convert(answer[167], (answer[168]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("储能C相功率因素：" + new BigDecimal(Convert(answer[169], (answer[170]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("光伏A相功率因素：" + new BigDecimal(Convert(answer[171], (answer[172]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("光伏B相功率因素：" + new BigDecimal(Convert(answer[173], (answer[174]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("光伏C相功率因素：" + new BigDecimal(Convert(answer[175], (answer[176]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("一负载A相功率因素：" + new BigDecimal(Convert(answer[177], (answer[178]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("一负载B相功率因素：" + new BigDecimal(Convert(answer[179], (answer[180]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("一负载C相功率因素：" + new BigDecimal(Convert(answer[181], (answer[182]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("二负载A相功率因素：" + new BigDecimal(Convert(answer[183], (answer[184]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("二负载B相功率因素：" + new BigDecimal(Convert(answer[185], (answer[186]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("二负载C相功率因素：" + new BigDecimal(Convert(answer[187], (answer[188]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("三负载A相功率因素：" + new BigDecimal(Convert(answer[189], (answer[190]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("三负载B相功率因素：" + new BigDecimal(Convert(answer[191], (answer[192]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("三负载C相功率因素：" + new BigDecimal(Convert(answer[193], (answer[194]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("四负载A相功率因素：" + new BigDecimal(Convert(answer[195], (answer[196]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("四负载B相功率因素：" + new BigDecimal(Convert(answer[197], (answer[198]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("四负载C相功率因素：" + new BigDecimal(Convert(answer[199], (answer[200]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("五负载A相功率因素：" + new BigDecimal(Convert(answer[201], (answer[202]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("五负载B相功率因素：" + new BigDecimal(Convert(answer[203], (answer[204]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("五负载C相功率因素：" + new BigDecimal(Convert(answer[205], (answer[206]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("六负载A相功率因素：" + new BigDecimal(Convert(answer[207], (answer[208]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("六负载B相功率因素：" + new BigDecimal(Convert(answer[209], (answer[210]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));
            log.debug("六负载C相功率因素：" + new BigDecimal(Convert(answer[211], (answer[212]))).multiply(new BigDecimal("0.0001")).setScale(2, BigDecimal.ROUND_DOWN));

            Map map = new HashMap();
            map.put("SDIA", SDIA);
            map.put("SDIB", SDIB);
            map.put("SDIC", SDIC);
            map.put("CNIA", CNIA);
            map.put("CNIB", CNIB);
            map.put("CNIC", CNIC);
            map.put("GFIA", GFIA);
            map.put("GFIB", GFIB);
            map.put("GFIC", GFIC);
            map.put("FZIA1", FZIA1);
            map.put("FZIB1", FZIB1);
            map.put("FZIC1", FZIC1);
            map.put("FZIA2", FZIA2);
            map.put("FZIB2", FZIB2);
            map.put("FZIC2", FZIC2);
            map.put("FZIA3", FZIA3);
            map.put("FZIB3", FZIB3);
            map.put("FZIC3", FZIC3);
            map.put("FZIA4", FZIA4);
            map.put("FZIB4", FZIB4);
            map.put("FZIC4", FZIC4);
            map.put("FZIA5", FZIA5);
            map.put("FZIB5", FZIB5);
            map.put("FZIC5", FZIC5);
            map.put("FZIA6", FZIA6);
            map.put("FZIB6", FZIB6);
            map.put("FZIC6", FZIC6);
            dataHandle.setSDIA(SDIA.doubleValue());
            dataHandle.setCNIA(CNIA.doubleValue());
            dataHandle.setGFIA(GFIA.doubleValue());
            dataHandle.setFZIA1(FZIA1.doubleValue());
            dataHandle.setFZIA2(FZIA2.doubleValue());
            dataHandle.setFZIA3(FZIA3.doubleValue());
            dataHandle.setFZIA4(FZIA4.doubleValue());
            dataHandle.setFZIA5(FZIA5.doubleValue());
            dataHandle.setFZIA6(FZIA6.doubleValue());
            dataHandle.setSDWG(SDWG.doubleValue());
            dataHandle.setCNWG(CNWG.doubleValue());
            dataHandle.setGFWG(GFWG.doubleValue());
            dataHandle.setFZWG1(FZWG1.doubleValue());
            dataHandle.setFZWG2(FZWG2.doubleValue());
            dataHandle.setFZWG3(FZWG3.doubleValue());
            dataHandle.setFZWG4(FZWG4.doubleValue());
            dataHandle.setFZWG5(FZWG5.doubleValue());
            dataHandle.setFZWG6(FZWG6.doubleValue());
            dataHandle.setSDYS(SDYS.doubleValue());
            dataHandle.setCNYS(CNYS.doubleValue());
            dataHandle.setGFYS(GFYS.doubleValue());
            dataHandle.setFZYS1(FZYS1.doubleValue());
            dataHandle.setFZYS2(FZYS2.doubleValue());
            dataHandle.setFZYS3(FZYS3.doubleValue());
            dataHandle.setFZYS4(FZYS4.doubleValue());
            dataHandle.setFZYS5(FZYS5.doubleValue());
            dataHandle.setFZYS6(FZYS6.doubleValue());
            map.put("SDWG", SDWG);
            map.put("CNWG", CNWG);
            map.put("GFWG", GFWG);
            map.put("FZWG1", FZWG1);
            map.put("FZWG2", FZWG2);
            map.put("FZWG3", FZWG3);
            map.put("FZWG4", FZWG4);
            map.put("FZWG5", FZWG5);
            map.put("FZWG6", FZWG6);
            map.put("SDYS", SDYS);
            map.put("CNYS", CNYS);
            map.put("GFYS", GFYS);
            map.put("FZYS1", FZYS1);
            map.put("FZYS2", FZYS2);
            map.put("FZYS3", FZYS3);
            map.put("FZYS4", FZYS4);
            map.put("FZYS5", FZYS5);
            map.put("FZYS6", FZYS6);
            JSONObject jsonObject = JSONObject.fromObject(map);
            RabbitMqUtils.sendMq(getChannel(), "VOLTAGE_CURRENT", jsonObject.toString());
        } else if (lenth == 6) {
            RabbitMqUtils.sendMq(getChannel(), "SWITCH_RETURN", "SUCCESS");
        } else {
            log.error("返回的数据格式不对，无法解析!");
        }

    }

    public static String Convert(String var1, String var2) {
        int highByte = Integer.parseInt(var1, 16);
        int lowByte = Integer.parseInt(var2, 16);
        String finalHighByte;
        String finalLowByte;
        String result = String.valueOf(Integer.parseInt(var1.concat(var2), 16));
        if (highByte > 127 || lowByte > 127) {
            if (highByte > 127 && lowByte > 127) {
                finalHighByte = Integer.toHexString(256 - highByte);
                finalLowByte = Integer.toHexString(256 - lowByte);
                result = "-".concat(String.valueOf(Integer.parseInt(finalHighByte.concat(finalLowByte), 16)));
            } else if (highByte > 127) {
                finalHighByte = Integer.toHexString(256 - highByte);
                result = "-".concat(String.valueOf(Integer.parseInt(finalHighByte.concat(var2), 16)));
            } else {
                finalLowByte = Integer.toHexString(256 - lowByte);
                result = "-".concat(String.valueOf(Integer.parseInt(var1.concat(finalLowByte), 16)));
            }
            return result;
        }
        return result;
    }

    public static Channel getChannel() {
        try {
            if (conn == null) {
                conn = RabbitMqUtils.newConnection();
            }
            if (channel == null) {
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
