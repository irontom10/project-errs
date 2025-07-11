package net.errs.ProjectErrs.Service;

import net.errs.ProjectErrs.Model.Customer;
import net.errs.ProjectErrs.Repo.CustomerRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() throws IOException {
        return customerRepository.getAllCustomers();
    }

    public Customer getCustomerById(Long id) throws IOException {
        return customerRepository.getCustomerById(id);
    }

    public void createCustomer(Customer customer) throws IOException {
        customerRepository.insertCustomer(customer);
    }

    public boolean updateCustomer(Long id, Customer customer) throws IOException {
        return customerRepository.updateCustomer(id, customer);
    }

    public boolean deleteCustomer(Long id) throws IOException {
        return customerRepository.deleteCustomer(id);
    }
}