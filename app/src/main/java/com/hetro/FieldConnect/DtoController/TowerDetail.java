package com.hetro.FieldConnect.DtoController;

import com.hetro.FieldConnect.DTO.TowerDetailDto;

import lombok.Getter;
import lombok.Setter;

public class TowerDetail {

    private static TowerDetailDto mInstance = null;

    @Getter
    @Setter
    private TowerDetailDto towerDetailsDto;

    private TowerDetail() {
        towerDetailsDto = new TowerDetailDto();
    }

    public static TowerDetailDto getInstance() {
        if (mInstance == null) {
            mInstance = new TowerDetailDto();
        }
        return mInstance;
    }
}