package com.zonbeozon.auth;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    /**
     * Member를 argument로 받는 Controller Method는 이미 SecurityFilter에 의해 authenticated 엔드포인트로 보호되고 있기 때문에
     * 실질적으로 이 메서드 호출시점에서 Authentication 객체는 존재해야 한다.
     * 따라서 AuthException이 아닌 IllegalState, IllegalArgument Exception을 발생시킨다.
     */
    @Override
    public Member resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("인증된 사용자가 아닙니다.");
        }
        return memberService.getByIdOrThrow(Long.parseLong(authentication.getName()));
    }
}
