package com.myapp.mybackend.models;

import com.myapp.mybackend.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Function;

/**
 * Custom user model derived from org.springframework.security.core.userdetails.User
 */
@Entity
@Table(name = "users", schema = Constants.DB_SCHEMA)
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Data
@Builder(builderClassName = "MyUserBuilder")
public class MyUser {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", columnDefinition = "CITEXT")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "account_expired")
    private boolean isAccountExpired;

    @Column(name = "account_locked")
    private boolean isAccountLocked;

    @Column(name = "enabled")
    private boolean isEnabled;

    @Column(name = "credentials_expired")
    private boolean isCredentialsExpired;

    @CreatedDate
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authorities", schema = Constants.DB_SCHEMA, joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authority_id", columnDefinition = "CITEXT")
    @Convert(converter = MyUserAuthorityEnum.Converter.class)
    @Builder.Default
    // JPA has problems persisting EnumSet
    private Set<MyUserAuthorityEnum> authorities = new HashSet<>();

    // Public no-arg constructor required by JPA
    public MyUser() {}

    // "package-private"
    MyUser(String username,
           String password,
           boolean isAccountExpired,
           boolean isAccountLocked,
           boolean isEnabled,
           boolean isCredentialsExpired,
           Set<MyUserAuthorityEnum> authorities) {
        this.username = username;
        this.password = password;
        this.isAccountExpired = isAccountExpired;
        this.isAccountLocked = isAccountLocked;
        this.isEnabled = isEnabled;
        this.isCredentialsExpired = isCredentialsExpired;
        this.authorities = authorities;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    // Override equals and hashcode to determine equality based on username (case insensitive)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyUser myUser)) return false;
        if (getUsername() == null || myUser.getUsername() == null) return false;
        return Objects.equals(getUsername().toUpperCase(), myUser.getUsername().toUpperCase());
    }

    // Override equals and hashcode to determine equality based on username (case insensitive)
    @Override
    public int hashCode() {
        return Objects.hash(getUsername() == null ? null : getUsername().toUpperCase());
    }

    public static final class MyUserBuilder {
        // Builder default value
        private boolean isEnabled = true;
        private Set<MyUserAuthorityEnum> authorities = new HashSet<>();
        private Function<String, String> passwordEncoder;
        private MyUserAuthorityEnum[] authEnums;

        public MyUserBuilder passwordEncoder(Function<String, String> passwordEncoder) {
            this.passwordEncoder = passwordEncoder;
            return this;
        }

        public MyUserBuilder authorities(MyUserAuthorityEnum... authEnums) {
            this.authEnums = authEnums;
            return this;
        }

        public MyUser build() {
            if (passwordEncoder == null) {
                throw new IllegalStateException(
                        "Cannot build a user without a password encoder.");
            }
            if (authEnums != null) {
                for (MyUserAuthorityEnum authEnum : authEnums) {
                    authorities.add(authEnum);
                }
            }
            MyUser myUser = new MyUser(
                    username,
                    passwordEncoder.apply(password),
                    isAccountExpired,
                    isAccountLocked,
                    isEnabled,
                    isCredentialsExpired,
                    authorities);
            return myUser;
        }
    }
}
