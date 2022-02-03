package com.kodigo.alltodo_api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    @Getter
    private final UserDTO user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAvailable();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAvailable();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isAvailable();
    }

    @Override
    public boolean isEnabled() {
        return user.isAvailable();
    }
}
