package com.zonbeozon.channel.controller;

import com.zonbeozon.channel.dto.*;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.service.BlogChannelAssembler;
import com.zonbeozon.channel.service.ChannelCreator;
import com.zonbeozon.channel.service.ChannelUpdater;
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
@RequestMapping("/channel")
@Tag(name = "채널", description = "채널 관련 엔드포인트")
public class ChannelController {
    private final ChannelCreator channelCreator;
    private final ChannelUpdater channelUpdater;
    private final BlogChannelAssembler blogChannelAssembler;

    @Operation(
            summary = "채널 추가",
            description = """
                    채널을 추가한다.
                    
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
                            schema = @Schema(type = "integer", format = "int64", description = "채널 ID")
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
    @PostMapping
    public ResponseEntity<Long> addChannel(
            @Valid
            @RequestBody
            ChannelCreateRequest request
    ) {
        Long channelId = channelCreator.addChannel(
                new ChannelCreateCommand(
                        request.channelType(),
                        request.title(),
                        request.description(),
                        request.profile(),
                        request.settings().visibility(),
                        request.settings().joinPolicy(),
                        ChannelCreatorType.COMMUNITY
                ));
        return ResponseEntity.status(HttpStatus.CREATED).body(channelId);
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
        channelUpdater.updateChannel(channelId, channelUpdateRequest);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "사용자가 속한 COMMUNITY - BLOG 타입의 모든 채널 정보 가져오기",
            description = """
                    사용자가 속한 모든 채널 정보 가져온다.
                    
                    가장 최근 post가 작성된 시간 기준 DESC순이며 
                    post가 없는 채널은 순서가 보장되지 않는다. 
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = JoinedBlogChannelListResponse.class))
            ),
    })
    @GetMapping("/community-blog/joined")
    public ResponseEntity<JoinedBlogChannelListResponse> getJoinedInfoChannels() {
        return ResponseEntity.ok(blogChannelAssembler.createJoinedCommunityBlogChannelResponse());
    }

//    @Operation(
//            summary = "채널 검색",
//            description = """
//                    채널 title중 searchParam이 포함된 채널을 찾는다.
//
//                    정렬 순서는 채널 구독자 DESC순이다.
//
//                    채널 설정에서 검색 허용을 PRIVATE으로 설정시 검색되지 않는다.
//                    """
//    )
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "성공",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = PagedChannelResponse.class))
//            ),
//    })
//    @GetMapping
//    public ResponseEntity<PagedChannelResponse> getChannels(
//            @Parameter(
//                    name = "채널 명",
//                    description = """
//                            채널 명, 입력 안할시 빈 문자열로 적용
//
//                            채널 명의 일부로도 검색할 수 있다.(대소문자 구분안함)
//                            """,
//                    example = "좋은 채널"
//            )
//            @RequestParam(defaultValue = "") String searchParam,
//            @Parameter(name = "채널 타입")
//            @RequestParam(required = false) ChannelType type,
//            @Parameter(name = "채널 컨텐츠 공개 수준")
//            @RequestParam(required = false) ChannelVisibility contentOpenLevel,
//            @Parameter(name = "채널 검색 허용 수준")
//            @RequestParam(required = false) ChannelJoinPolicy joinLevel,
//            @Parameter(name = "정렬 기준")
//            @RequestParam(defaultValue = "MEMBER_COUNT") ChannelSort sort,
//            @Parameter(name = "정렬 순서")
//            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size
//            ) {
//        return ResponseEntity.ok(channelService.createChannelSearchResponse(searchParam, page, size, sort, direction, type, contentOpenLevel, joinLevel));
//    }
//
//    @Operation(
//            summary = "채널 삭제",
//            description = "채널 Owner만 허용",
//            security = @SecurityRequirement(name = "bearerAuth")
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "204", description = "삭제 성공시"),
//    })
//    @DeleteMapping("/{channelId}")
//    public ResponseEntity<Void> deleteChannel(
//            @Parameter(hidden = true) Member member,
//            @PathVariable Long channelId
//            ) {
//        channelService.deleteChannel(member, channelId);
//        return ResponseEntity.noContent().build();
//    }


}
