package com.zonbeozon.channel.api.web;

import com.zonbeozon.channel.dto.ChannelInfoDto;
import com.zonbeozon.channel.repository.local.LocalChannelRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channels")
@Tag(name = "채널(로컬)", description = "dev 서버 전용 채널 관련 엔드포인트")
@Profile("local")
public class LocalChannelController {
    private final LocalChannelRepository localChannelRepository;
    @Operation(
            summary = "모든 채널 조회(임시)",
            description = "",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공시"),
    })
    @GetMapping
    public ResponseEntity<List<ChannelInfoDto>> getAllChannels(
    ) {
        return ResponseEntity.ok(localChannelRepository.findAll());
    }
}
