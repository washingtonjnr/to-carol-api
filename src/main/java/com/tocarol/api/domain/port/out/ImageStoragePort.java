package com.tocarol.api.domain.port.out;

import java.util.List;

import com.tocarol.api.domain.model.Image;

public interface ImageStoragePort {
    List<Image> listAll();
}
