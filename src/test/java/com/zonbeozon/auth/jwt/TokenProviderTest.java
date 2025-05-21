package com.zonbeozon.auth.jwt;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class TokenProviderTest {
    @InjectMocks
    private TokenProvider tokenProvider;

    private Authentication authentication;

//    @BeforeEach
//    void setup() {
//        authentication = new UsernamePasswordAuthenticationToken(
//                new PrincipalDetails(),
//                null,
//                List.of(new SimpleGrantedAuthority("ROLE_USER"))
//        );
//    }

//    @Test
//    @DisplayName("accessToken 생성")
//    void d() {
//        String accessToken = tokenProvider.generateAccessToken(authentication);
//    }
//
//    @Test
//    @DisplayName("refreshToken 생성")
}
