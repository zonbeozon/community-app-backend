package com.zonbeozon.post.service;

import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.service.ImageDeleter;
import com.zonbeozon.image.service.ImageFinder;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.domain.PostImage;
import com.zonbeozon.post.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostImageService {
    private final PostImageRepository postImageRepository;
    private final PostFinder postFinder;
    private final ImageFinder imageFinder;
    private final ImageDeleter imageDeleter;

    public void loadImages(List<Post> posts) {
        if (posts == null || posts.isEmpty()) return;
        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .toList();
        List<PostImage> postImages = postImageRepository.findAllByPostIdInWithImage(postIds);
        Map<Long, List<PostImage>> imagesByPostId = postImages.stream()
                .collect(Collectors.groupingBy(postImage -> postImage.getPost().getId()));
        posts.forEach(post -> {
            List<PostImage> images = imagesByPostId.getOrDefault(post.getId(), Collections.emptyList());
            post.setPostImages(images);
        });
    }

    public void updatePostImages(Long postId, List<Long> imageIds) {
        Post post = postFinder.findByIdWithImagesElseThrow(postId);
        checkPostImageLimit(imageIds);
        //더 이상 사용하지 않는 이미지 삭제
        List<PostImage> existImages = post.getPostImages();
        List<PostImage> postImageToDelete = existImages.stream()
                .filter(postImage -> !imageIds.contains(postImage.getImage().getId()))
                .toList();
        deletePostImages(post, postImageToDelete);
        //새로운 이미지 추가
        Set<Long> existImageIds = existImages.stream()
                .map(postImage -> postImage.getImage().getId())
                .collect(Collectors.toSet());
        List<Long> imageIdsToAdd = imageIds.stream()
                .filter(id -> !existImageIds.contains(id))
                .toList();

        if (imageIdsToAdd.isEmpty()) return;
        List<Image> images = imageFinder.findAllByIds(imageIdsToAdd);
        for(Image image : images) {
            PostImage postImage = new PostImage(post, image);
            postImageRepository.save(postImage);
            post.getPostImages().add(postImage);
        }
    }

    private void deletePostImages(Post post, List<PostImage> postImages) {
        post.getPostImages().removeAll(postImages);
        postImageRepository.deleteAllInBatch(postImages);
        imageDeleter.deleteImages(postImages.stream().map(PostImage::getImage).map(Image::getId).toList());
    }

    public void deletePostImagesByPostId(Long postId) {
        List<Long> ImageIds = postImageRepository.findAllByPostIdWithImage(postId).stream()
                .map(PostImage::getImage)
                .map(Image::getId)
                .toList();
        postImageRepository.deleteAllByPostId(postId);
        imageDeleter.deleteImages(ImageIds);
    }

    public void deletePostImagesByPostIdIn(List<Long> postIds) {
        List<Long> ImageIds = postImageRepository.findAllByPostIdInWithImage(postIds).stream()
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
