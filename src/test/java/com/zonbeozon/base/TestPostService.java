package com.zonbeozon.base;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.entity.PostImage;
import com.zonbeozon.post.repository.PostImageRepository;
import com.zonbeozon.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestPostService {
    private static final String DEFAULT_CONTENT = "some post...";

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostImageRepository postImageRepository;

    /**
     * @param author author는 채널에 가입되어 있어야 한다.
     */
    public Post createAndSave(String content, BlogChannel blogChannel, Member author) {
        Post post = Post.create(content, blogChannel, author);
        return postRepository.save(post);
    }

    public Post createAndSave(BlogChannel blogChannel, Member author) {
        return createAndSave(DEFAULT_CONTENT, blogChannel, author);
    }

    public List<PostImage> setPostImages(Post post, List<Image> images) {
        List<PostImage> postImages = images.stream().map(image -> new PostImage(post, image)).toList();
        postImageRepository.saveAll(postImages);
        post.getImages().addAll(postImages);
        return postImages;
    }
}
