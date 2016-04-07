package com.hetro.FieldConnect.DtoController;

import com.hetro.FieldConnect.DTO.Channel5Dto;

import lombok.Getter;
import lombok.Setter;

public class Channel5 {

    private static Channel5Dto mInstance = null;

    @Getter
    @Setter
    private Channel5Dto channel5Dto;

    private Channel5() {
        channel5Dto = new Channel5Dto();
    }

    public static Channel5Dto getInstance() {
        if (mInstance == null) {
            mInstance = new Channel5Dto();
        }
        return mInstance;
    }
}