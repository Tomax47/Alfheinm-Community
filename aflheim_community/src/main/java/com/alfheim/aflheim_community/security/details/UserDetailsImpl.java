package com.alfheim.aflheim_community.security.details;

import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.model.user.Role;
import com.alfheim.aflheim_community.model.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {

    private User user;

    public UserDetailsImpl(User user) { this.user = user; }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()));
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public String getUserEmail() { return user.getEmail(); }

    @Override
    public boolean isAccountNonExpired() {
        return !user.getState().equals("BANNED");
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getState().equals("SUSPENDED");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !user.getState().equals("BANNED");
    }

    @Override
    public boolean isEnabled() {
        return user.getState().equals("CONFIRMED");
    }
}
