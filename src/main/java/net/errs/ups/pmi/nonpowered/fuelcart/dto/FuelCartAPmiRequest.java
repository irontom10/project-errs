package net.errs.ups.pmi.nonpowered.fuelcart.dto;

import java.util.HashMap;
import java.util.Map;

public class FuelCartAPmiRequest {

    private String location = "ABY";
    private String unitNo = "FUEL-CART-001";
    private String dateCompleted;

    // optional future use
    private String technicianSignature; // data:image/png;base64,...
    private String supervisorSignature; // data:image/png;base64,...
    private Long workorderId = 17000L;

    private String rfTireDepth = "10";
    private String lfTireDepth = "10";
    private String lrTireDepth = "10";
    private String rrTireDepth = "10";

    private String rfTirePressureChecked = "100";
    private String lfTirePressureChecked = "100";
    private String lrTirePressureChecked = "100";
    private String rrTirePressureChecked = "100";

    private String rfTirePressureCorrected = "100";
    private String lfTirePressureCorrected = "100";
    private String lrTirePressureCorrected = "100";
    private String rrTirePressureCorrected = "100";

    /**
     * Item values keyed by displayed inspection item id.
     *
     * Suggested values:
     * 2 = OK
     * 3 = Adjusted
     * 4 = Repaired
     * 5 = Repair Needed
     *
     * Form item ids used here:
     * 1, 38, 40, 41, 9, 37, 29, 31, 32, 33, 16, 35, 46, 65, 36, 51,
     * 58, 20, 53, 22, 24, 59, 27, 39, 30, 61, 62, 63, 66, 67, 60, 50,
     * 52, 54, 56, 71
     */
    private Map<String, String> itemValues = new HashMap<>();

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getUnitNo() { return unitNo; }
    public void setUnitNo(String unitNo) { this.unitNo = unitNo; }

    public String getDateCompleted() { return dateCompleted; }
    public void setDateCompleted(String dateCompleted) { this.dateCompleted = dateCompleted; }

    public String getTechnicianSignature() { return technicianSignature; }
    public void setTechnicianSignature(String technicianSignature) { this.technicianSignature = technicianSignature; }

    public String getSupervisorSignature() { return supervisorSignature; }
    public void setSupervisorSignature(String supervisorSignature) { this.supervisorSignature = supervisorSignature; }

    public Long getWorkorderId() { return workorderId; }
    public void setWorkorderId(Long workorderId) { this.workorderId = workorderId; }

    public String getRfTireDepth() { return rfTireDepth; }
    public void setRfTireDepth(String rfTireDepth) { this.rfTireDepth = rfTireDepth; }

    public String getLfTireDepth() { return lfTireDepth; }
    public void setLfTireDepth(String lfTireDepth) { this.lfTireDepth = lfTireDepth; }

    public String getLrTireDepth() { return lrTireDepth; }
    public void setLrTireDepth(String lrTireDepth) { this.lrTireDepth = lrTireDepth; }

    public String getRrTireDepth() { return rrTireDepth; }
    public void setRrTireDepth(String rrTireDepth) { this.rrTireDepth = rrTireDepth; }

    public String getRfTirePressureChecked() { return rfTirePressureChecked; }
    public void setRfTirePressureChecked(String rfTirePressureChecked) { this.rfTirePressureChecked = rfTirePressureChecked; }

    public String getLfTirePressureChecked() { return lfTirePressureChecked; }
    public void setLfTirePressureChecked(String lfTirePressureChecked) { this.lfTirePressureChecked = lfTirePressureChecked; }

    public String getLrTirePressureChecked() { return lrTirePressureChecked; }
    public void setLrTirePressureChecked(String lrTirePressureChecked) { this.lrTirePressureChecked = lrTirePressureChecked; }

    public String getRrTirePressureChecked() { return rrTirePressureChecked; }
    public void setRrTirePressureChecked(String rrTirePressureChecked) { this.rrTirePressureChecked = rrTirePressureChecked; }

    public String getRfTirePressureCorrected() { return rfTirePressureCorrected; }
    public void setRfTirePressureCorrected(String rfTirePressureCorrected) { this.rfTirePressureCorrected = rfTirePressureCorrected; }

    public String getLfTirePressureCorrected() { return lfTirePressureCorrected; }
    public void setLfTirePressureCorrected(String lfTirePressureCorrected) { this.lfTirePressureCorrected = lfTirePressureCorrected; }

    public String getLrTirePressureCorrected() { return lrTirePressureCorrected; }
    public void setLrTirePressureCorrected(String lrTirePressureCorrected) { this.lrTirePressureCorrected = lrTirePressureCorrected; }

    public String getRrTirePressureCorrected() { return rrTirePressureCorrected; }
    public void setRrTirePressureCorrected(String rrTirePressureCorrected) { this.rrTirePressureCorrected = rrTirePressureCorrected; }

    public Map<String, String> getItemValues() { return itemValues; }
    public void setItemValues(Map<String, String> itemValues) { this.itemValues = itemValues; }
}