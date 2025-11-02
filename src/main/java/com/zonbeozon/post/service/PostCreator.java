package com.zonbeozon.post.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.service.finder.ChannelFinder;
import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import com.zonbeozon.post.domain.Post;
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
    private final ChannelFinder channelFinder;
    private final PostImageService postImageService;
    private final MemberFinder memberFinder;

    public Long addPost(Long authorId, Long channelId, PostCreateCommand command) {
        Member author = memberFinder.findByIdElseThrow(authorId);
        Channel channel = channelFinder.findByIdElseThrow(channelId);
        if(channel instanceof BlogChannel blogChannel) {
            Post post = Post.create(command.content(), blogChannel, author);
            postRepository.save(post);
            if(!command.imageIds().isEmpty()) postImageService.updatePostImages(post.getId(), command.imageIds());
            eventPublisher.publishEvent(new PostCreatedEvent(channelId, post.getId()));
            return post.getId();
        }
        throw new BadRequestException(ErrorCode.OPERATION_FOR_BLOG_CHANNEL_ONLY);
    }
}
