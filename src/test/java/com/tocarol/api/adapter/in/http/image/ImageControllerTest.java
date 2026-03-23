package com.tocarol.api.adapter.in.http.image;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tocarol.api.domain.model.Image;
import com.tocarol.api.domain.port.in.ListImagesPort;

@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ListImagesPort listImagesUseCase;

    @Test
    void list_returnsOkWithImages() throws Exception {
        when(listImagesUseCase.execute()).thenReturn(List.of(
                new Image("photo.jpg", "https://example.com/photo.jpg")
        ));

        mockMvc.perform(get("/images"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("photo.jpg"))
                .andExpect(jsonPath("$[0].url").value("https://example.com/photo.jpg"));
    }

    @Test
    void list_returnsEmptyArray() throws Exception {
        when(listImagesUseCase.execute()).thenReturn(List.of());

        mockMvc.perform(get("/images"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
