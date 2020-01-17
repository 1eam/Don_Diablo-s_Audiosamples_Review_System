package com.esther.dds.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User implements UserDetails {

    @Id @GeneratedValue
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

    @NonNull
    @NotEmpty(message = "Please enter you name")
    private String name;

    @NonNull
    @NotEmpty(message = "Please enter your last name")
    private String lastName;

    @NonNull
    @Column(length = 50)
    @NotEmpty(message = "Please enter your artist name")
    private String artistName;

    @NonNull
    @Column(length = 350)
    @NotEmpty(message = "Please tell something about yourself")
    private String bio;

    @NonNull
    private String profileImage;

    @NonNull
    @Column(nullable = false)
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id")
    )

    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
    }

//voor meerdere rollen (achteraf niet nodig)
    public void addRoles(Set<Role> roles) {
        roles.forEach(this::addRole);
    }

    //for each role in roles, authorities.add SGA rile.getname
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
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