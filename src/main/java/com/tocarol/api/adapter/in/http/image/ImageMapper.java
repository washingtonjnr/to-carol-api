package com.tocarol.api.adapter.in.http.image;

import com.tocarol.api.domain.model.Image;

public final class ImageMapper {

    private ImageMapper() { }

    public static ImageResponse toResponse(Image image) {
        return new ImageResponse(image.name(), image.url());
    }
}
