package net.errs.internal.contact.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contact_names", indexes = @Index(name = "ix_contact_names_contact", columnList = "contact_id"))
class ContactNameEntity extends BasePropertyEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private ContactEntity contact;

    @Column(name = "family_names", columnDefinition = "TEXT")
    private String familyNames;

    @Column(name = "given_names", columnDefinition = "TEXT")
    private String givenNames;

    @Column(name = "additional_names", columnDefinition = "TEXT")
    private String additionalNames;

    @Column(name = "honorific_prefixes", columnDefinition = "TEXT")
    private String honorificPrefixes;

    @Column(name = "honorific_suffixes", columnDefinition = "TEXT")
    private String honorificSuffixes;

    @Column(name = "secondary_surname", columnDefinition = "TEXT")
    private String secondarySurname;

    @Column(name = "generation", columnDefinition = "TEXT")
    private String generation;

    @Column(name = "sort_as", columnDefinition = "TEXT")
    private String sortAs;

    @Column(name = "language_tag", length = 64)
    private String languageTag;
}


