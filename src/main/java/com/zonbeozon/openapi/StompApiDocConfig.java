package com.zonbeozon.openapi;

import com.zonbeozon.channel.dto.ChannelEventResponse;
import com.zonbeozon.channel.dto.ChannelMemberEventResponse;
import com.zonbeozon.comment.dto.CommentCountEventResponse;
import com.zonbeozon.comment.dto.CommentEventResponse;
import com.zonbeozon.post.dto.PostEventResponse;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StompApiDocConfig {
    @Bean
    public OpenApiCustomizer stompApiCustomizer() {
        return openApi -> {
            openApi
                    .addTagsItem(new Tag().name("STOMP-CONNECT")
                            .description("""
                                    WebSocket Upgrade 요청 endpoint는 "/ws"이다.
                                    
                                    STOMP CONNECT 요청시 STOMP 헤더에 "Authorization : Bearer ${accessToken}" 의 인증 헤더를 추가하여 요청해야한다.
                                    """))
                    .addTagsItem(new Tag().name("STOMP-SUBSCRIPTION")
                            .description("""
                                    stomp 구독 목록
                                    
                                    HTTP METHOD 키워드 및 응답 Status Code는 무시한다.
                                    """));

            Schema channelMemberEventResponseSchema = ModelConverters.getInstance()
                    .resolveAsResolvedSchema(new AnnotatedType(ChannelMemberEventResponse.class)).schema;
            Schema channelEventResponseSchema = ModelConverters.getInstance()
                    .resolveAsResolvedSchema(new AnnotatedType(ChannelEventResponse.class)).schema;
            Schema postEventResponseSchema = ModelConverters.getInstance()
                    .resolveAsResolvedSchema(new AnnotatedType(PostEventResponse.class)).schema;
            Schema commentEventResponseSchema = ModelConverters.getInstance()
                    .resolveAsResolvedSchema(new AnnotatedType(CommentEventResponse.class)).schema;
            Schema CommentCountEventResponseSchema = ModelConverters.getInstance()
                    .resolveAsResolvedSchema(new AnnotatedType(CommentCountEventResponse.class)).schema;

            openApi.getComponents()
                    .addSchemas("channelMemberEventResponse", channelMemberEventResponseSchema)
                    .addSchemas("channelEventResponse", channelEventResponseSchema)
                    .addSchemas("postEventResponse", postEventResponseSchema)
                    .addSchemas("commentEventResponse", commentEventResponseSchema)
                    .addSchemas("commentCountEventResponse", CommentCountEventResponseSchema);

            openApi.path("/topic/member/{memberId}/channel", getChannelMemberSubPath())
                    .path("/topic/channel/{channelId}", getChannelSubPath())
                    .path("/topic/channel/{channelId}/post", getPostSubPath())
                    .path("/topic/post/{postId}/comment", getCommentSubPath())
                    .path("/topic/channel/{channelId}/comment-count", getCommentCountSubPath());
        };


    }

    private PathItem getChannelMemberSubPath() {
        return new PathItem()
                .get(new Operation()
                        .summary("채널 맴버 정보 구독")
                        .description("채널에서 맴버 단위로 발생하는 이벤트를 구독한다.")
                        .addTagsItem("STOMP-SUBSCRIPTION")
                        .addParametersItem(new Parameter()
                                .name("memberId")
                                .in("path")
                                .required(true)
                                .description("로그인된 맴버의 고유 ID")
                                .schema(new IntegerSchema().type("integer").format("int64"))
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("맴버 강퇴, 가입 승인 등 단일 맴버 단위로 발생하는 이벤트")
                                        .content(new Content()
                                                .addMediaType("application/json", new MediaType()
                                                .schema(new ObjectSchema().$ref("#/components/schemas/channelMemberEventResponse"))
                                        ))
                                )
                        )
                );
    }

    private PathItem getChannelSubPath() {
        return new PathItem()
                .get(new Operation()
                        .summary("채널 정보 구독")
                        .description("채널에서 발생하는 이벤트를 구독한다.")
                        .addTagsItem("STOMP-SUBSCRIPTION")
                        .addParametersItem(new Parameter()
                                .name("channelId")
                                .in("path")
                                .required(true)
                                .description("가입되어있는 채널 Id, 가입이 되어 있지 않다면 STOMP ERROR 응답 발생")
                                .schema(new IntegerSchema().type("integer").format("int64"))
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("채널 삭제 등 단일 채널 단위로 발생하는 이벤트")
                                        .content(new Content()
                                                .addMediaType("application/json", new MediaType()
                                                        .schema(new ObjectSchema().$ref("#/components/schemas/channelEventResponse"))
                                                ))
                                )
                        )
                );
    }

    private PathItem getPostSubPath() {
        return new PathItem()
                .get(new Operation()
                        .summary("Post 정보 구독")
                        .description("단일 채널에서 발생하는 Post CRUD 이벤트를 구독한다.")
                        .addTagsItem("STOMP-SUBSCRIPTION")
                        .addParametersItem(new Parameter()
                                .name("channelId")
                                .in("path")
                                .required(true)
                                .description("가입되어있는 채널 Id, 가입이 되어 있지 않다면 STOMP ERROR 응답 발생")
                                .schema(new IntegerSchema().type("integer").format("int64"))
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("단일 채널 단위로 발생하는 POST CRUD 이벤트")
                                        .content(new Content()
                                                .addMediaType("application/json", new MediaType()
                                                        .schema(new ObjectSchema().$ref("#/components/schemas/postEventResponse"))
                                                ))
                                )
                        )
                );
    }

    private PathItem getCommentSubPath() {
        return new PathItem()
                .get(new Operation()
                        .summary("Comment 정보 구독")
                        .description("단일 Post에서 발생하는 COMMENT CRUD 이벤트를 구독한다.")
                        .addTagsItem("STOMP-SUBSCRIPTION")
                        .addParametersItem(new Parameter()
                                .name("postId")
                                .in("path")
                                .required(true)
                                .description("해당 postId가 작성된 채널에 가입이 되어 있지 않다면 STOMP ERROR 응답 발생")
                                .schema(new IntegerSchema().type("integer").format("int64"))
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("단일 POST 단위로 발생하는 COMMENT CRUD 이벤트")
                                        .content(new Content()
                                                .addMediaType("application/json", new MediaType()
                                                        .schema(new Schema<>().$ref("#/components/schemas/commentEventResponse"))
                                                ))
                                )
                        )
                );
    }

    private PathItem getCommentCountSubPath() {
        return new PathItem()
                .get(new Operation()
                        .summary("Comment 개수 변동 정보 구독")
                        .description("단일 채널에서 발생하는 Post 별 Comment 개수 변동 이벤트를 구독한다.")
                        .addTagsItem("STOMP-SUBSCRIPTION")
                        .addParametersItem(new Parameter()
                                .name("channelId")
                                .in("path")
                                .required(true)
                                .description("가입되어있는 채널 Id, 가입이 되어 있지 않다면 STOMP ERROR 응답 발생")
                                .schema(new IntegerSchema().type("integer").format("int64"))
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("단일 채널 단위로 발생하는 Comment 개수 변동 이벤트")
                                        .content(new Content()
                                                .addMediaType("application/json", new MediaType()
                                                        .schema(new ObjectSchema().$ref("#/components/schemas/commentCountEventResponse"))
                                                ))
                                )
                        )
                );
    }
}
