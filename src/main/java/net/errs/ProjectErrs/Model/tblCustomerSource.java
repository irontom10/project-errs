package net.errs.ProjectErrs.Model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class tblCustomerSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SourceID")
    private Long SourceID;

    @Column(name = "SourceName", nullable = false)
    private String SourceName;
}
