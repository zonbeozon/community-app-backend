package com.zonbeozon.image.dto;

import com.zonbeozon.image.entity.Image;

public record ImageDto(
        Long imageId,
        String imageUrl
) {
    public static ImageDto create(Image image) {
        if(image == null) return null;
        return new ImageDto(image.getId(), image.getUrl());
    }
}
