package com.ddam.spring.config.security;

import com.ddam.spring.constant.Role;
import com.ddam.spring.domain.user.User;
import com.ddam.spring.dto.KakaoOAuth2Response;
import com.ddam.spring.dto.UserDto;
import com.ddam.spring.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2UserServiceFacade {

    private final UserService userService;

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        return userRequest -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());
            String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "kakao"
            String providerId = String.valueOf(kakaoResponse.id());
            String username = registrationId + "_" + providerId;

            return userService.findByUsername(username)
                    .map(UserDto::from)
                    .orElseGet(() ->
                            UserDto.from(
                                    userService.saveUser(
                                            User.of(username, kakaoResponse.email(), kakaoResponse.nickname(), Role.USER)
                                    )
                            )
                    );
        };
    }

}
