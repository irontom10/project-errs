package net.errs.ups.pmi.powered.cargotractor.dto;

import java.util.HashMap;
import java.util.Map;

public class CargoTractorGasABRequest {

    private String location = "ABY";
    private String unitNo = "496342";
    private String dateCompleted; // ISO string or anything parsable
    private String signature;     // data:image/png;base64,...
    private Long workorderId = 17000L;

    private String pmi = "A";
    private String gaugeHours = "635";

    private String steeringPlay = "0";
    private String kingPinPlay = "0";

    private String rfTireDepth = "10";
    private String lfTireDepth = "10";
    private String rrTireDepth = "10";
    private String lrTireDepth = "10";

    private String rfTirePressureChecked = "10";
    private String lfTirePressureChecked = "10";
    private String rrTirePressureChecked = "10";
    private String lrTirePressureChecked = "10";

    private String rfTirePressureCorrected = "10";
    private String lfTirePressureCorrected = "10";
    private String rrTirePressureCorrected = "10";
    private String lrTirePressureCorrected = "10";

    private String qtsOil = "8";

    private String coolantProtection = "8";
    private String coolantCondition = "8";

    private String batteryLoadTestVoltage = "12";
    private String battCableDropPos = "0";
    private String battCableDropNeg = "0";
    private String battIgnSwDrop = "0";

    private String initTiming = "7";
    private String mechanicalAdvTiming = "11";
    private String totalAdvTiming = "18";

    private String altFullOutputVolts = "68";
    private String altFullOutputAmps = "68";

    private String thermostatOpening = "170";

    /**
     * Inspection item values keyed by id like:
     * 101=2, 102=4, 110.1=2, 614.7=1, etc.
     */
    private Map<String, String> itemValues = new HashMap<>();

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getUnitNo() { return unitNo; }
    public void setUnitNo(String unitNo) { this.unitNo = unitNo; }

    public String getDateCompleted() { return dateCompleted; }
    public void setDateCompleted(String dateCompleted) { this.dateCompleted = dateCompleted; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public Long getWorkorderId() { return workorderId; }
    public void setWorkorderId(Long workorderId) { this.workorderId = workorderId; }

    public String getPmi() { return pmi; }
    public void setPmi(String pmi) { this.pmi = pmi; }

    public String getGaugeHours() { return gaugeHours; }
    public void setGaugeHours(String gaugeHours) { this.gaugeHours = gaugeHours; }

    public String getSteeringPlay() { return steeringPlay; }
    public void setSteeringPlay(String steeringPlay) { this.steeringPlay = steeringPlay; }

    public String getKingPinPlay() { return kingPinPlay; }
    public void setKingPinPlay(String kingPinPlay) { this.kingPinPlay = kingPinPlay; }

    public String getRfTireDepth() { return rfTireDepth; }
    public void setRfTireDepth(String rfTireDepth) { this.rfTireDepth = rfTireDepth; }

    public String getLfTireDepth() { return lfTireDepth; }
    public void setLfTireDepth(String lfTireDepth) { this.lfTireDepth = lfTireDepth; }

    public String getRrTireDepth() { return rrTireDepth; }
    public void setRrTireDepth(String rrTireDepth) { this.rrTireDepth = rrTireDepth; }

    public String getLrTireDepth() { return lrTireDepth; }
    public void setLrTireDepth(String lrTireDepth) { this.lrTireDepth = lrTireDepth; }

    public String getRfTirePressureChecked() { return rfTirePressureChecked; }
    public void setRfTirePressureChecked(String rfTirePressureChecked) { this.rfTirePressureChecked = rfTirePressureChecked; }

    public String getLfTirePressureChecked() { return lfTirePressureChecked; }
    public void setLfTirePressureChecked(String lfTirePressureChecked) { this.lfTirePressureChecked = lfTirePressureChecked; }

    public String getRrTirePressureChecked() { return rrTirePressureChecked; }
    public void setRrTirePressureChecked(String rrTirePressureChecked) { this.rrTirePressureChecked = rrTirePressureChecked; }

    public String getLrTirePressureChecked() { return lrTirePressureChecked; }
    public void setLrTirePressureChecked(String lrTirePressureChecked) { this.lrTirePressureChecked = lrTirePressureChecked; }

    public String getRfTirePressureCorrected() { return rfTirePressureCorrected; }
    public void setRfTirePressureCorrected(String rfTirePressureCorrected) { this.rfTirePressureCorrected = rfTirePressureCorrected; }

    public String getLfTirePressureCorrected() { return lfTirePressureCorrected; }
    public void setLfTirePressureCorrected(String lfTirePressureCorrected) { this.lfTirePressureCorrected = lfTirePressureCorrected; }

    public String getRrTirePressureCorrected() { return rrTirePressureCorrected; }
    public void setRrTirePressureCorrected(String rrTirePressureCorrected) { this.rrTirePressureCorrected = rrTirePressureCorrected; }

    public String getLrTirePressureCorrected() { return lrTirePressureCorrected; }
    public void setLrTirePressureCorrected(String lrTirePressureCorrected) { this.lrTirePressureCorrected = lrTirePressureCorrected; }

    public String getQtsOil() { return qtsOil; }
    public void setQtsOil(String qtsOil) { this.qtsOil = qtsOil; }

    public String getCoolantProtection() { return coolantProtection; }
    public void setCoolantProtection(String coolantProtection) { this.coolantProtection = coolantProtection; }

    public String getCoolantCondition() { return coolantCondition; }
    public void setCoolantCondition(String coolantCondition) { this.coolantCondition = coolantCondition; }

    public String getBatteryLoadTestVoltage() { return batteryLoadTestVoltage; }
    public void setBatteryLoadTestVoltage(String batteryLoadTestVoltage) { this.batteryLoadTestVoltage = batteryLoadTestVoltage; }

    public String getBattCableDropPos() { return battCableDropPos; }
    public void setBattCableDropPos(String battCableDropPos) { this.battCableDropPos = battCableDropPos; }

    public String getBattCableDropNeg() { return battCableDropNeg; }
    public void setBattCableDropNeg(String battCableDropNeg) { this.battCableDropNeg = battCableDropNeg; }

    public String getBattIgnSwDrop() { return battIgnSwDrop; }
    public void setBattIgnSwDrop(String battIgnSwDrop) { this.battIgnSwDrop = battIgnSwDrop; }

    public String getInitTiming() { return initTiming; }
    public void setInitTiming(String initTiming) { this.initTiming = initTiming; }

    public String getMechanicalAdvTiming() { return mechanicalAdvTiming; }
    public void setMechanicalAdvTiming(String mechanicalAdvTiming) { this.mechanicalAdvTiming = mechanicalAdvTiming; }

    public String getTotalAdvTiming() { return totalAdvTiming; }
    public void setTotalAdvTiming(String totalAdvTiming) { this.totalAdvTiming = totalAdvTiming; }

    public String getAltFullOutputVolts() { return altFullOutputVolts; }
    public void setAltFullOutputVolts(String altFullOutputVolts) { this.altFullOutputVolts = altFullOutputVolts; }

    public String getAltFullOutputAmps() { return altFullOutputAmps; }
    public void setAltFullOutputAmps(String altFullOutputAmps) { this.altFullOutputAmps = altFullOutputAmps; }

    public String getThermostatOpening() { return thermostatOpening; }
    public void setThermostatOpening(String thermostatOpening) { this.thermostatOpening = thermostatOpening; }

    public Map<String, String> getItemValues() { return itemValues; }
    public void setItemValues(Map<String, String> itemValues) { this.itemValues = itemValues; }
}