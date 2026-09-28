package net.errs.internal.util.cummins.dto;

import java.util.ArrayList;
import java.util.List;

public class CumminsFaultDump {
    private String sourceFileName = "";
    private String engineSerialNumber = "";
    private String customerUnitNumber = "";
    private String ecmTime = "";
    private String engineHours = "";
    private String keyoffs = "";
    private final List<CumminsFault> faults = new ArrayList<>();

    public String getSourceFileName() { return sourceFileName; }
    public void setSourceFileName(String sourceFileName) { this.sourceFileName = value(sourceFileName); }
    public String getEngineSerialNumber() { return engineSerialNumber; }
    public void setEngineSerialNumber(String engineSerialNumber) { this.engineSerialNumber = value(engineSerialNumber); }
    public String getCustomerUnitNumber() { return customerUnitNumber; }
    public void setCustomerUnitNumber(String customerUnitNumber) { this.customerUnitNumber = value(customerUnitNumber); }
    public String getEcmTime() { return ecmTime; }
    public void setEcmTime(String ecmTime) { this.ecmTime = value(ecmTime); }
    public String getEngineHours() { return engineHours; }
    public void setEngineHours(String engineHours) { this.engineHours = value(engineHours); }
    public String getKeyoffs() { return keyoffs; }
    public void setKeyoffs(String keyoffs) { this.keyoffs = value(keyoffs); }
    public List<CumminsFault> getFaults() { return faults; }

    public int getActiveFaultCount() {
        return (int) faults.stream().filter(CumminsFault::isActive).count();
    }

    public int getInactiveFaultCount() {
        return (int) faults.stream().filter(f -> !f.isActive()).count();
    }

    public int getRedFaultCount() {
        return (int) faults.stream().filter(f -> "red".equals(f.getLampKey())).count();
    }

    public int getTotalOccurrences() {
        return faults.stream().mapToInt(CumminsFault::getCount).sum();
    }

    private static String value(String value) {
        return value == null ? "" : value.trim();
    }
}
