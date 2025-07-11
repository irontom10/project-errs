package net.errs.ProjectErrs.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Customer {
    private Long customerID;
    private Long sourceID;
    private String typeID;
    private String companyName;
    private String contactLastName;
    private String contactFirstName;
    private String contactSpouse;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String sCompanyName;
    private String sContactLastName;
    private String sContactFirstName;
    private String sAddress;
    private String sCity;
    private String sState;
    private String sPostalCode;
    private String phoneNumber;
    private String phoneExt;
    private String faxNumber;
    private String workPhone;
    private String workExt;
    private String pagerPhone;
    private String cellPhone;
    private String emailAddr;
    private LocalDateTime custBDay;
    private LocalDateTime spouseBDay;
    private String dlNo;
    private LocalDateTime custSince;
    private String resaleNo;
    private boolean checkApp;
    private boolean chargeApp;
    private BigDecimal creditLim;
    private String internalComments;
    private String defaultPricing;
    private boolean taxExempt;
    private boolean airReminderSent;
    private LocalDateTime airReminderDate;
    private boolean oilChangeSent;
    private LocalDateTime oilChangeDate;
    private boolean tuneUpSent;
    private LocalDateTime tuneUpDate;
    private boolean transServiceSent;
    private LocalDateTime transServiceDate;
    private boolean smogSent;
    private String smogDate;
    private boolean custWarnings;

    // use alt + insert to auto generate constructors, getters, and setters

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    public String getContactSpouse() {
        return contactSpouse;
    }

    public void setContactSpouse(String contactSpouse) {
        this.contactSpouse = contactSpouse;
    }

    public Long getSourceID() {
        return sourceID;
    }

    public void setSourceID(Long sourceID) {
        this.sourceID = sourceID;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getsCompanyName() {
        return sCompanyName;
    }

    public void setsCompanyName(String sCompanyName) {
        this.sCompanyName = sCompanyName;
    }

    public String getsContactLastName() {
        return sContactLastName;
    }

    public void setsContactLastName(String sContactLastName) {
        this.sContactLastName = sContactLastName;
    }

    public String getsContactFirstName() {
        return sContactFirstName;
    }

    public void setsContactFirstName(String sContactFirstName) {
        this.sContactFirstName = sContactFirstName;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getsCity() {
        return sCity;
    }

    public void setsCity(String sCity) {
        this.sCity = sCity;
    }

    public String getsState() {
        return sState;
    }

    public void setsState(String sState) {
        this.sState = sState;
    }

    public String getsPostalCode() {
        return sPostalCode;
    }

    public void setsPostalCode(String sPostalCode) {
        this.sPostalCode = sPostalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneExt() {
        return phoneExt;
    }

    public void setPhoneExt(String phoneExt) {
        this.phoneExt = phoneExt;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getWorkExt() {
        return workExt;
    }

    public void setWorkExt(String workExt) {
        this.workExt = workExt;
    }

    public String getPagerPhone() {
        return pagerPhone;
    }

    public void setPagerPhone(String pagerPhone) {
        this.pagerPhone = pagerPhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public LocalDateTime getCustBDay() {
        return custBDay;
    }

    public void setCustBDay(LocalDateTime custBDay) {
        this.custBDay = custBDay;
    }

    public LocalDateTime getSpouseBDay() {
        return spouseBDay;
    }

    public void setSpouseBDay(LocalDateTime spouseBDay) {
        this.spouseBDay = spouseBDay;
    }

    public String getDlNo() {
        return dlNo;
    }

    public void setDlNo(String dlNo) {
        this.dlNo = dlNo;
    }

    public LocalDateTime getCustSince() {
        return custSince;
    }

    public void setCustSince(LocalDateTime custSince) {
        this.custSince = custSince;
    }

    public String getResaleNo() {
        return resaleNo;
    }

    public void setResaleNo(String resaleNo) {
        this.resaleNo = resaleNo;
    }

    public boolean isCheckApp() {
        return checkApp;
    }

    public void setCheckApp(boolean checkApp) {
        this.checkApp = checkApp;
    }

    public boolean isChargeApp() {
        return chargeApp;
    }

    public void setChargeApp(boolean chargeApp) {
        this.chargeApp = chargeApp;
    }

    public BigDecimal getCreditLim() {
        return creditLim;
    }

    public void setCreditLim(BigDecimal creditLim) {
        this.creditLim = creditLim;
    }

    public String getInternalComments() {
        return internalComments;
    }

    public void setInternalComments(String internalComments) {
        this.internalComments = internalComments;
    }

    public String getDefaultPricing() {
        return defaultPricing;
    }

    public void setDefaultPricing(String defaultPricing) {
        this.defaultPricing = defaultPricing;
    }

    public boolean isTaxExempt() {
        return taxExempt;
    }

    public void setTaxExempt(boolean taxExempt) {
        this.taxExempt = taxExempt;
    }

    public boolean isAirReminderSent() {
        return airReminderSent;
    }

    public void setAirReminderSent(boolean airReminderSent) {
        this.airReminderSent = airReminderSent;
    }

    public LocalDateTime getAirReminderDate() {
        return airReminderDate;
    }

    public void setAirReminderDate(LocalDateTime airReminderDate) {
        this.airReminderDate = airReminderDate;
    }

    public boolean isOilChangeSent() {
        return oilChangeSent;
    }

    public void setOilChangeSent(boolean oilChangeSent) {
        this.oilChangeSent = oilChangeSent;
    }

    public LocalDateTime getOilChangeDate() {
        return oilChangeDate;
    }

    public void setOilChangeDate(LocalDateTime oilChangeDate) {
        this.oilChangeDate = oilChangeDate;
    }

    public boolean isTuneUpSent() {
        return tuneUpSent;
    }

    public void setTuneUpSent(boolean tuneUpSent) {
        this.tuneUpSent = tuneUpSent;
    }

    public LocalDateTime getTuneUpDate() {
        return tuneUpDate;
    }

    public void setTuneUpDate(LocalDateTime tuneUpDate) {
        this.tuneUpDate = tuneUpDate;
    }

    public boolean isTransServiceSent() {
        return transServiceSent;
    }

    public void setTransServiceSent(boolean transServiceSent) {
        this.transServiceSent = transServiceSent;
    }

    public LocalDateTime getTransServiceDate() {
        return transServiceDate;
    }

    public void setTransServiceDate(LocalDateTime transServiceDate) {
        this.transServiceDate = transServiceDate;
    }

    public boolean isSmogSent() {
        return smogSent;
    }

    public void setSmogSent(boolean smogSent) {
        this.smogSent = smogSent;
    }

    public String getSmogDate() {
        return smogDate;
    }

    public void setSmogDate(String smogDate) {
        this.smogDate = smogDate;
    }

    public boolean isCustWarnings() {
        return custWarnings;
    }

    public void setCustWarnings(boolean custWarnings) {
        this.custWarnings = custWarnings;
    }
}