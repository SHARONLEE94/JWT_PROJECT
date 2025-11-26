package com.lab.jwtmvc.security;

import com.lab.jwtcore.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // 실제로는 DB에서 사용자 정보를 조회해야 하지만, 여기서는 간단히 userId만으로 UserDetails 생성
        String defaultName = "User"; // 기본 이름 설정

        return new CustomUserDetails(userId, defaultName);
    }
}
