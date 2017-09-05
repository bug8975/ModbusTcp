package com.visenergy.modbustcp;

import com.flying.jdbc.SqlHelper;
import com.flying.jdbc.data.CommandType;
import com.flying.jdbc.data.Parameter;
import com.flying.jdbc.db.type.BaseTypes;
import com.flying.jdbc.util.DBConnection;
import com.flying.jdbc.util.DBConnectionPool;
import org.apache.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author WuSong
 * @Date 2017-08-23
 * @Time 17:42:19
 */
public class DataHandle {
    private int DCU = -1;
    private int DCI = -1;
    private double SDPL = -1;
    private int BLWZT = -1;
    private int GZDM = -1;
    private int QTBZ = -1;
    private int GZMS = -1;
    private int ZCDL = -1;
    private int ZFDL = -1;
    private int SZDMS = -1;
    private double SDUA = -1;
    private double SDUB = -1;
    private double SDUC = -1;
    private int SDIA = -1;
    private int CNIA = -1;
    private int GFIA = -1;
    private int FZIA1 = -1;
    private int FZIA2 = -1;
    private int FZIA3 = -1;
    private int FZIA4 = -1;
    private int FZIA5 = -1;
    private int FZIA6 = -1;
    private double SDGL = -1;
    private double CNGL = -1;
    private double GFGL = -1;
    private double FZGL1 = -1;
    private double FZGL2 = -1;
    private double FZGL3 = -1;
    private double FZGL4 = -1;
    private double FZGL5 = -1;
    private double FZGL6 = -1;
    private double SDWG = -1;
    private double CNWG = -1;
    private double GFWG = -1;
    private double FZWG1 = -1;
    private double FZWG2 = -1;
    private double FZWG3 = -1;
    private double FZWG4 = -1;
    private double FZWG5 = -1;
    private double FZWG6 = -1;
    private double SDYS = -1;
    private double CNYS = -1;
    private double GFYS = -1;
    private double FZYS1 = -1;
    private double FZYS2 = -1;
    private double FZYS3 = -1;
    private double FZYS4 = -1;
    private double FZYS5 = -1;
    private double FZYS6 = -1;
    private Logger log = Logger.getLogger(DataHandle.class);
static int j =1;
static int k =1;
static int m =1;
    static {
        SqlHelper.connPool = new DBConnectionPool(10);
    }
    /** 存储数据到数据库*/
    public  DataHandle(){
        log.info("************************m"+m++);
        Runnable runnable = new Runnable() {
            public void run() {
                log.info("**************************k"+k++);
                DBConnection conn = SqlHelper.connPool.getConnection();
                String sql = "INSERT INTO T_MICROGRID_COLLECT (DCU,DCI,SDPL,BLWZT,GZDM,QTBZ,GZMS,ZCDL,ZFDL,SZDMS,SDUA,SDUB,SDUC,SDIA,CNIA,GFIA,FZIA1,FZIA2,FZIA3,FZIA4,FZIA5,FZIA6,SDGL,CNGL,GFGL,FZGL1,FZGL2,FZGL3,FZGL4,FZGL5,FZGL6,SDWG,CNWG,GFWG,FZWG1,FZWG2,FZWG3,FZWG4,FZWG5,FZWG6,SDYS,CNYS,GFYS,FZYS1,FZYS2,FZYS3,FZYS4,FZYS5,FZYS6) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                Parameter[] parameter = new Parameter[49];
                int i = 0;
                parameter[i++] = new Parameter("DCU", BaseTypes.BIGINT,DCU);
                parameter[i++] = new Parameter("DCI", BaseTypes.BIGINT,DCI);
                parameter[i++] = new Parameter("SDPL", BaseTypes.DECIMAL,SDPL);
                parameter[i++] = new Parameter("BLWZT", BaseTypes.BIGINT,BLWZT);
                parameter[i++] = new Parameter("GZDM", BaseTypes.BIGINT,GZDM);
                parameter[i++] = new Parameter("QTBZ", BaseTypes.BIGINT,QTBZ);
                parameter[i++] = new Parameter("GZMS", BaseTypes.BIGINT,GZMS);
                parameter[i++] = new Parameter("ZCDL", BaseTypes.BIGINT,ZCDL);
                parameter[i++] = new Parameter("ZFDL", BaseTypes.BIGINT,ZFDL);
                parameter[i++] = new Parameter("SZDMS", BaseTypes.BIGINT,SZDMS);
                parameter[i++] = new Parameter("SDUA", BaseTypes.DECIMAL,SDUA);
                parameter[i++] = new Parameter("SDUB", BaseTypes.DECIMAL,SDUB);
                parameter[i++] = new Parameter("SDUC", BaseTypes.DECIMAL,SDUC);
                parameter[i++] = new Parameter("SDIA", BaseTypes.BIGINT,SDIA);
                parameter[i++] = new Parameter("CNIA", BaseTypes.BIGINT,CNIA);
                parameter[i++] = new Parameter("GFIA", BaseTypes.BIGINT,GFIA);
                parameter[i++] = new Parameter("FZIA1", BaseTypes.BIGINT,FZIA1);
                parameter[i++] = new Parameter("FZIA2", BaseTypes.BIGINT,FZIA2);
                parameter[i++] = new Parameter("FZIA3", BaseTypes.BIGINT,FZIA3);
                parameter[i++] = new Parameter("FZIA4", BaseTypes.BIGINT,FZIA4);
                parameter[i++] = new Parameter("FZIA5", BaseTypes.BIGINT,FZIA5);
                parameter[i++] = new Parameter("FZIA6", BaseTypes.BIGINT,FZIA6);
                parameter[i++] = new Parameter("SDGL", BaseTypes.DECIMAL,SDGL);
                parameter[i++] = new Parameter("CNGL", BaseTypes.DECIMAL,CNGL);
                parameter[i++] = new Parameter("GFGL", BaseTypes.DECIMAL,GFGL);
                parameter[i++] = new Parameter("FZGL1", BaseTypes.DECIMAL,FZGL1);
                parameter[i++] = new Parameter("FZGL2", BaseTypes.DECIMAL,FZGL2);
                parameter[i++] = new Parameter("FZGL3", BaseTypes.DECIMAL,FZGL3);
                parameter[i++] = new Parameter("FZGL4", BaseTypes.DECIMAL,FZGL4);
                parameter[i++] = new Parameter("FZGL5", BaseTypes.DECIMAL,FZGL5);
                parameter[i++] = new Parameter("FZGL6", BaseTypes.DECIMAL,FZGL6);
                parameter[i++] = new Parameter("SDWG", BaseTypes.DECIMAL,SDWG);
                parameter[i++] = new Parameter("CNWG", BaseTypes.DECIMAL,CNWG);
                parameter[i++] = new Parameter("GFWG", BaseTypes.DECIMAL,GFWG);
                parameter[i++] = new Parameter("FZWG1", BaseTypes.DECIMAL,FZWG1);
                parameter[i++] = new Parameter("FZWG2", BaseTypes.DECIMAL,FZWG2);
                parameter[i++] = new Parameter("FZWG3", BaseTypes.DECIMAL,FZWG3);
                parameter[i++] = new Parameter("FZWG4", BaseTypes.DECIMAL,FZWG4);
                parameter[i++] = new Parameter("FZWG5", BaseTypes.DECIMAL,FZWG5);
                parameter[i++] = new Parameter("FZWG6", BaseTypes.DECIMAL,FZWG6);
                parameter[i++] = new Parameter("SDYS", BaseTypes.DECIMAL,SDYS);
                parameter[i++] = new Parameter("CNYS", BaseTypes.DECIMAL,CNYS);
                parameter[i++] = new Parameter("GFYS", BaseTypes.DECIMAL,GFYS);
                parameter[i++] = new Parameter("FZYS1", BaseTypes.DECIMAL,FZYS1);
                parameter[i++] = new Parameter("FZYS2", BaseTypes.DECIMAL,FZYS2);
                parameter[i++] = new Parameter("FZYS3", BaseTypes.DECIMAL,FZYS3);
                parameter[i++] = new Parameter("FZYS4", BaseTypes.DECIMAL,FZYS4);
                parameter[i++] = new Parameter("FZYS5", BaseTypes.DECIMAL,FZYS5);
                parameter[i++] = new Parameter("FZYS6", BaseTypes.DECIMAL,FZYS6);
                i = 0;
                try {
                    SqlHelper.executeNonQuery(conn, CommandType.Text,sql,parameter);
                    log.info("******************************j"+j++);
                } catch (Exception e) {
                    log.debug("存储数据出错！",e);
                }
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable,0,30, TimeUnit.SECONDS);
    }

    public int getDCU() {
        return DCU;
    }

    public void setDCU(int DCU) {
        this.DCU = DCU;
    }

    public int getDCI() {
        return DCI;
    }

    public void setDCI(int DCI) {
        this.DCI = DCI;
    }

    public int getBLWZT() {
        return BLWZT;
    }

    public void setBLWZT(int BLWZT) {
        this.BLWZT = BLWZT;
    }

    public double getSDPL() {
        return SDPL;
    }

    public void setSDPL(double SDPL) {
        this.SDPL = SDPL;
    }

    public int getGZDM() {
        return GZDM;
    }

    public void setGZDM(int GZDM) {
        this.GZDM = GZDM;
    }

    public int getQTBZ() {
        return QTBZ;
    }

    public void setQTBZ(int QTBZ) {
        this.QTBZ = QTBZ;
    }

    public int getGZMS() {
        return GZMS;
    }

    public void setGZMS(int GZMS) {
        this.GZMS = GZMS;
    }

    public int getZCDL() {
        return ZCDL;
    }

    public void setZCDL(int ZCDL) {
        this.ZCDL = ZCDL;
    }

    public int getZFDL() {
        return ZFDL;
    }

    public void setZFDL(int ZFDL) {
        this.ZFDL = ZFDL;
    }

    public int getSZDMS() {
        return SZDMS;
    }

    public void setSZDMS(int SZDMS) {
        this.SZDMS = SZDMS;
    }

    public double getSDUA() {
        return SDUA;
    }

    public void setSDUA(double SDUA) {
        this.SDUA = SDUA;
    }

    public double getSDUB() {
        return SDUB;
    }

    public void setSDUB(double SDUB) {
        this.SDUB = SDUB;
    }

    public double getSDUC() {
        return SDUC;
    }

    public void setSDUC(double SDUC) {
        this.SDUC = SDUC;
    }

    public int getSDIA() {
        return SDIA;
    }

    public void setSDIA(int SDIA) {
        this.SDIA = SDIA;
    }

    public int getCNIA() {
        return CNIA;
    }

    public void setCNIA(int CNIA) {
        this.CNIA = CNIA;
    }

    public int getGFIA() {
        return GFIA;
    }

    public void setGFIA(int GFIA) {
        this.GFIA = GFIA;
    }

    public int getFZIA1() {
        return FZIA1;
    }

    public void setFZIA1(int FZIA1) {
        this.FZIA1 = FZIA1;
    }

    public int getFZIA2() {
        return FZIA2;
    }

    public void setFZIA2(int FZIA2) {
        this.FZIA2 = FZIA2;
    }

    public int getFZIA3() {
        return FZIA3;
    }

    public void setFZIA3(int FZIA3) {
        this.FZIA3 = FZIA3;
    }

    public int getFZIA4() {
        return FZIA4;
    }

    public void setFZIA4(int FZIA4) {
        this.FZIA4 = FZIA4;
    }

    public int getFZIA5() {
        return FZIA5;
    }

    public void setFZIA5(int FZIA5) {
        this.FZIA5 = FZIA5;
    }

    public int getFZIA6() {
        return FZIA6;
    }

    public void setFZIA6(int FZIA6) {
        this.FZIA6 = FZIA6;
    }

    public double getSDGL() {
        return SDGL;
    }

    public void setSDGL(double SDGL) {
        this.SDGL = SDGL;
    }

    public double getCNGL() {
        return CNGL;
    }

    public void setCNGL(double CNGL) {
        this.CNGL = CNGL;
    }

    public double getGFGL() {
        return GFGL;
    }

    public void setGFGL(double GFGL) {
        this.GFGL = GFGL;
    }

    public double getFZGL1() {
        return FZGL1;
    }

    public void setFZGL1(double FZGL1) {
        this.FZGL1 = FZGL1;
    }

    public double getFZGL2() {
        return FZGL2;
    }

    public void setFZGL2(double FZGL2) {
        this.FZGL2 = FZGL2;
    }

    public double getFZGL3() {
        return FZGL3;
    }

    public void setFZGL3(double FZGL3) {
        this.FZGL3 = FZGL3;
    }

    public double getFZGL4() {
        return FZGL4;
    }

    public void setFZGL4(double FZGL4) {
        this.FZGL4 = FZGL4;
    }

    public double getFZGL5() {
        return FZGL5;
    }

    public void setFZGL5(double FZGL5) {
        this.FZGL5 = FZGL5;
    }

    public double getFZGL6() {
        return FZGL6;
    }

    public void setFZGL6(double FZGL6) {
        this.FZGL6 = FZGL6;
    }

    public double getSDWG() {
        return SDWG;
    }

    public void setSDWG(double SDWG) {
        this.SDWG = SDWG;
    }

    public double getCNWG() {
        return CNWG;
    }

    public void setCNWG(double CNWG) {
        this.CNWG = CNWG;
    }

    public double getGFWG() {
        return GFWG;
    }

    public void setGFWG(double GFWG) {
        this.GFWG = GFWG;
    }

    public double getFZWG1() {
        return FZWG1;
    }

    public void setFZWG1(double FZWG1) {
        this.FZWG1 = FZWG1;
    }

    public double getFZWG2() {
        return FZWG2;
    }

    public void setFZWG2(double FZWG2) {
        this.FZWG2 = FZWG2;
    }

    public double getFZWG3() {
        return FZWG3;
    }

    public void setFZWG3(double FZWG3) {
        this.FZWG3 = FZWG3;
    }

    public double getFZWG4() {
        return FZWG4;
    }

    public void setFZWG4(double FZWG4) {
        this.FZWG4 = FZWG4;
    }

    public double getFZWG5() {
        return FZWG5;
    }

    public void setFZWG5(double FZWG5) {
        this.FZWG5 = FZWG5;
    }

    public double getFZWG6() {
        return FZWG6;
    }

    public void setFZWG6(double FZWG6) {
        this.FZWG6 = FZWG6;
    }

    public double getSDYS() {
        return SDYS;
    }

    public void setSDYS(double SDYS) {
        this.SDYS = SDYS;
    }

    public double getCNYS() {
        return CNYS;
    }

    public void setCNYS(double CNYS) {
        this.CNYS = CNYS;
    }

    public double getGFYS() {
        return GFYS;
    }

    public void setGFYS(double GFYS) {
        this.GFYS = GFYS;
    }

    public double getFZYS1() {
        return FZYS1;
    }

    public void setFZYS1(double FZYS1) {
        this.FZYS1 = FZYS1;
    }

    public double getFZYS2() {
        return FZYS2;
    }

    public void setFZYS2(double FZYS2) {
        this.FZYS2 = FZYS2;
    }

    public double getFZYS3() {
        return FZYS3;
    }

    public void setFZYS3(double FZYS3) {
        this.FZYS3 = FZYS3;
    }

    public double getFZYS4() {
        return FZYS4;
    }

    public void setFZYS4(double FZYS4) {
        this.FZYS4 = FZYS4;
    }

    public double getFZYS5() {
        return FZYS5;
    }

    public void setFZYS5(double FZYS5) {
        this.FZYS5 = FZYS5;
    }

    public double getFZYS6() {
        return FZYS6;
    }

    public void setFZYS6(double FZYS6) {
        this.FZYS6 = FZYS6;
    }
}
