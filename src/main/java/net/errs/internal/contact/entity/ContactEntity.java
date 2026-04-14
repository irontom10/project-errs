package net.errs.internal.contact.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
private LocalDateTime updatedAt;

@OneToOne(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private ContactVcardEntity vcard;

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactNameEntity> names = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactNicknameEntity> nicknames = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactCategoryEntity> categories = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactNoteEntity> noteEntries = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactDateEntity> dates = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactPlaceEntity> places = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactPhoneEntity> phones = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactEmailEntity> emails = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactImppEntity> impps = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactLangEntity> langs = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactAddressEntity> addresses = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactTimeZoneEntity> timeZones = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactGeoEntity> geos = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactUrlEntity> urls = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactOrganizationEntity> organizations = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactTitleEntity> titles = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactRoleEntity> roles = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactRelatedEntity> relatedEntries = new LinkedHashSet<>();

@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
private Set<ContactSocialProfileEntity> socialProfiles = new LinkedHashSet<>();
}
