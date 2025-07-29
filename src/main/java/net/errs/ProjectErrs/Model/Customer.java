package net.errs.ProjectErrs.Model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Customer {
    private int customerId;
    private Integer customerSourceId;
    private Integer customerTypeId;

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

    private LocalDate custBDay;
    private LocalDate spouseBDay;
    private String dlNo;
    private LocalDate custSince;
    private String resaleNo;

    private Boolean checkApp;
    private Boolean chargeApp;
    private BigDecimal creditLim;
    private String internalComments;
    private String defaultPricing;
    private Boolean taxExempt;

    private Boolean airReminderSent;
    private LocalDate airReminderDate;
    private Boolean oilChangeSent;
    private LocalDate oilChangeDate;
    private Boolean tuneUpSent;
    private LocalDate tuneUpDate;
    private Boolean transServiceSent;
    private LocalDate transServiceDate;
    private Boolean smogSent;
    private String smogDate; // still varchar(100) in DB
    private Boolean custWarnings;

    // Getters and setters can be generated automatically by your IDE
}