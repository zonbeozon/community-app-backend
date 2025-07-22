package com.zonbeozon.image.entity;

public record ImageResponse(
        Long imageId,
        String imageUrl
) {
    public static ImageResponse from(Image image) {
        return new ImageResponse(image.getId(), image.getUrl());
    }
}
