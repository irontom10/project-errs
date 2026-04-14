package net.errs.internal.contact.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contact_nicknames", indexes = @Index(name = "ix_contact_nicknames_contact", columnList = "contact_id"))
class ContactNicknameEntity extends BasePropertyEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private ContactEntity contact;

    @Column(name = "nickname", nullable = false, columnDefinition = "TEXT")
    private String nickname;

    @Column(name = "language_tag", length = 64)
    private String languageTag;
}