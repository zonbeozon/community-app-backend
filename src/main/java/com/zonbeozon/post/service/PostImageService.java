package com.zonbeozon.post.service;

import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.service.ImageDeleter;
import com.zonbeozon.image.service.ImageFinder;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.entity.PostImage;
import com.zonbeozon.post.repository.PostFetchOptions;
import com.zonbeozon.post.repository.PostImageRepository;
import com.zonbeozon.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class PostImageService {
    private final PostImageRepository postImageRepository;
    private final PostFinder postFinder;
    private final ImageFinder imageFinder;
    private final ImageDeleter imageDeleter;

    public void updatePostImages(Long postId, List<Long> imageIds) {
        Post post = postFinder.findByIdElseThrow(postId);
        checkPostImageLimit(imageIds);
        deletePostImages(postId);
        List<Image> images = imageFinder.findAllByIds(imageIds);
        for(Image image : images) {
            PostImage postImage = new PostImage(post, image);
            postImageRepository.save(postImage);
            post.getImages().add(postImage);
        }
    }

    public void deletePostImages(Long postId) {
        List<Long> ImageIds = postImageRepository.findAllByPostId(postId).stream()
                .map(PostImage::getImage)
                .map(Image::getId)
                .toList();
        postImageRepository.deleteAllByPostId(postId);
        imageDeleter.deleteImages(ImageIds);
    }

    public void deletePostImages(List<Long> postIds) {
        List<Long> ImageIds = postImageRepository.findAllByPostIdIn(postIds).stream()
                .map(PostImage::getImage)
                .map(Image::getId)
                .toList();
        postImageRepository.deleteAllByPostIdIn(postIds);
        imageDeleter.deleteImages(ImageIds);
    }

    private void checkPostImageLimit(List<Long> imageIds) {
        if(imageIds.size() > Post.MAX_IMAGE_COUNT) {
            throw new BadRequestException(ErrorCode.MAX_POST_IMAGE_REACHED);
        }
    }
}
