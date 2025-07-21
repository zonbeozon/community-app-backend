package com.zonbeozon.post.service;

import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.service.ImageOwnershipVerifier;
import com.zonbeozon.image.ImageRepository;
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
public class PostImageCreator {
    private final PostImageRepository postImageRepository;
    private final ImageRepository imageRepository;
    private final ImageOwnershipVerifier imageOwnershipVerifier;
    private final PostFinder postFinder;

    public void addPostImages(Long postId, List<Long> imageIds) {
        Post post = postFinder.findById(postId);
        checkPostImageLimit(postId, imageIds);
        imageOwnershipVerifier.verify(imageIds);
        List<Image> images = imageRepository.findAllById(imageIds);
        int displayOrder = 0;
        for(Image image : images) {
            PostImage postImage = new PostImage(post, image, displayOrder++);
            postImageRepository.save(postImage);
            post.getImages().add(postImage);
        }
    }

    private void checkPostImageLimit(Long postId, List<Long> imageIds) {
        List<PostImage> postImages = postImageRepository.findAllByPostId(postId);
        if(postImages.size() + imageIds.size() > Post.MAX_IMAGE_COUNT) {
            throw new BadRequestException(ErrorCode.MAX_POST_IMAGE_REACHED);
        }
    }
}
