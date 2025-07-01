package com.zonbeozon.post.service;

import com.zonbeozon.channel.service.ChannelMemberResolver;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.repository.PostSort;
import com.zonbeozon.post.service.dto.PagedPostsResponse;
import com.zonbeozon.post.service.dto.PostAddCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceAdapter implements PostService {
    private final PostServiceImpl postServiceImpl;
    private final ChannelMemberResolver channelMemberResolver;


    @Override
    public Long addPost(Member member, Long channelId, PostAddCommand command) {
        return channelMemberResolver.findChannelMemberThenApply(member, channelId,
                channelMember -> postServiceImpl.addPost(command, channelMember));
    }

    @Override
    public void deletePost(Member member, Long channelId, Long postId) {
        channelMemberResolver.findChannelMemberThenConsume(member, channelId,
                channelMember -> postServiceImpl.deletePost(postId, channelMember));

    }

    @Override
    public void updatePostContent(Member member, Long channelId, Long postId, String content) {
        channelMemberResolver.findChannelMemberThenConsume(member, channelId,
                channelMember -> postServiceImpl.updatePostContent(postId, channelMember, content));
    }

    @Override
    public PagedPostsResponse createPagedPostResponse(Member member, Long channelId, String searchParam, int page, int size, PostSort sort, Sort.Direction direction) {
        return channelMemberResolver.findChannelMemberThenApply(
                member,
                channelId,
                channelMember -> postServiceImpl.createPagedPostResponse(channelMember, channelId, searchParam, page, size, sort, direction),
                true
        );
    }
}
