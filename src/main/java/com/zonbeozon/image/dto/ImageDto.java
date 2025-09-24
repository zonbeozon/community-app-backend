package com.zonbeozon.image.dto;

import com.zonbeozon.image.entity.Image;

public record ImageDto(
        Long imageId,
        String imageUrl
) {
    public static ImageDto from(Image image) {
        return new ImageDto(image.getId(), image.getUrl());
    }
}
