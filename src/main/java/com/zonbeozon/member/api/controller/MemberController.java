package com.zonbeozon.member.api.controller;

import com.zonbeozon.config.SwaggerConfig;
import com.zonbeozon.member.api.MemberDeleteApi;
import com.zonbeozon.member.api.MemberQueryApi;
import com.zonbeozon.member.api.MemberUpdateApi;
import com.zonbeozon.member.dto.MemberProfileUpdateRequest;
import com.zonbeozon.member.dto.UsernameUpdateRequest;
import com.zonbeozon.member.dto.MemberDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "서버 맴버", description = "서버 맴버 관련 로직 (채널 맴버가 아니다)")
public class MemberController {
    private final MemberUpdateApi memberUpdateApi;
    private final MemberQueryApi memberQueryApi;
    private final MemberDeleteApi memberDeleteApi;

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
                    description = "성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MemberDto.class)
                    )
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
    @PatchMapping("/username")
    public ResponseEntity<MemberDto> updateUsername(
            @RequestBody
            @Valid
            UsernameUpdateRequest request
    ) {
        MemberDto response = memberUpdateApi.updateUsername(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "맴버 프로필 업데이트",
            description = """
                    맴버 프로필을 변경한다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MemberDto.class)
                    )
            )}
    )
    @PatchMapping("/profile")
    public ResponseEntity<MemberDto> updateProfile(
            @RequestBody
            @Valid
            MemberProfileUpdateRequest request
    ) {
        MemberDto response = memberUpdateApi.updateProfile(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "맴버 프로필을 기본 프로필로 변경",
            description = """
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MemberDto.class)
                    )
            )}
    )
    @PatchMapping("/profile/default")
    public ResponseEntity<MemberDto> setAsDefaultProfile() {
        MemberDto response = memberUpdateApi.setAsDefaultProfile();
        return ResponseEntity.ok(response);
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
    @GetMapping
    public ResponseEntity<Page<MemberDto>> searchMember(
            @Parameter(name = "일부 닉네임 키워드")
            String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(0, size);
        return ResponseEntity.ok(
                memberQueryApi.getMemberResponseByUsername(pageable, username)
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
                            schema = @Schema(implementation = MemberDto.class)
                    )
            )}
    )
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDto> getMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberQueryApi.getMemberResponse(memberId));
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
        memberDeleteApi.deleteMember();
        return ResponseEntity.noContent().build();
    }
}
