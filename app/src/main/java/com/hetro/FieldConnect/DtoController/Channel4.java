package com.hetro.FieldConnect.DtoController;

import com.hetro.FieldConnect.DTO.Channel4Dto;

import lombok.Getter;
import lombok.Setter;

public class Channel4 {

    private static Channel4Dto mInstance = null;

    @Getter
    @Setter
    private Channel4Dto channel4Dto;

    private Channel4() {
        channel4Dto = new Channel4Dto();
    }

    public static Channel4Dto getInstance() {
        if (mInstance == null) {
            mInstance = new Channel4Dto();
        }
        return mInstance;
    }
}