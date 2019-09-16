package ashley.origin_andriod_cabinetmonitoringinfo;

/**
 * Created by AshleyZHANG on 2019/8/13.
 */
public class HeatDatasList {
    int heatCurrent;    //加热电流 单位0.1A
    int heatVoltage;    //加热电压  单位0.1V
    int heatPower;      //加热功率  单位0.1W
    int temperPMS;      //PMS温度   单位0.1摄氏度
    int[] temperBatteries;   //电池温度，若没电池，赋值10000， 单位摄氏度
    int temperCharger;       //柜子充电器区域温度，单位0.1摄氏度
    int temperCabin;        //柜子上部分区域温度，单位0.1摄氏度
    int ammeterVoltage;      //电表电压，单位0.1V
    int ammeterCurrent;      //电表电流，单位mA
    int ammeterPower;        //电表有功总电能
    int ammeterInstantTotalPower;//瞬时总有功功率
    int enableTemper;        //允许自动调节的温度，单位摄氏度
    int disableTemper;       //禁止自动调节的温度，单位摄氏度
    boolean isAuto;          //是否安卓机自动调节
    boolean isEnable;        //是否允许固件调节
    boolean isValid;         //数据是否有效
    long recordTime;         //记录时间
    long meterTime;          //电表读数记录时间

    public int getHeatCurrent() {
        return heatCurrent;
    }

    public void setHeatCurrent(int heatCurrent) {
        this.heatCurrent = heatCurrent;
    }

    public int getHeatVoltage() {
        return heatVoltage;
    }

    public void setHeatVoltage(int heatVoltage) {
        this.heatVoltage = heatVoltage;
    }

    public int getHeatPower() {
        return heatPower;
    }

    public void setHeatPower(int heatPower) {
        this.heatPower = heatPower;
    }

    public int getTemperPMS() {
        return temperPMS;
    }

    public void setTemperPMS(int temperPMS) {
        this.temperPMS = temperPMS;
    }

    public int[] getTemperBatteries() {
        return temperBatteries;
    }

    public void setTemperBatteries(int[] temperBatteries) {
        this.temperBatteries = temperBatteries;
    }

    public int getTemperCharger() {
        return temperCharger;
    }

    public void setTemperCharger(int temperCharger) {
        this.temperCharger = temperCharger;
    }

    public int getTemperCabin() {
        return temperCabin;
    }

    public void setTemperCabin(int temperCabin) {
        this.temperCabin = temperCabin;
    }

    public int getAmmeterVoltage() {
        return ammeterVoltage;
    }

    public void setAmmeterVoltage(int ammeterVoltage) {
        this.ammeterVoltage = ammeterVoltage;
    }

    public int getAmmeterCurrent() {
        return ammeterCurrent;
    }

    public void setAmmeterCurrent(int ammeterCurrent) {
        this.ammeterCurrent = ammeterCurrent;
    }

    public int getAmmeterPower() {
        return ammeterPower;
    }

    public void setAmmeterPower(int ammeterPower) {
        this.ammeterPower = ammeterPower;
    }

    public int getAmmeterInstantTotalPower() {
        return ammeterInstantTotalPower;
    }

    public void setAmmeterInstantTotalPower(int ammeterInstantTotalPower) {
        this.ammeterInstantTotalPower = ammeterInstantTotalPower;
    }

    public int getEnableTemper() {
        return enableTemper;
    }

    public void setEnableTemper(int enableTemper) {
        this.enableTemper = enableTemper;
    }

    public int getDisableTemper() {
        return disableTemper;
    }

    public void setDisableTemper(int disableTemper) {
        this.disableTemper = disableTemper;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    public long getMeterTime() {
        return meterTime;
    }

    public void setMeterTime(long meterTime) {
        this.meterTime = meterTime;
    }
}
