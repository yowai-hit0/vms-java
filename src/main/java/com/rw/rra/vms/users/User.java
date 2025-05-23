package com.rw.rra.vms.users;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User implements UserDetails {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable=false, length=50)
    private String firstName;

    @Column(nullable=false, length=50)
    private String lastName;

    @Column(nullable=false, unique=true, length=100)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false, unique=true, length=10)
    private String phoneNumber;

    @Column(nullable=false, unique=true, length=16)
    private String nationalId;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private Role role;

    private boolean enabled = false;

    private String verificationToken;
    private String resetToken;

    // UserDetails
    @Override public Collection<org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> role.name());
    }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired()    { return true; }
    @Override public boolean isAccountNonLocked()     { return true; }
    @Override public boolean isCredentialsNonExpired(){ return true; }
}