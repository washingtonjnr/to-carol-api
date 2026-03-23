package com.tocarol.api.domain.port.in;

import java.util.List;

import com.tocarol.api.domain.model.Image;

public interface ListImagesPort {
    List<Image> execute();
}
