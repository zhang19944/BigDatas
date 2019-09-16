package ashley.origin_andriod_cabinetmonitoringinfo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by AshleyZHANG on 2019/8/13.
 */

public class MonitorBean {
    /**{"heatDatas":[{"ammeterCurrent":5502,"ammeterPower":14005,"ammeterVoltage":2184,"disableTemper":45,"enableTemper":40,"heatCurrent":0,"heatPower":0,"heatVoltage":0,"isAuto":true,"isEnable":true,"isValid":false,"meterTime":1565488925423,"recordTime":1565488968194,"temperBatteries":[33,36,28,31,30,37,29,10000,10000,34,31,35,30],"temperCabin":325,"temperCharger":352,"temperPMS":0},
        {"ammeterCurrent":4299,"ammeterPower":14005,"ammeterVoltage":2197,"disableTemper":45,"enableTemper":40,"heatCurrent":0,"heatPower":0,"heatVoltage":0,"isAuto":true,"isEnable":true,"isValid":false,"meterTime":1565489044880,"recordTime":1565489088194,"temperBatteries":[33,36,28,31,30,37,29,10000,10000,34,31,35,30],"temperCabin":326,"temperCharger":352,"temperPMS":0},
        {"ammeterCurrent":4649,"ammeterPower":14005,"ammeterVoltage":2199,"disableTemper":45,"enableTemper":40,"heatCurrent":0,"heatPower":0,"heatVoltage":0,"isAuto":true,"isEnable":true,"isValid":false,"meterTime":1565489164495,"recordTime":1565489208213,"temperBatteries":[33,36,28,31,30,37,29,10000,10000,34,31,35,30],"temperCabin":326,"temperCharger":354,"temperPMS":0},
        {"ammeterCurrent":5429,"ammeterPower":14005,"ammeterVoltage":2186,"disableTemper":45,"enableTemper":40,"heatCurrent":0,"heatPower":0,"heatVoltage":0,"isAuto":true,"isEnable":true,"isValid":false,"meterTime":1565489284551,"recordTime":1565489328246,"temperBatteries":[33,36,28,31,30,38,29,10000,10000,34,31,35,31],"temperCabin":327,"temperCharger":354,"temperPMS":0},
        {"ammeterCurrent":3707,"ammeterPower":14005,"ammeterVoltage":2204,"disableTemper":45,"enableTemper":40,"heatCurrent":0,"heatPower":0,"heatVoltage":0,"isAuto":true,"isEnable":true,"isValid":false,"meterTime":1565489404833,"recordTime":1565489448265,"temperBatteries":[34,36,28,31,30,38,29,10000,10000,34,31,35,31],"temperCabin":328,"temperCharger":356,"temperPMS":0},
        {"ammeterCurrent":5116,"ammeterPower":14005,"ammeterVoltage":2215,"disableTemper":45,"enableTemper":40,"heatCurrent":0,"heatPower":0,"heatVoltage":0,"isAuto":true,"isEnable":true,"isValid":false,"meterTime":1565489524783,"recordTime":1565489568288,"temperBatteries":[34,36,28,31,30,38,29,10000,10000,34,31,35,31],"temperCabin":328,"temperCharger":353,"temperPMS":0},
        {"ammeterCurrent":2236,"ammeterPower":14005,"ammeterVoltage":2203,"disableTemper":45,"enableTemper":40,"heatCurrent":0,"heatPower":0,"heatVoltage":0,"isAuto":true,"isEnable":true,"isValid":false,"meterTime":1565489644694,"recordTime":1565489688311,"temperBatteries":[34,36,28,31,30,38,30,10000,10000,33,31,35,31],"temperCabin":329,"temperCharger":358,"temperPMS":0},
        {"ammeterCurrent":2240,"ammeterPower":14005,"ammeterVoltage":2193,"disableTemper":45,"enableTemper":40,"heatCurrent":0,"heatPower":0,"heatVoltage":0,"isAuto":true,"isEnable":true,"isValid":false,"meterTime":1565489765302,"recordTime":1565489808344,"temperBatteries":[34,36,28,31,30,38,30,10000,10000,33,31,35,31],"temperCabin":330,"temperCharger":356,"temperPMS":0},
        {"ammeterCurrent":4623,"ammeterPower":14005,"ammeterVoltage":2207,"disableTemper":45,"enableTemper":40,"heatCurrent":0,"heatPower":0,"heatVoltage":0,"isAuto":true,"isEnable":true,"isValid":false,"meterTime":1565489884680,"recordTime":1565489928382,"temperBatteries":[34,36,28,31,30,38,30,10000,10000,33,31,35,31],"temperCabin":330,"temperCharger":359,"temperPMS":0},
        {"ammeterCurrent":4453,"ammeterPower":14005,"ammeterVoltage":2201,"disableTemper":45,"enableTemper":40,"heatCurrent":0,"heatPower":0,"heatVoltage":0,"isAuto":true,"isEnable":true,"isValid":false,"meterTime":1565490005469,"recordTime":1565490048397,"temperBatteries":[34,36,10000,31,30,38,30,31,10000,33,31,35,31],"temperCabin":329,"temperCharger":353,"temperPMS":0},
        {"ammeterCurrent":8178,"ammeterPower":14005,"ammeterVoltage":2157,"disableTemper":45,"enableTemper":40,"heatCurrent":0,"heatPower":0,"heatVoltage":0,"isAuto":true,"isEnable":true,"isValid":false,"meterTime":1565490124858,"recordTime":1565490168427,"temperBatteries":[34,36,10000,31,30,38,30,31,10000,33,31,35,31],"temperCabin":330,"temperCharger":352,"temperPMS":0},
        {"ammeterCurrent":7425,"ammeterPower":14005,"ammeterVoltage":2156,"disableTemper":45,"enableTemper":40,"heatCurrent":0,"heatPower":0,"heatVoltage":0,"isAuto":true,"isEnable":true,"isValid":false,"meterTime":1565490244945,"recordTime":1565490288453,"temperBatteries":[34,36,10000,31,30,38,30,31,10000,33,31,35,31],"temperCabin":332,"temperCharger":352,"temperPMS":0},
        {"ammeterCurrent":6656,"ammeterPower":14005,"ammeterVoltage":2157,"disableTemper":45,"enableTemper":40,"heatCurrent":0,"heatPower":0,"heatVoltage":0,"isAuto":true,"isEnable":true,"isValid":false,"meterTime":1565490364946,"recordTime":1565490408477,"temperBatteries":[34,36,10000,31,30,38,30,31,10000,33,31,35,31],"temperCabin":332,"temperCharger":354,"temperPMS":0},
        {"ammeterCurrent":6595,"ammeterPower":14005,"ammeterVoltage":2153,"disableTemper":45,"enableTemper":40,"heatCurrent":0,"heatPower":0,"heatVoltage":0,"isAuto":true,"isEnable":true,"isValid":false,"meterTime":1565490484985,"recordTime":1565490528521,"temperBatteries":[34,36,31,31,30,38,30,31,10000,10000,31,35,31],"temperCabin":333,"temperCharger":354,"temperPMS":0},
        {"ammeterCurrent":4718,"ammeterPower":14005,"ammeterVoltage":2156,"disableTemper":45,"enableTemper":40,"heatCurrent":0,"heatPower":0,"heatVoltage":0,"isAuto":true,"isEnable":true,"isValid":false,"meterTime":1565490604920,"recordTime":1565490648547,"temperBatteries":[34,36,31,31,30,38,30,31,10000,10000,31,35,31],"temperCabin":334,"temperCharger":355,"temperPMS":0}],
        "pID":"5a9f8c5a03623c24"}
     **/

    private  String pID;
    private List<HeatDatasList> heatDatas;

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public List<HeatDatasList> getHeatDatas() {
        return heatDatas;
    }

    public void setHeatDatas(List<HeatDatasList> heatDatas) {
        this.heatDatas = heatDatas;
    }
}