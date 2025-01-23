package com.zonbeozon.communityapp.auth.domain.dto;

import com.zonbeozon.communityapp.auth.exception.AuthException;
import com.zonbeozon.communityapp.common.utils.KeyGenerator;
import com.zonbeozon.communityapp.exception.ErrorCode;
import com.zonbeozon.communityapp.member.domain.Role;
import com.zonbeozon.communityapp.member.domain.Member;
import lombok.Builder;

import java.util.Map;

@Builder
public record OAuth2UserInfo(
        String name,
        String email,
        String profile
) {

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            default -> throw new AuthException(ErrorCode.ILLEGAL_REGISTRATION_ID);
        };
    }

    public static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profile((String) attributes.get("picture"))
                .build();
    }

    public static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        return OAuth2UserInfo.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .profile((String) profile.get("profile_image_url"))
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .username(name)
                .email(email)
                .profile(profile)
                .memberKey(KeyGenerator.generateKey())
                .role(Role.USER)
                .build();
    }






}
