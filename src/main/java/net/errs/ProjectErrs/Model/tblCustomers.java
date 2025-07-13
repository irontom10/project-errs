package net.errs.ProjectErrs.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Getter
@Setter
public class tblCustomers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CustomerID;

    @ManyToOne
    @JoinColumn(
            name = "SourceID",
            referencedColumnName = "SourceID",
            foreignKey = @ForeignKey(name = "fk_customer_source")
    )
    private tblCustomerSource source;
    private String typeID;
    private String companyName;
    private String contactLastName;
    private String contactFirstName;
    private String contactSpouse;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    private String sCompanyName;
    private String sContactLastName;
    private String sContactFirstName;
    private String sAddress;
    private String sCity;
    private String sState;
    private String sPostalCode;
    private String sCountry;

    private String phoneNumber;
    private String phoneExt;
    private String faxNumber;
    private String workPhone;
    private String workExt;
    private String pagerPhone;
    private String cellPhone;
    private String emailAddr;

    @Temporal(TemporalType.DATE)
    private Date custBDay;

    @Temporal(TemporalType.DATE)
    private Date spouseBDay;

    private String dlNo;

    @Temporal(TemporalType.DATE)
    private Date custSince;

    private String resaleNo;

    private boolean checkApp;
    private boolean chargeApp;
    private BigDecimal creditLim;
    private String internalComments;
    private String defaultPricing;
    private boolean taxExempt;
    private boolean airReminderSent;

    @Temporal(TemporalType.DATE)
    private Date airReminderDate;

    private boolean oilChangeSent;

    @Temporal(TemporalType.DATE)
    private Date oilChangeDate;

    private boolean tuneUpSent;

    @Temporal(TemporalType.DATE)
    private Date tuneUpDate;

    private boolean transServiceSent;

    @Temporal(TemporalType.DATE)
    private Date transServiceDate;

    private boolean smogSent;

    private String smogDate;

    private boolean custWarnings;

}