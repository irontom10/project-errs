package net.errs.ProjectErrs.Repository;

import net.errs.ProjectErrs.Model.tblCustomers;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepository {
    private final JdbcTemplate jdbcTemplate;

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Retrieve all customers
    public List<tblCustomers> findAll() {
        String sql = "SELECT * FROM tblCustomers";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(tblCustomers.class));
    }

    // Retrieve one customer by ID
    public Optional<tblCustomers> findById(Long id) {
        String sql = "SELECT * FROM tblCustomers WHERE CustomerID = ?";
        List<tblCustomers> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(tblCustomers.class), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
