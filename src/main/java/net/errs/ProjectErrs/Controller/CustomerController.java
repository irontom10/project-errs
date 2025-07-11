package net.errs.ProjectErrs.Controller;

import net.errs.ProjectErrs.Model.Customer;
import net.errs.ProjectErrs.Service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Service
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAllCustomers() throws IOException {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) throws IOException {
        Customer customer = customerService.getCustomerById(id);
        return (customer != null)
                ? ResponseEntity.ok(customer)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer) throws IOException {
        customerService.createCustomer(customer);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) throws IOException {
        boolean updated = customerService.updateCustomer(id, customer);
        return updated
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) throws IOException {
        boolean deleted = customerService.deleteCustomer(id);
        return deleted
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}