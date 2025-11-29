package com.zonbeozon.post.service;

import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.channel.service.assembler.ChannelMemberQueryService;
import com.zonbeozon.global.CursorPage;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.PagedPostsPayload;
import com.zonbeozon.post.dto.PostCursor;
import com.zonbeozon.post.dto.PostPayload;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.reaction.post.dto.PersonalizedPostReactionDto;
import com.zonbeozon.reaction.post.service.PostReactionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryService {
    private final PostRepository postRepository;
    private final ChannelMemberQueryService channelMemberQueryService;
    private final PostReactionQueryService postReactionQueryService;

    public PagedPostsPayload getPagedPostsPayload(
            Long requesterId,
            Long channelId,
            PostCursor cursor,
            int size,
            boolean inverted
    ) {
        CursorPage<Post, PostCursor> pagedPosts = postRepository.findByChannelIdWithMetric(channelId, cursor, size, inverted);
        Map<Long, PersonalizedPostReactionDto> personalizedReactions = postReactionQueryService.getPersonalizedInfoByPostIdIn(requesterId, extractPostIds(pagedPosts.getContent()));
        Map<Long, ChannelMemberDto> authors = channelMemberQueryService.getChannelMembers(channelId, extractAuthorIds(pagedPosts.getContent()))
                .stream().collect(Collectors.toMap(ChannelMemberDto::memberId, Function.identity()));
        return PagedPostsPayload.from(pagedPosts, personalizedReactions, authors);
    }

    public PostPayload getPostPayload(Long postId) {
        Post post = postRepository.findByIdWithChannelAndImagesAndMetric(postId).orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
        ChannelMemberDto authorResponse = channelMemberQueryService.getChannelMember(post.getChannel().getId(), post.getAuthor().getId());
        return PostPayload.from(post, null, authorResponse);
    }

    public PostPayload getPostPayload(Long requesterId, Long postId) {
        PostPayload postPayload = getPostPayload(postId);
        PersonalizedPostReactionDto personalizedReaction = postReactionQueryService.getPersonalizedInfoByPostId(requesterId, postId);
        return PostPayload.from(postPayload, personalizedReaction);
    }

    private List<Long> extractPostIds(List<Post> posts) {
        return posts.stream().map(Post::getId).toList();
    }

    private List<Long> extractAuthorIds(List<Post> posts) {
        return posts.stream().map(Post::getAuthor).map(Member::getId).distinct().toList();
    }
}
