package com.visenergy.modbustcp;

/**
 * Modbus发送类命令生成
 * @Author WuSong
 * @Date 2017-08-04
 * @Time 16:02:34
 */
public class ModbusSendGenerate {

    protected static final String TcpHead = "000000000006";

    public static String queryBefoer(){
        StringBuffer query = new StringBuffer(TcpHead);
        query.append("01");
        query.append("03");
        query.append("00");
        query.append("00");
        query.append("00");
        query.append(Integer.toHexString(27));
        return query.toString();
    }
    public static String queryMiddle(){
        StringBuffer query = new StringBuffer(TcpHead);
        query.append("01");
        query.append("03");
        query.append("00");
        query.append(Integer.toHexString(39));
        query.append("00");
        query.append(Integer.toHexString(45));
        return query.toString();
    }
    public static String queryAfter(){
        StringBuffer query = new StringBuffer(TcpHead);
        query.append("01");
        query.append("03");
        query.append("00");
        query.append(Integer.toHexString(90));
        query.append("00");
        query.append(Integer.toHexString(102));
        return query.toString();
    }
    /**
     * 启动/停止控制
     * @param command 启动或停止命令
     * @return
     * @throws Exception
     */
    public static String startOrStop(String command) throws Exception {
        StringBuffer startAndStop = new StringBuffer(TcpHead);
        startAndStop.append("01");
        startAndStop.append("06");
        startAndStop.append("00");
        startAndStop.append(Integer.toHexString(55));
        startAndStop.append("00");

        if (command.equals("start")){
            startAndStop.append("01");
            return startAndStop.toString();
        }else if (command.equals("stop")){
            startAndStop.append("00");
            return startAndStop.toString();
        }else {
            throw new Exception("错误！不是启动或停止命令");
        }
    }

    /**
     * 工作模式设置
     * @param command 01并机 02直流电压源 03恒直流电流充放电 04恒交流功率充放电 05离网
     * @return
     */
    public static String workMode(String command){
        StringBuffer work = new StringBuffer(TcpHead);
        work.append("01");
        work.append("06");
        work.append("00");
        work.append(Integer.toHexString(32));
        work.append("00");
        work.append(command);
        return work.toString();
    }

  /*  public static String PowerSet(){
        StringBuffer power = new StringBuffer(TcpHead);
        power.append("01");
        power.append("06");
        power.append("00");
        power.append(Integer.toHexString(33));
        power.append("00");
    }*/

    /**
     * 功率大小设置
     * @param powerHigh 功率设置高字节
     * @param powerLow 功率设置低字节
     * @return
     */
    public String setPower(String powerHigh,String powerLow){
        StringBuffer power = new StringBuffer(TcpHead);
        power.append("01");
        power.append("06");
        power.append("00");
        power.append("13");
        power.append(powerHigh);
        power.append(powerLow);
        return power.toString();
    }

    /**
     * 充电/逆变转换控制
     * @param command 转换控制命令
     * @return
     * @throws Exception
     */
    public String chargeTransform(String command) throws Exception {
        StringBuffer transform = new StringBuffer(TcpHead);
        transform.append("01");
        transform.append("06");
        transform.append("00");
        transform.append("14");
        transform.append("00");

        if (command.equals("charge")){
            transform.append("01");
            return transform.toString();
        }else if (command.equals("contravariant")){
            transform.append("00");
            return transform.toString();
        }else {
            throw new Exception("错误！不是充电或逆变转换命令");
        }
    }

    /**
     * 设置充电电流
     * @param currentHigh 电流高字节
     * @param currentLow 电流低字节
     * @return
     */
    public String setCurrent(String currentHigh,String currentLow){
        StringBuffer current = new StringBuffer(TcpHead);
        current.append("01");
        current.append("06");
        current.append("00");
        current.append("15");
        current.append(currentHigh);
        current.append(currentLow);
        return current.toString();
    }
}
