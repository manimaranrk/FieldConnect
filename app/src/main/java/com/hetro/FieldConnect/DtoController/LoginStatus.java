package com.hetro.FieldConnect.DtoController;

import com.hetro.FieldConnect.DTO.LoginDto;
import lombok.Getter;
import lombok.Setter;

public class LoginStatus {

    private static LoginDto mInstance = null;

    @Getter
    @Setter
    private LoginDto loginStatusDTO;

    private LoginStatus() {
        loginStatusDTO = new LoginDto();
    }

    public static LoginDto getInstance() {
        if (mInstance == null) {
            mInstance = new LoginDto();
        }
        return mInstance;
    }
}