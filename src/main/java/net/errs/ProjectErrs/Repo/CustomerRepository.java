package net.errs.ProjectErrs.Repo;

import com.healthmarketscience.jackcess.*;
import jakarta.annotation.PostConstruct;
import net.errs.ProjectErrs.Model.Customer;
import net.errs.ProjectErrs.Mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Repository
public class CustomerRepository {

    @Value("${app.mdb.filepath}")
    private String mdbPath;

    private Database db;

    @PostConstruct
    public void init() throws IOException {
        File mdbFile = new File(mdbPath);
        this.db = DatabaseBuilder.open(mdbFile);
    }

    public List<Customer> getAllCustomers() throws IOException {
        List<Customer> customers = new ArrayList<>();
        Table table = db.getTable("tblCustomers");

        for (Row row : table) {
            customers.add(CustomerMapper.rowToCustomer(row));
        }

        return customers;
    }

    public Customer getCustomerById(Long customerId) throws IOException {
        Table table = db.getTable("tblCustomers");

        for (Row row : table) {
            if (Objects.equals(row.get("CustomerID"), customerId)) {
                return CustomerMapper.rowToCustomer(row);
            }
        }

        return null;
    }

    public void insertCustomer(Customer customer) throws IOException {
        Table table = db.getTable("tblCustomers");
        table.addRow(customerToRow(customer));
    }

    public boolean updateCustomer(Long customerId, Customer updated) throws IOException {
        Table table = db.getTable("tblCustomers");

        for (Row row : table) {
            if (Objects.equals(row.get("CustomerID"), customerId)) {
                table.updateRow(row);
                return true;
            }
        }

        return false;
    }

    public boolean deleteCustomer(Long customerId) throws IOException {
        Table table = db.getTable("tblCustomers");

        for (Row row : table) {
            if (Objects.equals(row.get("CustomerID"), customerId)) {
                table.deleteRow(row);
                return true;
            }
        }

        return false;
    }

    private Map<String, Object> customerToRow(Customer c) {
        Map<String, Object> row = new LinkedHashMap<>();

        row.put("CustomerID", c.getCustomerID());
        row.put("SourceID", c.getSourceID());
        row.put("TypeID", c.getTypeID());
        row.put("CompanyName", c.getCompanyName());
        row.put("ContactLastName", c.getContactLastName());
        row.put("ContactFirstName", c.getContactFirstName());
        row.put("ContactSpouse", c.getContactSpouse());
        row.put("Address", c.getAddress());
        row.put("City", c.getCity());
        row.put("State", c.getState());
        row.put("PostalCode", c.getPostalCode());
        row.put("SCompanyName", c.getsCompanyName());
        row.put("SContactLastName", c.getsContactLastName());
        row.put("SContactFirstName", c.getsContactFirstName());
        row.put("SAddress", c.getsAddress());
        row.put("SCity", c.getsCity());
        row.put("SState", c.getsState());
        row.put("SPostalCode", c.getsPostalCode());
        row.put("PhoneNumber", c.getPhoneNumber());
        row.put("PhoneExt", c.getPhoneExt());
        row.put("FaxNumber", c.getFaxNumber());
        row.put("WorkPhone", c.getWorkPhone());
        row.put("WorkExt", c.getWorkExt());
        row.put("PagerPhone", c.getPagerPhone());
        row.put("CellPhone", c.getCellPhone());
        row.put("EmailAddr", c.getEmailAddr());
        row.put("CustBDay", toDate(c.getCustBDay()));
        row.put("SpouseBDay", toDate(c.getSpouseBDay()));
        row.put("DLNo", c.getDlNo());
        row.put("CustSince", toDate(c.getCustSince()));
        row.put("ResaleNo", c.getResaleNo());
        row.put("CheckApp", c.isCheckApp());
        row.put("ChargeApp", c.isChargeApp());
        row.put("CreditLim", c.getCreditLim());
        row.put("InternalComments", c.getInternalComments());
        row.put("DefaultPricing", c.getDefaultPricing());
        row.put("TaxExempt", c.isTaxExempt());
        row.put("AirReminderSent", c.isAirReminderSent());
        row.put("AirReminderDate", toDate(c.getAirReminderDate()));
        row.put("OilChangeSent", c.isOilChangeSent());
        row.put("OilChangeDate", toDate(c.getOilChangeDate()));
        row.put("TuneUpSent", c.isTuneUpSent());
        row.put("TuneUpDate", toDate(c.getTuneUpDate()));
        row.put("TransServiceSent", c.isTransServiceSent());
        row.put("TransServiceDate", toDate(c.getTransServiceDate()));
        row.put("SmogSent", c.isSmogSent());
        row.put("SmogDate", c.getSmogDate());
        row.put("CustWarnings", c.isCustWarnings());

        return row;
    }

    private Date toDate(LocalDateTime dt) {
        return dt == null ? null : Date.from(dt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
