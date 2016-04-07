package com.hetro.FieldConnect.DtoController;

import com.hetro.FieldConnect.DTO.ImagePathDto;


import lombok.Getter;
import lombok.Setter;

public class ImagePath {

    private static ImagePathDto mInstance = null;

    @Getter
    @Setter
    private ImagePathDto imagePathDto;

    private ImagePath() {
        imagePathDto = new ImagePathDto();
    }

    public static ImagePathDto getInstance() {
        if (mInstance == null) {
            mInstance = new ImagePathDto();
        }
        return mInstance;
    }
}