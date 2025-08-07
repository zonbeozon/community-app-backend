package com.zonbeozon.post.service;

import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.service.ImageFinder;
import com.zonbeozon.image.service.ImageOwnershipVerifier;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.entity.PostImage;
import com.zonbeozon.post.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostImageUpdater {
    private final PostImageRepository postImageRepository;
    private final ImageOwnershipVerifier imageOwnershipVerifier;
    private final PostFinder postFinder;
    private final ImageFinder imageFinder;

    /**
     * todo: image cascade 삭제 처리
     */
    public void updatePostImages(Long postId, List<Long> imageIds) {
        Post post = postFinder.findById(postId);
        checkPostImageLimit(imageIds);
        imageOwnershipVerifier.verify(imageIds);
        postImageRepository.deleteAllByPostId(postId);
        List<Image> images = imageFinder.findAllById(imageIds);
        for(Image image : images) {
            PostImage postImage = new PostImage(post, image);
            postImageRepository.save(postImage);
            post.getImages().add(postImage);
        }
    }

    private void checkPostImageLimit(List<Long> imageIds) {
        if(imageIds.size() > Post.MAX_IMAGE_COUNT) {
            throw new BadRequestException(ErrorCode.MAX_POST_IMAGE_REACHED);
        }
    }
}
