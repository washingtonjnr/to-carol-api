package com.tocarol.api.application.usecase.image;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tocarol.api.domain.model.Image;
import com.tocarol.api.domain.port.in.ListImagesPort;
import com.tocarol.api.domain.port.out.ImageStoragePort;

@Service
public class ListImagesUseCase implements ListImagesPort {

    private final ImageStoragePort imageStorageGateway;

    public ListImagesUseCase(ImageStoragePort imageStorageGateway) {
        this.imageStorageGateway = imageStorageGateway;
    }

    @Override
    public List<Image> execute() {
        return imageStorageGateway.listAll();
    }
}
