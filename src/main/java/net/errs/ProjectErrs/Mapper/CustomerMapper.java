package net.errs.ProjectErrs.Mapper;

import com.healthmarketscience.jackcess.Row;
import net.errs.ProjectErrs.Model.Customer;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class CustomerMapper {

    public static Customer rowToCustomer(Row row) {
        Customer c = new Customer();

        c.setCustomerID(getLong(row, "CustomerID"));
        c.setSourceID(getLong(row, "SourceID"));
        c.setTypeID((String) row.get("TypeID"));
        c.setCompanyName((String) row.get("CompanyName"));
        c.setContactLastName((String) row.get("ContactLastName"));
        c.setContactFirstName((String) row.get("ContactFirstName"));
        c.setContactSpouse((String) row.get("ContactSpouse"));
        c.setAddress((String) row.get("Address"));
        c.setCity((String) row.get("City"));
        c.setState((String) row.get("State"));
        c.setPostalCode((String) row.get("PostalCode"));
        c.setsCompanyName((String) row.get("SCompanyName"));
        c.setsContactLastName((String) row.get("SContactLastName"));
        c.setContactFirstName((String) row.get("SContactFirstName"));
        c.setsAddress((String) row.get("SAddress"));
        c.setsCity((String) row.get("SCity"));
        c.setsState((String) row.get("SState"));
        c.setsPostalCode((String) row.get("SPostalCode"));
        c.setPhoneNumber((String) row.get("PhoneNumber"));
        c.setPhoneExt((String) row.get("PhoneExt"));
        c.setFaxNumber((String) row.get("FaxNumber"));
        c.setWorkPhone((String) row.get("WorkPhone"));
        c.setWorkExt((String) row.get("WorkExt"));
        c.setPagerPhone((String) row.get("PagerPhone"));
        c.setCellPhone((String) row.get("CellPhone"));
        c.setEmailAddr((String) row.get("EmailAddr"));
        c.setCustBDay(toLocalDateTime(row.get("CustBDay")));
        c.setSpouseBDay(toLocalDateTime(row.get("SpouseBDay")));
        c.setDlNo((String) row.get("DLNo"));
        c.setCustSince(toLocalDateTime(row.get("CustSince")));
        c.setResaleNo((String) row.get("ResaleNo"));
        c.setCheckApp(Boolean.TRUE.equals(row.get("CheckApp")));
        c.setChargeApp(Boolean.TRUE.equals(row.get("ChargeApp")));
        c.setCreditLim(toBigDecimal(row.get("CreditLim")));
        c.setInternalComments((String) row.get("InternalComments"));
        c.setDefaultPricing((String) row.get("DefaultPricing"));
        c.setTaxExempt(Boolean.TRUE.equals(row.get("TaxExempt")));
        c.setAirReminderSent(Boolean.TRUE.equals(row.get("AirReminderSent")));
        c.setAirReminderDate(toLocalDateTime(row.get("AirReminderDate")));
        c.setOilChangeSent(Boolean.TRUE.equals(row.get("OilChangeSent")));
        c.setOilChangeDate(toLocalDateTime(row.get("OilChangeDate")));
        c.setTuneUpSent(Boolean.TRUE.equals(row.get("TuneUpSent")));
        c.setTuneUpDate(toLocalDateTime(row.get("TuneUpDate")));
        c.setTransServiceSent(Boolean.TRUE.equals(row.get("TransServiceSent")));
        c.setTransServiceDate(toLocalDateTime(row.get("TransServiceDate")));
        c.setSmogSent(Boolean.TRUE.equals(row.get("SmogSent")));
        c.setSmogDate((String) row.get("SmogDate"));
        c.setCustWarnings(Boolean.TRUE.equals(row.get("CustWarnings")));

        return c;
    }

    private static LocalDateTime toLocalDateTime(Object value) {
        if (value instanceof Date) {
            return Instant.ofEpochMilli(((Date) value).getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        }
        return null;
    }

    private static BigDecimal toBigDecimal(Object value) {
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        return null;
    }

    private static Long getLong(Row row, String fieldName) {
        Object val = row.get(fieldName);
        return val instanceof Number ? ((Number) val).longValue() : null;
    }
}