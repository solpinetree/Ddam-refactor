package com.ddam.spring.config.security;

import com.ddam.spring.dto.UserDto;
import com.ddam.spring.service.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService) throws Exception {
		return http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
						.mvcMatchers("/", "/fonts/**", "/font-awesome/**", "/img/**", "/crewphoto/**").permitAll()
						.anyRequest().authenticated()
				)
				.formLogin(withDefaults())
				.logout(logout -> logout.logoutSuccessUrl("/"))
				.oauth2Login(oAuth -> oAuth
						.userInfoEndpoint(userInfo -> userInfo
								.userService(oAuth2UserService)
						)
				)
				.build();
	}

	@Bean
	public UserDetailsService userDetailsService(UserService userService) {
		return username -> userService
				.findByUsername(username)
				.map(UserDto::from)
				.orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
	}
}
