package com.ddam.spring.dto;

import com.ddam.spring.constant.Role;
import com.ddam.spring.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public record UserDto(
        Long id,
        String username,
        String email,
        String nickname,
        Role role,
        Map<String, Object> oAuth2Attributes
) implements UserDetails, OAuth2User {

    @Override public String getName() { return username; }
    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return null; }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    @Override public Map<String, Object> getAttributes() { return oAuth2Attributes; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.toString()));
    }

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getNickname(),
                user.getRole(),
                Map.of()
        );
    }
}
