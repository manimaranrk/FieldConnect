package com.hetro.FieldConnect.DtoController;

import com.hetro.FieldConnect.DTO.TowerDetailsDto;

import lombok.Getter;
import lombok.Setter;

public class TowerDetails {

    private static TowerDetailsDto mInstance = null;

    @Getter
    @Setter
    private TowerDetailsDto towerDetailsDto;

    private TowerDetails() {
        towerDetailsDto = new TowerDetailsDto();
    }

    public static TowerDetailsDto getInstance() {
        if (mInstance == null) {
            mInstance = new TowerDetailsDto();
        }
        return mInstance;
    }
}