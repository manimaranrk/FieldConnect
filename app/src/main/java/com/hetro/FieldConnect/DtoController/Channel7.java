package com.hetro.FieldConnect.DtoController;

import com.hetro.FieldConnect.DTO.Channel7Dto;

import lombok.Getter;
import lombok.Setter;

public class Channel7 {

    private static Channel7Dto mInstance = null;

    @Getter
    @Setter
    private Channel7Dto channel7Dto;

    private Channel7() {
        channel7Dto = new Channel7Dto();
    }

    public static Channel7Dto getInstance() {
        if (mInstance == null) {
            mInstance = new Channel7Dto();
        }
        return mInstance;
    }
}