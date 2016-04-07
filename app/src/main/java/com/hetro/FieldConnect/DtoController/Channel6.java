package com.hetro.FieldConnect.DtoController;

import com.hetro.FieldConnect.DTO.Channel6Dto;

import lombok.Getter;
import lombok.Setter;

public class Channel6 {

    private static Channel6Dto mInstance = null;

    @Getter
    @Setter
    private Channel6Dto channel6Dto;

    private Channel6() {
        channel6Dto = new Channel6Dto();
    }

    public static Channel6Dto getInstance() {
        if (mInstance == null) {
            mInstance = new Channel6Dto();
        }
        return mInstance;
    }
}