package com.zonbeozon.channel.api.web;

import com.zonbeozon.channel.api.ChannelCreateApi;
import com.zonbeozon.channel.api.ChannelQueryApi;
import com.zonbeozon.channel.api.ChannelRemoveApi;
import com.zonbeozon.channel.api.ChannelUpdateApi;
import com.zonbeozon.channel.dto.*;
import com.zonbeozon.channel.dto.ChannelInfoDto;
import com.zonbeozon.channel.dto.ChannelInfoWithRequesterDto;
import com.zonbeozon.channel.dto.ChannelInfosWithRequesterDto;
import com.zonbeozon.config.SwaggerConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channels")
@Tag(name = "채널", description = "채널 관련 엔드포인트")
public class ChannelController {
    private final ChannelCreateApi channelCreateApi;
    private final ChannelUpdateApi channelUpdateApi;
    private final ChannelQueryApi channelQueryApi;
    private final ChannelRemoveApi channelRemoveApi;

    @Operation(
            summary = "커뮤니티 채널 추가",
            description = """
                    커뮤니티 채널을 추가한다.
                    
                    일부 채널은 서버 권한에 따라 생성이 가능하다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "성공 - 채널 id 반환",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(contentSchema = ChannelInfoDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "필드 형식 오류 또는 채널 생성 규칙 위반",
                    content =  @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "잘못된 필드 값 전달",
                                            summary = "필수 필드 누락하거나 길이 등 규칙을 위배한 경우",
                                            value = """
                                                        {
                                                          "code": "INVALID_ARGUMENT",
                                                          "errors": [
                                                            {
                                                              "field": "title",
                                                              "message": "채널 이름은 2자 이상 32자 이하여야 합니다."
                                                            },
                                                            {
                                                              "field": "description",
                                                              "message": "채널 설명은 256자 이하여야 합니다."
                                                            }
                                                          ]
                                                        }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "허용되지 않는 문자를 사용시",
                                            description = "입력값에 보안상 허용되지 않는 특수 문자 혹은 문자열이 포함된 경우.",
                                            value = """
                                                        {
                                                          "code": "INVALID_CHARACTERS",
                                                          "message": "입력값에 허용되지 않는 문자가 포함되어 있습니다."
                                                        }
                                                    """
                                    )
                            })),
            @ApiResponse(responseCode = "409", description = "채널 생성 충돌 (예: 중복 채널명)",
                    content =  @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "중복 채널 명일 때",
                                            summary = "활성 상태의 채널명이 이미 존재하여 생성할 수 없는 경우",
                                            value = """
                                                    {
                                                      "code": "DUPLICATE_CHANNEL_TITLE",
                                                      "message": "해당 채널 명이 이미 존재합니다."
                                                    }
                                                """
                                    )
                            }))
    })
    @PostMapping("/community")
    public ResponseEntity<ChannelInfoWithRequesterDto> addCommunityChannel(
            @Valid
            @RequestBody
            ChannelCreateRequest request
    ) {
        ChannelInfoWithRequesterDto channelInfoWithRequester = channelCreateApi.addCommunityChannel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(channelInfoWithRequester);
    }

    @Operation(
            summary = "채널 정보 업데이트",
            description = "채널 Owner만 호출 가능하다",
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @PatchMapping("/{channelId}")
    public ResponseEntity<Void> updateChannel(
            @Valid @RequestBody ChannelUpdateRequest channelUpdateRequest,
            @PathVariable Long channelId
    ) {
        channelUpdateApi.updateChannel(channelId, channelUpdateRequest);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "사용자가 속한 모든 채널 정보 가져오기",
            description = """
                    사용자가 속한 모든 채널 정보 가져온다.
                    
                    Blog 타입일 경우에는 가장 최근 post 생성일 기준 DESC순, Chat타입일 경우에는 가장 최근 chat 생성일 기준 DESC순이다.
                    
                    post, Chat이 없는 채널은 마지막에 배치되며 그들간의 순서가 보장되지 않는다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ChannelInfosWithRequesterDto.class))
            ),
    })
    @GetMapping("/joined")
    public ResponseEntity<ChannelInfosWithRequesterDto> getJoinedChannels(
    ) {
        return ResponseEntity.ok(channelQueryApi.getJoinedChannels());
    }

    @Operation(
            summary = "사용자가 속한 단일 채널 정보 가져오기",
            description = """
                    사용자가 속한 단일 채널 정보 가져온다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ChannelInfoWithRequesterDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "주어진 id에 해당하는 채널에 참가하지 않은 상태일때")
    })
    @GetMapping("/joined/{channelId}")
    public ResponseEntity<ChannelInfoWithRequesterDto> getJoinedChannel(
            @PathVariable Long channelId
    ) {
        return ResponseEntity.ok(channelQueryApi.getJoinedChannel(channelId));
    }

    @Operation(
            summary = "채널 삭제",
            description = "채널 Owner만 허용",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공시"),
    })
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(
            @PathVariable Long channelId
            ) {
        channelRemoveApi.removeChannel(channelId);
        return ResponseEntity.noContent().build();
    }


}
