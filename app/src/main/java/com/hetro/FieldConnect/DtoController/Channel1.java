package com.hetro.FieldConnect.DtoController;

import com.hetro.FieldConnect.DTO.Channel1Dto;

import lombok.Getter;
import lombok.Setter;

public class Channel1 {

    private static Channel1Dto mInstance = null;

    @Getter
    @Setter
    private Channel1Dto channel1Dto;

    private Channel1() {
        channel1Dto = new Channel1Dto();
    }

    public static Channel1Dto getInstance() {
        if (mInstance == null) {
            mInstance = new Channel1Dto();
        }
        return mInstance;
    }
}