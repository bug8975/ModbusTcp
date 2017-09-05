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
    private static  DataHandle dataHandle = new DataHandle();
    protected static Integer[] arraySDGL = new Integer[]{0,0,0,0,0,0,0,0,0,0};
    protected static Integer[] arrayCNGL = new Integer[]{0,0,0,0,0,0,0,0,0,0};
    protected static Integer[] arrayGFGL = new Integer[]{0,0,0,0,0,0,0,0,0,0};
    protected static Integer[] arrayFZGL1 = new Integer[]{0,0,0,0,0,0,0,0,0,0};
    protected static Integer[] arrayFZGL2 = new Integer[]{0,0,0,0,0,0,0,0,0,0};
    protected static Integer[] arrayFZGL3 = new Integer[]{0,0,0,0,0,0,0,0,0,0};
    protected static Integer[] arrayFZGL4 = new Integer[]{0,0,0,0,0,0,0,0,0,0};
    protected static Integer[] arrayFZGL5 = new Integer[]{0,0,0,0,0,0,0,0,0,0};
    protected static Integer[] arrayFZGL6 = new Integer[]{0,0,0,0,0,0,0,0,0,0};



    /**
     * 数据解析
     * @param received 传入命令
     * @throws Exception
     */
    public static void analysis(String received) throws IOException, TimeoutException {

        String[] answer = received.split(" ");
        int lenth = Integer.parseInt(answer[5],16);
        if ( lenth == 57){
            int DCU = Integer.parseInt(answer[9].concat(answer[10]),16);
           // int DCI = answer[11].contains("FF") ? new BigInteger("FFFF".concat(answer[11].concat(answer[12])),16).intValue():Integer.parseInt(answer[11].concat(answer[12]),16);
            int DCI = (byte)Integer.parseInt(answer[11].concat(answer[12]),16);
//            int CNUA = Integer.parseInt(answer[13].concat(answer[14]),16);
//            int CNIA = Integer.parseInt(answer[15].concat(answer[16]),16);
//            int CNUB = Integer.parseInt(answer[17].concat(answer[18]),16);
//            int CNIB = Integer.parseInt(answer[19].concat(answer[20]),16);
//            int CNUC = Integer.parseInt(answer[21].concat(answer[22]),16);
//            int CNIC = Integer.parseInt(answer[23].concat(answer[24]),16);
            log.debug("直流电压："+DCU);
            log.debug("直流电流："+DCI);

            int SDUA = Integer.parseInt(answer[25].concat(answer[26]),16);
            int SDUB = Integer.parseInt(answer[27].concat(answer[28]),16);
            int SDUC = Integer.parseInt(answer[29].concat(answer[30]),16);
//            BigDecimal GLYS = new BigDecimal((byte)Integer.parseInt(answer[31].concat(answer[32]),16)).multiply(new BigDecimal("0.01"));
//            BigDecimal WGGL = new BigDecimal((byte)Integer.parseInt(answer[33].concat(answer[34]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal NBXL = new BigDecimal(Integer.parseInt(answer[35].concat(answer[36]),16)).multiply(new BigDecimal("0.1"));
            int BLWZT = Integer.parseInt(answer[39].concat(answer[40]),16);
            BigDecimal SDPL = new BigDecimal(Integer.parseInt(answer[43].concat(answer[44]),16)).multiply(new BigDecimal("0.1"));
//            BigDecimal YGGL = new BigDecimal(Integer.parseInt(answer[45].concat(answer[46]),16)).multiply(new BigDecimal("0.1"));
            int GZDM = Integer.parseInt(answer[47].concat(answer[48]),16);
            int QTBZ = Integer.parseInt(answer[49].concat(answer[50]),16);
            int GZMS = Integer.parseInt(answer[51].concat(answer[52]),16);
            int ZCDL = Integer.parseInt(answer[53].concat(answer[54]).concat(answer[55]).concat(answer[56]),16);
            int ZFDL = Integer.parseInt(answer[57].concat(answer[58]).concat(answer[59]).concat(answer[60]),16);
            int SZDMS = Integer.parseInt(answer[61].concat(answer[62]),16);
            log.debug("市电A相电压："+SDUA);
            log.debug("市电B相电压："+SDUB);
            log.debug("市电C相电压："+SDUC);
            log.debug("逆变效率："+NBXL+"%");
            //log.debug("预留："+Integer.parseInt(answer[37].concat(answer[38]),16));
            log.debug("并离网状态："+BLWZT);
            log.debug("逆变频率："+new BigDecimal(Integer.parseInt(answer[41].concat(answer[42]),16)).multiply(new BigDecimal("0.1")));
            log.debug("市电频率："+SDPL);
            log.debug("故障代码："+GZDM);
            log.debug("启停标志："+QTBZ);
            log.debug("工作模式："+GZMS);
            log.debug("总充电量："+ZCDL+"kwh");
            log.debug("总放电量："+ZFDL+"kwh");
            log.debug("手自动模式："+SZDMS);

            Map map = new HashMap();
            map.put("DCU",DCU);
            map.put("DCI",DCI);
            map.put("SDUA",SDUA);
            map.put("SDUB",SDUB);
            map.put("SDUC",SDUC);
            map.put("SDPL",SDPL);
            map.put("NBXL",NBXL);
            map.put("BLWZT",BLWZT);
            map.put("GZDM",GZDM);
            map.put("QTBZ",QTBZ);
            map.put("GZMS",GZMS);
            map.put("ZCDL",ZCDL);
            map.put("ZFDL",ZFDL);
            map.put("SZDMS",SZDMS);
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
            RabbitMqUtils.sendMq(getChannel(),"STATUS",map.toString());
        } else if (lenth == 59) {
            int SDGL  = ((byte)Integer.parseInt(answer[11].concat(answer[12]),16))+((byte)Integer.parseInt(answer[13].concat(answer[14]),16))+((byte)Integer.parseInt(answer[15].concat(answer[16]),16));
            int CNGL  = ((byte)Integer.parseInt(answer[17].concat(answer[18]),16))+((byte)Integer.parseInt(answer[19].concat(answer[20]),16))+((byte)Integer.parseInt(answer[21].concat(answer[22]),16));
            int GFGL  = ((byte)Integer.parseInt(answer[23].concat(answer[24]),16))+((byte)Integer.parseInt(answer[25].concat(answer[26]),16))+((byte)Integer.parseInt(answer[27].concat(answer[28]),16));
            int FZGL1 = ((byte)Integer.parseInt(answer[29].concat(answer[30]),16))+((byte)Integer.parseInt(answer[31].concat(answer[32]),16))+((byte)Integer.parseInt(answer[33].concat(answer[34]),16));
            int FZGL2 = ((byte)Integer.parseInt(answer[35].concat(answer[36]),16))+((byte)Integer.parseInt(answer[37].concat(answer[38]),16))+((byte)Integer.parseInt(answer[39].concat(answer[40]),16));
            int FZGL3 = ((byte)Integer.parseInt(answer[41].concat(answer[42]),16))+((byte)Integer.parseInt(answer[43].concat(answer[44]),16))+((byte)Integer.parseInt(answer[45].concat(answer[46]),16));
            int FZGL4 = ((byte)Integer.parseInt(answer[47].concat(answer[48]),16))+((byte)Integer.parseInt(answer[49].concat(answer[50]),16))+((byte)Integer.parseInt(answer[51].concat(answer[52]),16));
            int FZGL5 = ((byte)Integer.parseInt(answer[53].concat(answer[54]),16))+((byte)Integer.parseInt(answer[55].concat(answer[56]),16))+((byte)Integer.parseInt(answer[57].concat(answer[58]),16));
            int FZGL6 = ((byte)Integer.parseInt(answer[59].concat(answer[60]),16))+((byte)Integer.parseInt(answer[61].concat(answer[62]),16))+((byte)Integer.parseInt(answer[63].concat(answer[64]),16));

            for (int i = 0; i < arraySDGL.length - 1; i++) {
                arraySDGL[i] = arraySDGL[i+1];
                arrayCNGL[i] = arrayCNGL[i+1];
                arrayGFGL[i] = arrayGFGL[i+1];
                arrayFZGL1[i] = arrayFZGL1[i+1];
                arrayFZGL2[i] = arrayFZGL2[i+1];
                arrayFZGL3[i] = arrayFZGL3[i+1];
                arrayFZGL4[i] = arrayFZGL4[i+1];
                arrayFZGL5[i] = arrayFZGL5[i+1];
                arrayFZGL6[i] = arrayFZGL6[i+1];
            }
            arraySDGL[arraySDGL.length-1] = SDGL;
            arrayCNGL[arrayCNGL.length-1] = CNGL;
            arrayGFGL[arrayGFGL.length-1] = GFGL;
            arrayFZGL1[arrayFZGL1.length-1] = FZGL1;
            arrayFZGL2[arrayFZGL2.length-1] = FZGL2;
            arrayFZGL3[arrayFZGL3.length-1] = FZGL3;
            arrayFZGL4[arrayFZGL4.length-1] = FZGL4;
            arrayFZGL5[arrayFZGL5.length-1] = FZGL5;
            arrayFZGL6[arrayFZGL6.length-1] = FZGL6;

            int SDKG = Integer.parseInt(answer[9].concat(answer[10]),16);
            int CNKG = Integer.parseInt(answer[13].concat(answer[14]),16);
            int GFKG = Integer.parseInt(answer[17].concat(answer[18]),16);
            int FZKG1 = Integer.parseInt(answer[21].concat(answer[22]),16);
            int FZKG2 = Integer.parseInt(answer[25].concat(answer[26]),16);
            int FZKG3 = Integer.parseInt(answer[29].concat(answer[30]),16);
            int FZKG4 = Integer.parseInt(answer[33].concat(answer[34]),16);
            int FZKG5 = Integer.parseInt(answer[37].concat(answer[38]),16);
            int FZKG6 = Integer.parseInt(answer[41].concat(answer[42]),16);
            log.debug("市电投入状态："+Integer.parseInt(answer[9].concat(answer[10]),16));
            log.debug("储能投入状态："+Integer.parseInt(answer[13].concat(answer[14]),16));
            log.debug("光伏投入状态："+Integer.parseInt(answer[17].concat(answer[18]),16));
            log.debug("1负载投入状态："+Integer.parseInt(answer[21].concat(answer[22]),16));
            log.debug("2负载投入状态："+Integer.parseInt(answer[25].concat(answer[26]),16));
            log.debug("3负载投入状态："+Integer.parseInt(answer[29].concat(answer[30]),16));
            log.debug("4负载投入状态："+Integer.parseInt(answer[33].concat(answer[34]),16));
            log.debug("5负载投入状态："+Integer.parseInt(answer[37].concat(answer[38]),16));
            log.debug("6负载投入状态："+Integer.parseInt(answer[41].concat(answer[42]),16));
            log.debug("市电A相有功功率："+Integer.parseInt(answer[45].concat(answer[46]),16));
            log.debug("市电B相有功功率："+Integer.parseInt(answer[47].concat(answer[48]),16));
            log.debug("市电C相有功功率："+Integer.parseInt(answer[49].concat(answer[50]),16));
            log.debug("储能A相有功功率："+Integer.parseInt(answer[51].concat(answer[52]),16));
            log.debug("储能B相有功功率："+Integer.parseInt(answer[53].concat(answer[54]),16));
            log.debug("储能C相有功功率："+Integer.parseInt(answer[55].concat(answer[56]),16));
            log.debug("光伏A相有功功率："+Integer.parseInt(answer[57].concat(answer[58]),16));
            log.debug("光伏B相有功功率："+Integer.parseInt(answer[59].concat(answer[60]),16));
            log.debug("光伏C相有功功率："+Integer.parseInt(answer[61].concat(answer[62]),16));
            log.debug("一负载A相有功功率："+Integer.parseInt(answer[63].concat(answer[64]),16));
            log.debug("一负载B相有功功率："+Integer.parseInt(answer[65].concat(answer[66]),16));
            log.debug("一负载C相有功功率："+Integer.parseInt(answer[67].concat(answer[68]),16));
            log.debug("二负载A相有功功率："+Integer.parseInt(answer[69].concat(answer[70]),16));
            log.debug("二负载B相有功功率："+Integer.parseInt(answer[71].concat(answer[72]),16));
            log.debug("二负载C相有功功率："+Integer.parseInt(answer[73].concat(answer[74]),16));
            log.debug("三负载A相有功功率："+Integer.parseInt(answer[75].concat(answer[76]),16));
            log.debug("三负载B相有功功率："+Integer.parseInt(answer[77].concat(answer[78]),16));
            log.debug("三负载C相有功功率："+Integer.parseInt(answer[79].concat(answer[80]),16));
            log.debug("四负载A相有功功率："+Integer.parseInt(answer[81].concat(answer[82]),16));
            log.debug("四负载B相有功功率："+Integer.parseInt(answer[83].concat(answer[84]),16));
            log.debug("四负载B相有功功率："+Integer.parseInt(answer[85].concat(answer[86]),16));
            log.debug("五负载A相有功功率："+Integer.parseInt(answer[87].concat(answer[88]),16));
            log.debug("五负载B相有功功率："+Integer.parseInt(answer[89].concat(answer[90]),16));
            log.debug("五负载C相有功功率："+Integer.parseInt(answer[91].concat(answer[92]),16));
            log.debug("六负载A相有功功率："+Integer.parseInt(answer[93].concat(answer[94]),16));
            log.debug("六负载B相有功功率："+Integer.parseInt(answer[95].concat(answer[96]),16));
            log.debug("六负载C相有功功率："+Integer.parseInt(answer[97].concat(answer[98]),16));
            Map map =new HashMap();
            map.put("SDKG",SDKG);
            map.put("CNKG",CNKG);
            map.put("GFKG",GFKG);
            map.put("FZKG1",FZKG1);
            map.put("FZKG2",FZKG2);
            map.put("FZKG3",FZKG3);
            map.put("FZKG4",FZKG4);
            map.put("FZKG5",FZKG5);
            map.put("FZKG6",FZKG6);
            map.put("SDGL",SDGL);
            map.put("CNGL",CNGL);
            map.put("GFGL",GFGL);
            map.put("FZGL1",FZGL1);
            map.put("FZGL2",FZGL2);
            map.put("FZGL3",FZGL3);
            map.put("FZGL4",FZGL4);
            map.put("FZGL5",FZGL5);
            map.put("FZGL6",FZGL6);
            dataHandle.setSDGL(SDGL);
            dataHandle.setCNGL(CNGL);
            dataHandle.setGFGL(GFGL);
            dataHandle.setFZGL1(FZGL1);
            dataHandle.setFZGL2(FZGL2);
            dataHandle.setFZGL3(FZGL3);
            dataHandle.setFZGL4(FZGL4);
            dataHandle.setFZGL5(FZGL5);
            dataHandle.setFZGL6(FZGL6);

            RabbitMqUtils.sendMq(getChannel(),"POWER",map.toString());
        } else if (lenth == 99) {
            BigDecimal GFUA = new BigDecimal(Integer.parseInt(answer[9].concat(answer[10]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal GFUB = new BigDecimal(Integer.parseInt(answer[11].concat(answer[12]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal GFUC = new BigDecimal(Integer.parseInt(answer[13].concat(answer[14]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUA1 = new BigDecimal(Integer.parseInt(answer[15].concat(answer[16]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUB1 = new BigDecimal(Integer.parseInt(answer[17].concat(answer[18]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUC1 = new BigDecimal(Integer.parseInt(answer[19].concat(answer[20]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUA2 = new BigDecimal(Integer.parseInt(answer[21].concat(answer[22]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUB2 = new BigDecimal(Integer.parseInt(answer[23].concat(answer[24]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUC2 = new BigDecimal(Integer.parseInt(answer[25].concat(answer[26]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUA3 = new BigDecimal(Integer.parseInt(answer[27].concat(answer[28]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUB3 = new BigDecimal(Integer.parseInt(answer[29].concat(answer[30]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUC3 = new BigDecimal(Integer.parseInt(answer[31].concat(answer[32]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUA4 = new BigDecimal(Integer.parseInt(answer[33].concat(answer[34]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUB4 = new BigDecimal(Integer.parseInt(answer[35].concat(answer[36]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUC4 = new BigDecimal(Integer.parseInt(answer[37].concat(answer[38]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUA5 = new BigDecimal(Integer.parseInt(answer[39].concat(answer[40]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUB5 = new BigDecimal(Integer.parseInt(answer[41].concat(answer[42]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUC5 = new BigDecimal(Integer.parseInt(answer[43].concat(answer[44]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUA6 = new BigDecimal(Integer.parseInt(answer[45].concat(answer[46]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUB6 = new BigDecimal(Integer.parseInt(answer[47].concat(answer[48]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZUC6 = new BigDecimal(Integer.parseInt(answer[49].concat(answer[50]),16)).multiply(new BigDecimal("0.1"));
            int SDIA = Integer.parseInt(answer[51].concat(answer[52]),16);
            int SDIB = Integer.parseInt(answer[53].concat(answer[54]),16);
            int SDIC = Integer.parseInt(answer[55].concat(answer[56]),16);
            int CNIA = Integer.parseInt(answer[57].concat(answer[58]),16);
            int CNIB = Integer.parseInt(answer[59].concat(answer[60]),16);
            int CNIC = Integer.parseInt(answer[61].concat(answer[62]),16);
            int GFIA = Integer.parseInt(answer[63].concat(answer[64]),16);
            int GFIB = Integer.parseInt(answer[65].concat(answer[66]),16);
            int GFIC = Integer.parseInt(answer[67].concat(answer[68]),16);
            int FZIA1 = Integer.parseInt(answer[69].concat(answer[70]),16);
            int FZIB1 = Integer.parseInt(answer[71].concat(answer[72]),16);
            int FZIC1 = Integer.parseInt(answer[73].concat(answer[74]),16);
            int FZIA2 = Integer.parseInt(answer[75].concat(answer[76]),16);
            int FZIB2 = Integer.parseInt(answer[77].concat(answer[78]),16);
            int FZIC2 = Integer.parseInt(answer[79].concat(answer[80]),16);
            int FZIA3 = Integer.parseInt(answer[81].concat(answer[82]),16);
            int FZIB3 = Integer.parseInt(answer[83].concat(answer[84]),16);
            int FZIC3 = Integer.parseInt(answer[85].concat(answer[86]),16);
            int FZIA4 = Integer.parseInt(answer[87].concat(answer[88]),16);
            int FZIB4 = Integer.parseInt(answer[89].concat(answer[90]),16);
            int FZIC4 = Integer.parseInt(answer[91].concat(answer[92]),16);
            int FZIA5 = Integer.parseInt(answer[93].concat(answer[94]),16);
            int FZIB5 = Integer.parseInt(answer[95].concat(answer[96]),16);
            int FZIC5 = Integer.parseInt(answer[97].concat(answer[98]),16);
            int FZIA6 = Integer.parseInt(answer[99].concat(answer[100]),16);
            int FZIB6 = Integer.parseInt(answer[101].concat(answer[102]),16);
            int FZIC6 = Integer.parseInt(answer[103].concat(answer[104]),16);
            log.debug("光伏A相电压："+Integer.parseInt(answer[9].concat(answer[10]),16));
            log.debug("光伏B相电压："+Integer.parseInt(answer[11].concat(answer[12]),16));
            log.debug("光伏C相电压："+Integer.parseInt(answer[13].concat(answer[14]),16));
            log.debug("一负载A相电压："+Integer.parseInt(answer[15].concat(answer[16]),16));
            log.debug("一负载B相电压："+Integer.parseInt(answer[17].concat(answer[18]),16));
            log.debug("一负载C相电压："+Integer.parseInt(answer[19].concat(answer[20]),16));
            log.debug("二负载A相电压："+Integer.parseInt(answer[21].concat(answer[22]),16));
            log.debug("二负载B相电压："+Integer.parseInt(answer[23].concat(answer[24]),16));
            log.debug("二负载C相电压："+Integer.parseInt(answer[25].concat(answer[26]),16));
            log.debug("三负载A相电压："+Integer.parseInt(answer[27].concat(answer[28]),16));
            log.debug("三负载B相电压："+Integer.parseInt(answer[29].concat(answer[30]),16));
            log.debug("三负载C相电压："+Integer.parseInt(answer[31].concat(answer[32]),16));
            log.debug("四负载A相电压："+Integer.parseInt(answer[33].concat(answer[34]),16));
            log.debug("四负载B相电压："+Integer.parseInt(answer[35].concat(answer[36]),16));
            log.debug("四负载C相电压："+Integer.parseInt(answer[37].concat(answer[38]),16));
            log.debug("五负载A相电压："+Integer.parseInt(answer[39].concat(answer[40]),16));
            log.debug("五负载B相电压："+Integer.parseInt(answer[41].concat(answer[42]),16));
            log.debug("五负载C相电压："+Integer.parseInt(answer[43].concat(answer[44]),16));
            log.debug("六负载A相电压："+Integer.parseInt(answer[45].concat(answer[46]),16));
            log.debug("六负载B相电压："+Integer.parseInt(answer[47].concat(answer[48]),16));
            log.debug("六负载C相电压："+Integer.parseInt(answer[49].concat(answer[50]),16));
            log.debug("市电A相电流："+Integer.parseInt(answer[51].concat(answer[52]),16));
            log.debug("市电B相电流："+Integer.parseInt(answer[53].concat(answer[54]),16));
            log.debug("市电C相电流："+Integer.parseInt(answer[55].concat(answer[56]),16));
            log.debug("储能A相电流："+Integer.parseInt(answer[57].concat(answer[58]),16));
            log.debug("储能B相电流："+Integer.parseInt(answer[59].concat(answer[60]),16));
            log.debug("储能C相电流："+Integer.parseInt(answer[61].concat(answer[62]),16));
            log.debug("光伏A相电流："+Integer.parseInt(answer[63].concat(answer[64]),16));
            log.debug("光伏B相电流："+Integer.parseInt(answer[65].concat(answer[66]),16));
            log.debug("光伏C相电流："+Integer.parseInt(answer[67].concat(answer[68]),16));
            log.debug("一负载A相电流："+Integer.parseInt(answer[69].concat(answer[70]),16));
            log.debug("一负载B相电流："+Integer.parseInt(answer[71].concat(answer[72]),16));
            log.debug("一负载C相电流："+Integer.parseInt(answer[73].concat(answer[74]),16));
            log.debug("二负载A相电流："+Integer.parseInt(answer[75].concat(answer[76]),16));
            log.debug("二负载B相电流："+Integer.parseInt(answer[77].concat(answer[78]),16));
            log.debug("二负载C相电流："+Integer.parseInt(answer[79].concat(answer[80]),16));
            log.debug("三负载A相电流："+Integer.parseInt(answer[81].concat(answer[82]),16));
            log.debug("三负载B相电流："+Integer.parseInt(answer[83].concat(answer[84]),16));
            log.debug("三负载C相电流："+Integer.parseInt(answer[85].concat(answer[86]),16));
            log.debug("四负载A相电流："+Integer.parseInt(answer[87].concat(answer[88]),16));
            log.debug("四负载B相电流："+Integer.parseInt(answer[89].concat(answer[90]),16));
            log.debug("四负载C相电流："+Integer.parseInt(answer[91].concat(answer[92]),16));
            log.debug("五负载A相电流："+Integer.parseInt(answer[93].concat(answer[94]),16));
            log.debug("五负载B相电流："+Integer.parseInt(answer[95].concat(answer[96]),16));
            log.debug("五负载C相电流："+Integer.parseInt(answer[97].concat(answer[98]),16));
            log.debug("六负载A相电流："+Integer.parseInt(answer[99].concat(answer[100]),16));
            log.debug("六负载B相电流："+Integer.parseInt(answer[101].concat(answer[102]),16));
            log.debug("六负载C相电流："+Integer.parseInt(answer[103].concat(answer[104]),16));

            BigDecimal SDWG  = new BigDecimal((byte)Integer.parseInt(answer[105].concat(answer[106]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal CNWG  = new BigDecimal((byte)Integer.parseInt(answer[111].concat(answer[112]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal GFWG  = new BigDecimal((byte)Integer.parseInt(answer[117].concat(answer[118]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZWG1 = new BigDecimal((byte)Integer.parseInt(answer[123].concat(answer[124]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZWG2 = new BigDecimal((byte)Integer.parseInt(answer[129].concat(answer[130]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZWG3 = new BigDecimal((byte)Integer.parseInt(answer[135].concat(answer[136]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZWG4 = new BigDecimal((byte)Integer.parseInt(answer[141].concat(answer[142]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZWG5 = new BigDecimal((byte)Integer.parseInt(answer[147].concat(answer[148]),16)).multiply(new BigDecimal("0.1"));
            BigDecimal FZWG6 = new BigDecimal((byte)Integer.parseInt(answer[153].concat(answer[154]),16)).multiply(new BigDecimal("0.1"));
            log.debug("市电A相无功："+Integer.parseInt(answer[105].concat(answer[106]),16));
            log.debug("市电B相无功："+Integer.parseInt(answer[107].concat(answer[108]),16));
            log.debug("市电C相无功："+Integer.parseInt(answer[109].concat(answer[110]),16));
            log.debug("储能A相无功："+Integer.parseInt(answer[111].concat(answer[112]),16));
            log.debug("储能B相无功："+Integer.parseInt(answer[113].concat(answer[114]),16));
            log.debug("储能C相无功："+Integer.parseInt(answer[115].concat(answer[116]),16));
            log.debug("光伏A相无功："+Integer.parseInt(answer[117].concat(answer[118]),16));
            log.debug("光伏B相无功："+Integer.parseInt(answer[119].concat(answer[120]),16));
            log.debug("光伏C相无功："+Integer.parseInt(answer[121].concat(answer[122]),16));
            log.debug("一负载A相无功："+Integer.parseInt(answer[123].concat(answer[124]),16));
            log.debug("一负载B相无功："+Integer.parseInt(answer[125].concat(answer[126]),16));
            log.debug("一负载C相无功："+Integer.parseInt(answer[127].concat(answer[128]),16));
            log.debug("二负载A相无功："+Integer.parseInt(answer[129].concat(answer[130]),16));
            log.debug("二负载B相无功："+Integer.parseInt(answer[131].concat(answer[132]),16));
            log.debug("二负载C相无功："+Integer.parseInt(answer[133].concat(answer[134]),16));
            log.debug("三负载A相无功："+Integer.parseInt(answer[135].concat(answer[136]),16));
            log.debug("三负载B相无功："+Integer.parseInt(answer[137].concat(answer[138]),16));
            log.debug("三负载C相无功："+Integer.parseInt(answer[139].concat(answer[140]),16));
            log.debug("四负载A相无功："+Integer.parseInt(answer[141].concat(answer[142]),16));
            log.debug("四负载B相无功："+Integer.parseInt(answer[143].concat(answer[144]),16));
            log.debug("四负载C相无功："+Integer.parseInt(answer[145].concat(answer[146]),16));
            log.debug("五负载A相无功："+Integer.parseInt(answer[147].concat(answer[148]),16));
            log.debug("五负载B相无功："+Integer.parseInt(answer[149].concat(answer[150]),16));
            log.debug("五负载C相无功："+Integer.parseInt(answer[151].concat(answer[152]),16));
            log.debug("六负载A相无功："+Integer.parseInt(answer[153].concat(answer[154]),16));
            log.debug("六负载B相无功："+Integer.parseInt(answer[155].concat(answer[156]),16));
            log.debug("六负载C相无功："+Integer.parseInt(answer[157].concat(answer[158]),16));

            BigDecimal SDYS = new BigDecimal((byte)Integer.parseInt(answer[159].concat(answer[160]),16)).multiply(new BigDecimal("0.01"));
            BigDecimal CNYS = new BigDecimal((byte)Integer.parseInt(answer[165].concat(answer[166]),16)).multiply(new BigDecimal("0.01"));
            BigDecimal GFYS = new BigDecimal((byte)Integer.parseInt(answer[171].concat(answer[172]),16)).multiply(new BigDecimal("0.01"));
            BigDecimal FZYS1 = new BigDecimal((byte)Integer.parseInt(answer[177].concat(answer[178]),16)).multiply(new BigDecimal("0.01"));
            BigDecimal FZYS2 = new BigDecimal((byte)Integer.parseInt(answer[183].concat(answer[184]),16)).multiply(new BigDecimal("0.01"));
            BigDecimal FZYS3 = new BigDecimal((byte)Integer.parseInt(answer[189].concat(answer[190]),16)).multiply(new BigDecimal("0.01"));
            BigDecimal FZYS4 = new BigDecimal((byte)Integer.parseInt(answer[195].concat(answer[196]),16)).multiply(new BigDecimal("0.01"));
            BigDecimal FZYS5 = new BigDecimal((byte)Integer.parseInt(answer[201].concat(answer[202]),16)).multiply(new BigDecimal("0.01"));
            BigDecimal FZYS6 = new BigDecimal((byte)Integer.parseInt(answer[207].concat(answer[208]),16)).multiply(new BigDecimal("0.01"));
            log.debug("市电A相功率因素："+Integer.parseInt(answer[159].concat(answer[160]),16));
            log.debug("市电B相功率因素："+Integer.parseInt(answer[161].concat(answer[162]),16));
            log.debug("市电C相功率因素："+Integer.parseInt(answer[163].concat(answer[164]),16));
            log.debug("储能A相功率因素："+Integer.parseInt(answer[165].concat(answer[166]),16));
            log.debug("储能B相功率因素："+Integer.parseInt(answer[167].concat(answer[168]),16));
            log.debug("储能C相功率因素："+Integer.parseInt(answer[169].concat(answer[170]),16));
            log.debug("光伏A相功率因素："+Integer.parseInt(answer[171].concat(answer[172]),16));
            log.debug("光伏B相功率因素："+Integer.parseInt(answer[173].concat(answer[174]),16));
            log.debug("光伏C相功率因素："+Integer.parseInt(answer[175].concat(answer[176]),16));
            log.debug("一负载A相功率因素："+Integer.parseInt(answer[177].concat(answer[178]),16));
            log.debug("一负载B相功率因素："+Integer.parseInt(answer[179].concat(answer[180]),16));
            log.debug("一负载C相功率因素："+Integer.parseInt(answer[181].concat(answer[182]),16));
            log.debug("二负载A相功率因素："+Integer.parseInt(answer[183].concat(answer[184]),16));
            log.debug("二负载B相功率因素："+Integer.parseInt(answer[185].concat(answer[186]),16));
            log.debug("二负载C相功率因素："+Integer.parseInt(answer[187].concat(answer[188]),16));
            log.debug("三负载A相功率因素："+Integer.parseInt(answer[189].concat(answer[190]),16));
            log.debug("三负载B相功率因素："+Integer.parseInt(answer[191].concat(answer[192]),16));
            log.debug("三负载C相功率因素："+Integer.parseInt(answer[193].concat(answer[194]),16));
            log.debug("四负载A相功率因素："+Integer.parseInt(answer[195].concat(answer[196]),16));
            log.debug("四负载B相功率因素："+Integer.parseInt(answer[197].concat(answer[198]),16));
            log.debug("四负载C相功率因素："+Integer.parseInt(answer[199].concat(answer[200]),16));
            log.debug("五负载A相功率因素："+Integer.parseInt(answer[201].concat(answer[202]),16));
            log.debug("五负载B相功率因素："+Integer.parseInt(answer[203].concat(answer[204]),16));
            log.debug("五负载C相功率因素："+Integer.parseInt(answer[205].concat(answer[206]),16));
            log.debug("六负载A相功率因素："+Integer.parseInt(answer[207].concat(answer[208]),16));
            log.debug("六负载B相功率因素："+Integer.parseInt(answer[209].concat(answer[210]),16));
            log.debug("六负载C相功率因素："+Integer.parseInt(answer[211].concat(answer[212]),16));
            Map map = new HashMap();
            map.put("SDIA",SDIA);
            map.put("SDIB",SDIB);
            map.put("SDIC",SDIC);
            map.put("CNIA",CNIA);
            map.put("CNIB",CNIB);
            map.put("CNIC",CNIC);
            map.put("GFIA",GFIA);
            map.put("GFIB",GFIB);
            map.put("GFIC",GFIC);
            map.put("FZIA1",FZIA1);
            map.put("FZIB1",FZIB1);
            map.put("FZIC1",FZIC1);
            map.put("FZIA2",FZIA2);
            map.put("FZIB2",FZIB2);
            map.put("FZIC2",FZIC2);
            map.put("FZIA3",FZIA3);
            map.put("FZIB3",FZIB3);
            map.put("FZIC3",FZIC3);
            map.put("FZIA4",FZIA4);
            map.put("FZIB4",FZIB4);
            map.put("FZIC4",FZIC4);
            map.put("FZIA5",FZIA5);
            map.put("FZIB5",FZIB5);
            map.put("FZIC5",FZIC5);
            map.put("FZIA6",FZIA6);
            map.put("FZIB6",FZIB6);
            map.put("FZIC6",FZIC6);
            dataHandle.setSDIA(SDIA);
            dataHandle.setCNIA(CNIA);
            dataHandle.setGFIA(GFIA);
            dataHandle.setFZIA1(FZIA1);
            dataHandle.setFZIA2(FZIA2);
            dataHandle.setFZIA3(FZIA3);
            dataHandle.setFZIA4(FZIA4);
            dataHandle.setFZIA5(FZIA5);
            dataHandle.setFZIA6(FZIA6);
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
            map.put("SDWG",SDWG);
            map.put("CDWG",CNWG);
            map.put("GFWG",GFWG);
            map.put("FZWG1",FZWG1);
            map.put("FZWG2",FZWG2);
            map.put("FZWG3",FZWG3);
            map.put("FZWG4",FZWG4);
            map.put("FZWG5",FZWG5);
            map.put("FZWG6",FZWG6);
            map.put("SDYS",SDYS);
            map.put("CDYS",CNYS);
            map.put("GFYS",GFYS);
            map.put("FZYS1",FZYS1);
            map.put("FZYS2",FZYS2);
            map.put("FZYS3",FZYS3);
            map.put("FZYS4",FZYS4);
            map.put("FZYS5",FZYS5);
            map.put("FZYS6",FZYS6);
            RabbitMqUtils.sendMq(getChannel(),"VOLTAGE_CURRENT",map.toString());
        } else if (lenth == 6) {
            RabbitMqUtils.sendMq(getChannel(),"SWITCH_RETURN","SUCCESS");
        } else {
                log.error("返回的数据格式不对，无法解析!");
        }

    }
    public static Channel getChannel() {
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


    public static void main(String[] args) throws Exception {
//    analysis("00 00 00 00 00 39 01 03 4E 02 67 00 01 00 DD 00 00 00 DC 00 00 00 DD 00 00 00 00 00 01 00 01 FF CB FF F8 03 D9 00 00 00 00 01 F3 00 00 FF F8 00 00 00 01 00 05 00 00 00 02 00 00 00 EA 00 00 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 64 00 00 00 03 00 DC 00 00");
    }
}
