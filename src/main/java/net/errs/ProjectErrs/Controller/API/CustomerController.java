package net.errs.ProjectErrs.Controller.API;

import java.util.List;

import net.errs.ProjectErrs.Model.tblCustomers;
import net.errs.ProjectErrs.Service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService service) {
        this.customerService = service;
    }

    // GET /api/v1/customers  - retrieve all customers
    @GetMapping
    public List<tblCustomers> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // GET /api/v1/customers/{id} - retrieve a customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<tblCustomers> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)               // return 200 OK with customer JSON
                .orElse(ResponseEntity.notFound().build());  // or 404 if not found
    }
}