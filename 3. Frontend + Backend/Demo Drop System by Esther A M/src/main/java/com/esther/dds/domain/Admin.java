package com.esther.dds.domain;

import com.esther.dds.domain.Validator.PasswordsMatch;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * update 2021: After all I would have rather not used the lombok plugin but POJ instead
 */

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@PasswordsMatch(baseField = "password", matchField = "confirmPassword")
public class Admin implements UserDetails {

    public Admin(String email) {
        this.email = email;
    }
    
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @NotEmpty
//    @Size(min = 8, max = 40)
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty
    @Column(length = 100)
    private String password = null;

    @Transient
    @NotEmpty(message = "Please enter Password Confirmation")
    private String confirmPassword;

    @Transient
    private String oldPassword;

    @Transient
    private String generatedPassword;

    private String activationCode;

    @NonNull
    @Column(nullable = false)
    private boolean enabled = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "admin_roles",
            joinColumns = @JoinColumn(name = "admin_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id")
    )       private Set<AdminRole> adminRoles = new HashSet<>();

    public void addAdminRole(AdminRole adminRole) {
        adminRoles.add(adminRole);
    }

    //for each role in adminRoles, authorities.add SGA rile.getname
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return adminRoles.stream().map(adminRole -> new SimpleGrantedAuthority(adminRole.getName())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
