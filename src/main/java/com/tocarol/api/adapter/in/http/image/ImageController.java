package com.tocarol.api.adapter.in.http.image;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tocarol.api.domain.port.in.ListImagesPort;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ListImagesPort listImagesUseCase;

    public ImageController(ListImagesPort listImagesUseCase) {
        this.listImagesUseCase = listImagesUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ImageResponse>> list() {
        List<ImageResponse> images = listImagesUseCase.execute()
                .stream()
                .map(ImageMapper::toResponse)
                .toList();
                
        return ResponseEntity.ok(images);
    }
}
