package com.zonbeozon.post.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.security.ChannelAction;
import com.zonbeozon.channel.security.CheckChannelAccess;
import com.zonbeozon.channel.service.ChannelFinder;
import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.post.dto.PostCreateCommand;
import com.zonbeozon.post.dto.PostCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCreator {
    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticationService authenticationService;
    private final ChannelFinder channelFinder;
    private final PostImageUpdater postImageUpdater;

    @CheckChannelAccess(ChannelAction.POST_CREATE)
    public Long addPost(Long channelId, PostCreateCommand command) {
        Member requester = authenticationService.getCurrentMember();
        Channel channel = channelFinder.findById(channelId);
        if(channel instanceof BlogChannel blogChannel) {
            Post post = Post.create(command.content(), blogChannel, requester);
            postRepository.save(post);
            if(!command.imageIds().isEmpty()) postImageUpdater.updatePostImages(post.getId(), command.imageIds());
            eventPublisher.publishEvent(new PostCreatedEvent(channelId, post.getId()));
            return post.getId();
        }
        throw new BadRequestException(ErrorCode.OPERATION_FOR_BLOG_CHANNEL_ONLY);
    }
}
