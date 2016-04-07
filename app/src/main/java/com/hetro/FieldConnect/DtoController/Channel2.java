package com.hetro.FieldConnect.DtoController;

import com.hetro.FieldConnect.DTO.Channel1Dto;
import com.hetro.FieldConnect.DTO.Channel2Dto;

import lombok.Getter;
import lombok.Setter;

public class Channel2 {

    private static Channel2Dto mInstance = null;

    @Getter
    @Setter
    private Channel2Dto channel2Dto;

    private Channel2() {
        channel2Dto = new Channel2Dto();
    }

    public static Channel2Dto getInstance() {
        if (mInstance == null) {
            mInstance = new Channel2Dto();
        }
        return mInstance;
    }
}