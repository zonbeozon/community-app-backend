package com.zonbeozon.config;


import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    public static final String SECURITY_METHOD = "bearerAuth";

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
                .components(new Components()
                    .addSecuritySchemes("bearerAuth", new SecurityScheme()
                            .name("Authorization")
                            .in(SecurityScheme.In.HEADER)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                )
        );
    }
}
