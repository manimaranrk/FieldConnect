package com.hetro.FieldConnect.DtoController;

import com.hetro.FieldConnect.DTO.Channel3Dto;

import lombok.Getter;
import lombok.Setter;

public class Channel3 {

    private static Channel3Dto mInstance = null;

    @Getter
    @Setter
    private Channel3Dto channel3Dto;

    private Channel3() {
        channel3Dto = new Channel3Dto();
    }

    public static Channel3Dto getInstance() {
        if (mInstance == null) {
            mInstance = new Channel3Dto();
        }
        return mInstance;
    }
}