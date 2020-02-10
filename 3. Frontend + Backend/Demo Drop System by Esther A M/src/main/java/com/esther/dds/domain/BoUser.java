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

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@PasswordsMatch(baseField = "password", matchField = "confirmPassword")
public class BoUser implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @NotEmpty
//    @Size(min = 8, max = 40)
    @Column(nullable = false, unique = true)
    private String email;

    @NonNull
    @NotEmpty
    @Column(length = 100)
    private String password;

    @Transient
    @NotEmpty(message = "Please enter Password Confirmation")
    private String confirmPassword;

    @Transient
    private String oldPassword;

    @Transient
    private String generatedPassword;

    private String activationCode;


    @NonNull
    @NotEmpty(message = "Please enter you name")
    private String name;

    @NonNull
    @NotEmpty(message = "Please enter your last name")
    private String lastName;


    @NonNull
    @Column(nullable = false)
    private boolean enabled;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "boUser_roles",
            joinColumns = @JoinColumn(name = "boUser_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id")
    )       private Set<BoRole> boRoles = new HashSet<>();



    public void addBoRole(BoRole boRole) {
        boRoles.add(boRole);
    }

    //voor meerdere rollen (Eventueel nodig voor in de toekomst)
    public void addBoRoles(Set<BoRole> boRoles) {
        boRoles.forEach(this::addBoRole);
    }

    //for each role in boRoles, authorities.add SGA rile.getname
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return boRoles.stream().map(boRole -> new SimpleGrantedAuthority(boRole.getName())).collect(Collectors.toList());
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
