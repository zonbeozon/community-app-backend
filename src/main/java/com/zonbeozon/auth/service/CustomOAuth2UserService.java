package com.zonbeozon.auth.service;

import com.zonbeozon.auth.dto.OAuth2UserInfo;
import com.zonbeozon.auth.dto.SimpleOAuth2User;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;
import com.zonbeozon.member.service.MemberCreator;
import com.zonbeozon.member.service.MemberFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberFinder memberFinder;
    private final MemberCreator memberCreator;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);
        Member member = getOrCreateMember(oAuth2UserInfo);
        return new SimpleOAuth2User(oAuth2UserAttributes, member);
    }

    private Member getOrCreateMember(OAuth2UserInfo oAuth2UserInfo) {
        Optional<Member> optMember = memberFinder.findByEmail(oAuth2UserInfo.email());
        return optMember.orElseGet(() -> memberCreator.createMemberWithRandomUsername(
                oAuth2UserInfo.email(),
                ServerRole.USER
        ));
    }
}
