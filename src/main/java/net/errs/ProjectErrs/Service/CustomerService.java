package net.errs.ProjectErrs.Service;

import java.util.List;
import java.util.Optional;

import net.errs.ProjectErrs.Model.tblCustomers;
import net.errs.ProjectErrs.Repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepo;
    public CustomerService(CustomerRepository repo) {
        this.customerRepo = repo;
    }

    public List<tblCustomers> getAllCustomers() {
        return customerRepo.findAll();
    }

    public Optional<tblCustomers> getCustomerById(Long id) {
        return customerRepo.findById(id);
    }
}