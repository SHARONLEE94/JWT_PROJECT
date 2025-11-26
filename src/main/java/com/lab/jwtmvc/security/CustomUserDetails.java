package com.lab.jwtmvc.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    // Spring Security에서 관리하는 유저 정보 객체.
    private final String userId;
    private final String name;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return userId; // JWT 인증에서는 password 비교를 안 함
    }

    @Override
    public String getUsername() {
        return ""; // Security가 인증할 때 username = userId
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}
