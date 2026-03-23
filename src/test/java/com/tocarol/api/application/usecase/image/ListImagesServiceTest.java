package com.tocarol.api.application.usecase.image;

import com.tocarol.api.domain.model.Image;
import com.tocarol.api.domain.port.out.ImageStoragePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListImagesServiceTest {

    @Mock
    private ImageStoragePort imageStorageGateway;

    @InjectMocks
    private ListImagesUseCase listImagesService;

    @Test
    void execute_returnsImagesFromGateway() {
        List<Image> images = List.of(
                new Image("photo.jpg", "https://example.com/photo.jpg")
        );
        when(imageStorageGateway.listAll()).thenReturn(images);

        List<Image> result = listImagesService.execute();

        assertThat(result).isEqualTo(images);
    }

    @Test
    void execute_returnsEmptyListWhenNoImages() {
        when(imageStorageGateway.listAll()).thenReturn(List.of());

        List<Image> result = listImagesService.execute();

        assertThat(result).isEmpty();
    }
}
