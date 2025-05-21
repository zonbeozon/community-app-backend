package com.zonbeozon.auth.service;

import com.zonbeozon.auth.dto.OAuth2UserInfo;
import com.zonbeozon.auth.dto.SimpleOAuth2User;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;
import com.zonbeozon.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);
        Member member = getOrCreateMember(oAuth2UserInfo);
        return new SimpleOAuth2User(oAuth2UserAttributes, member);
    }

    private Member getOrCreateMember(OAuth2UserInfo oAuth2UserInfo) {
        return memberService.getByEmail(oAuth2UserInfo.email())
                .orElseGet(() -> memberService.createMemberWithRandomUsername(
                        oAuth2UserInfo.email(),
                        oAuth2UserInfo.profile(),
                        ServerRole.USER)
                );
    }
}
