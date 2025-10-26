package com.zonbeozon.base;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.entity.PostImage;
import com.zonbeozon.post.repository.PostImageRepository;
import com.zonbeozon.post.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestPostService {
    private static final Logger logger = LoggerFactory.getLogger(TestPostService.class);
    private static final String DEFAULT_CONTENT = "some post...";

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostImageRepository postImageRepository;
    @Autowired
    private TestBlogChannelService testBlogChannelService;

    /**
     * @param author author는 채널에 가입되어 있어야 한다.
     */
    public Post createAndSave(String content, List<Image> images, BlogChannel blogChannel, Member author) {
        ChannelMember chMember = testBlogChannelService.findByChannelAndMemberElseThrow(blogChannel, author);
        if(!chMember.getRole().isHigherThan(ChannelRole.CHANNEL_MEMBER))
            logger.warn("Admin 이하의 채널 권한을 가진 유저가 Post를 생성합니다.");
        Post post = Post.create(content, blogChannel, author);
        postRepository.save(post);
        if(images != null && !images.isEmpty()) {
            List<PostImage> postImages = postImageRepository.saveAll(images.stream().map(image -> new PostImage(post, image)).toList());
            post.getPostImages().addAll(postImages);
        }
        return post;
    }

    public Post createAndSave(BlogChannel blogChannel, Member author) {
        return createAndSave(DEFAULT_CONTENT, null, blogChannel, author);
    }

    public List<PostImage> setPostImages(Post post, List<Image> images) {
        List<PostImage> postImages = images.stream().map(image -> new PostImage(post, image)).toList();
        postImageRepository.saveAll(postImages);
        post.getPostImages().addAll(postImages);
        return postImages;
    }

    public void clearAll() {
        postRepository.deleteAll();
    }
}
