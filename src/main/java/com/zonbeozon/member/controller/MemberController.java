package com.zonbeozon.member.controller;

import com.zonbeozon.config.SwaggerConfig;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.respository.MemberSort;
import com.zonbeozon.member.service.MemberAssembler;
import com.zonbeozon.member.service.MemberFinder;
import com.zonbeozon.member.service.MemberRemover;
import com.zonbeozon.member.service.MemberUpdater;
import com.zonbeozon.member.service.dto.MemberResponse;
import com.zonbeozon.member.service.dto.PagedMemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "서버 맴버", description = "서버 맴버 관련 로직 (채널 맴버가 아니다)")
public class MemberController {
    private final MemberAssembler memberAssembler;
    private final MemberUpdater memberUpdater;
    private final MemberRemover memberRemover;

    @Operation(
            summary = "맴버 명 업데이트",
            description = """
                    맴버 명을 변경한다.
                    
                    중복 맴명은 허용하지 않으며 2~32자, 한글, 영문, _, 숫자만 허용된다
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공 - 응답 바디 없음"
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "중복 맴버 명일떄",
                                            value = """
                                                    {                                                    {
                                                      "code": "DUPLICATE_USERNAME",
                                                      "message": "중복 맴버 명입니다."
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "허용이 안되는 맴버 명일때",
                                            value = """
                                                {
                                                  "code": "INVALID_ARGUMENT",
                                                  "errors": [
                                                    {
                                                      "field": "username",
                                                      "message": "username은 한글, 영문, 숫자, _ 만 가능하며 2~32자여야 합니다."
                                                    }
                                                  ]
                                                }
                                                """
                                    )
                            }
                    )
            )
    })
    @PatchMapping
    public ResponseEntity<Void> updateUsername(
            @RequestBody
            @Valid
            UsernameUpdateRequest request
    ) {
       memberUpdater.updateUsername(request.username());
       return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "맴버 명으로 맴버 검색",
            description = """
                    맴버 명으로 페이지 처리된 맴버 를 조회한다.
                    
                    추후 다양한 정렬 기준이 추가된다(팔로워, 팔로우 순).
                    
                    영문에 대해서는 대소문자는 무시된다.
                    
                    USER role을 가진 유저만 검색된다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = PagedMemberResponse.class)
                    )
            )}
    )
    @GetMapping
    public ResponseEntity<PagedMemberResponse> searchMember(
            @Parameter(name = "일부 닉네임 키워드")
            String partialUsername,
            @Parameter(name = "정렬 기준")
            @RequestParam(defaultValue = "CREATED_AT") MemberSort sort,
            @Parameter(name = "정렬 순서")
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                memberAssembler.searchPagedMemberResponse(
                    partialUsername,
                    sort,
                    direction,
                    page,
                    size
                )
        );
    }

    @Operation(
            summary = "맵버 id 검색",
            description = """
                    맴버 id로 맴버를 검색한다.
                    
                    ADMIN role을 가진 맴버 또한 조회된다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MemberResponse.class)
                    )
            )}
    )
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberAssembler.createMemberResponse(memberId));
    }


    @Operation(
            summary = "맴버 삭제",
            description = """
                    자기 자신만 호출 가능하다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "성공"
            )}
    )
    @DeleteMapping
    public ResponseEntity<Void> deleteMember() {
        memberRemover.deleteMember();
        return ResponseEntity.noContent().build();
    }
}
