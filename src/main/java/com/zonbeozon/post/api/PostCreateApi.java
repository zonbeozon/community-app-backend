package com.zonbeozon.post.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.PostCreateCommand;
import com.zonbeozon.post.dto.PostCreateRequest;
import com.zonbeozon.post.service.PostCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@Transactional
@RequiredArgsConstructor
public class PostCreateApi {
    private final PostCreator postCreator;
    private final AuthenticationService authenticationService;
    private final ChannelAuthorizationCheckService channelAuthorizationCheckService;

    public Long createPost(Long channelId, PostCreateRequest request) {
        if(!channelAuthorizationCheckService.isAtLeastAdmin(channelId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        Member member = authenticationService.getCurrentMember();
        return postCreator.addPost(member.getId(), channelId, new PostCreateCommand(request.content(), request.imageIds()));
    }
}
