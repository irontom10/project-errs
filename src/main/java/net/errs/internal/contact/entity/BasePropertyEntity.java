package net.errs.internal.contact.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
abstract class BasePropertyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pref")
    private Integer pref;

    @Column(name = "altid", length = 64)
    private String altid;

    @Column(name = "prop_group", length = 64)
    private String propGroup;

    @Column(name = "prop_id", length = 255)
    private String propId;

    @Column(name = "created_param_tstamp")
    private OffsetDateTime createdParamTstamp;

    @Column(name = "params_json", columnDefinition = "TEXT")
    private String paramsJson;
}
