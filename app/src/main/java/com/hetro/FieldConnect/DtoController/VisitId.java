package com.hetro.FieldConnect.DtoController;

import com.hetro.FieldConnect.DTO.VisitIdDto;

import lombok.Getter;
import lombok.Setter;

public class VisitId {

    private static VisitIdDto mInstance = null;

    @Getter
    @Setter
    private VisitIdDto visitIdDto;

    private VisitId() {
        visitIdDto = new VisitIdDto();
    }

    public static VisitIdDto getInstance() {
        if (mInstance == null) {
            mInstance = new VisitIdDto();
        }
        return mInstance;
    }
}