package com.zonbeozon.config;


import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.*;

@Configuration
public class SwaggerConfig {
    public static final String SECURITY_METHOD = "bearerAuth";

    private static final String PARAM_QUERY = "query";
    private static final String HEADER = "header";
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v1.0.0")
                .title("Zonbeozon")
                .description("Zonbeozon API List");

        return new OpenAPI()
                .info(info)
                .addTagsItem(new Tag().name("로컬 인증").description("로컬 환경에서만 사용가능하다."))
                .addTagsItem(new Tag().name("인증").description("인증 관련 엔드포인트."))
                .paths(createCustomPaths())
                .components(new Components()
                    .addSecuritySchemes(SECURITY_METHOD, new SecurityScheme()
                            .name("Authorization")
                            .in(SecurityScheme.In.HEADER)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                )
        );
    }

    private Paths createCustomPaths() {
        Paths paths = new Paths();

        PathItem localLoginPath = new PathItem()
                .get(new Operation()
                        .summary("로컬 로그인")
                        .description("로컬 환경에서 쉽게 jwt토큰을 얻을 수 있다.")
                        .addTagsItem("로컬 인증")
                        .addParametersItem( new Parameter()
                                .name("email")
                                .in(PARAM_QUERY)
                                .description("정해진 이메일을 사용해야 한다 - 스키마를 참조.")
                                .required(true)
                                .schema(new StringSchema()
                                        ._enum(List.of(
                                                "user_1@gmail.com",
                                                "user_2@gmail.com",
                                                "user_3@gmail.com",
                                                "admin@gmail.com"
                                        ))
                                )
                        ).responses(new ApiResponses()
                                .addApiResponse("302", new ApiResponse()
                                        .description("리다이렉트 - 쿼리 파라미터로 access 토큰 반환.")
                                        .headers(Map.of(
                                                "Location", new Header()
                                                        .description("등록된 프론트엔드 url로 accessToken과 함께 리다이렉트")
                                                        .schema(new StringSchema()
                                                                .example("https://front-domain.com/auth/success?accessToken=eyJhbGciOi...")
                                                        )
                                        ))
                        ))
                );
        paths.addPathItem("/local/login", localLoginPath);

        PathItem tokenReissuePath = new PathItem()
                .post(new Operation()
                        .summary("access token 재발급")
                        .description("""
                                access token이 만료되었을때 재발급 가능한 url.
                                
                                db에 저장된 refresh token을 통해 재발급한다.
                                
                                refresh token의 유효기한은 7일이기 때문에 만료되었다면 다시 로그인해야 한다.
                                
                                (자세한 예외 응답들은 responses를 참고)
                                """)
                        .addTagsItem("인증")
                        .addSecurityItem(new SecurityRequirement()
                                .addList(SECURITY_METHOD))
                        .addParametersItem(new Parameter()
                                .name("Authorization")
                                .in(HEADER)
                                .description("만료된 access token, Bearer prefix를 붙여야 한다.")
                                .required(true)
                        ).responses(new ApiResponses()
                                .addApiResponse("401", new ApiResponse()
                                        .description("잘못된 토큰 형식이거나 refresh token까지 만료되었을 경우.")
                                        .content(new Content()
                                                .addMediaType(APPLICATION_JSON_VALUE, new MediaType()
                                                        .addExamples("잘못된 토큰", new Example()
                                                                .summary("잘못된 토큰")
                                                                .description("잘못된 토큰 값을 넣었거나, refresh token까지 만료 되었을때, 다시 로그인 필요")
                                                                .value(Map.of(
                                                                        "code", "INVALID_TOKEN",
                                                                        "message", "유효하지 않은 토큰입니다."
                                                                ))
                                                        )
                                                )
                                        )
                                )
                                .addApiResponse("200", new ApiResponse()
                                        .description("정상적으로 재발급되었을때 - 헤더로 전송된다.")
                                        .headers(Map.of(
                                                "Authorization", new Header()
                                                        .description("재발급된 access token")
                                                        .schema(new StringSchema()
                                                                .example("eyJhbGciOi...")
                                                        )
                                        ))
                                )
                        )
                );
        paths.addPathItem("/auth/reissue", tokenReissuePath);

        PathItem oauth2LoginPath = new PathItem()
                .get(new Operation()
                        .summary("oauth2 로그인(구글)")
                        .description("""
                                oauth2 구글 인증 서버로 리다이렉션한다.                                                       \s
                               """)
                        .addTagsItem("인증")
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("유저가 소셜 로그인에 성공한다면 frontend uri로 리다이렉션한다..")
                                        .headers(Map.of(
                                                "Location", new Header()
                                                        .description("등록된 프론트엔드 url로 accessToken과 함께 리다이렉트")
                                                        .schema(new StringSchema()
                                                                .example("https://front-domain.com/auth/success?accessToken=eyJhbGciOi...")
                                                        )
                                        ))
                                )
                        )
                );
        paths.addPathItem("/oauth2/authorization/google", oauth2LoginPath);

        return paths;
    }
}
