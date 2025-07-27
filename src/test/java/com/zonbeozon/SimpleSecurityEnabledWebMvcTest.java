package com.zonbeozon;

import com.zonbeozon.auth.filter.ReissueAccessTokenFilter;
import com.zonbeozon.auth.filter.TokenAuthenticationFilter;
import com.zonbeozon.auth.filter.TokenExceptionFilter;
import com.zonbeozon.channel.validation.ChannelSettingValidator;
import com.zonbeozon.channel.validation.ChannelTitleValidator;
import com.zonbeozon.channel.validation.ValidChannelSetting;
import com.zonbeozon.config.SecurityConfig;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 로그인 기능이 제거된 매우 단순화된 webMvcTest에 사용된다.
 * 인증을 위해서는 withMockUser를 사용한다.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@WebMvcTest(
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ReissueAccessTokenFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TokenAuthenticationFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TokenExceptionFilter.class)
        }
)
@Import({
        SimpleTestSecurityConfig.class
})
public @interface SimpleSecurityEnabledWebMvcTest {
    @AliasFor(value = "value", annotation = WebMvcTest.class)
    Class<?>[] value();
}
