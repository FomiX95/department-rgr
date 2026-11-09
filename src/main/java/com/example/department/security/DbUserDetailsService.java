package com.example.department.security;

import com.example.department.entity.AppUser;
import com.example.department.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbUserDetailsService implements UserDetailsService {
    private final UserRepository users;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser u = users.findByUsername(username).orElseThrow(() -> {
            log.warn("Login failed: user '{}' not found", username);
            return new UsernameNotFoundException(username);
        });
        log.info("User '{}' found, role={}", username, u.getRole());
        return User.withUsername(u.getUsername()).password(u.getPassword()).roles(u.getRole()).build();
    }
}
