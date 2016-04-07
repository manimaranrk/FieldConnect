package com.hetro.FieldConnect.DtoController;

import com.hetro.FieldConnect.DTO.UserDetailsDto;

import lombok.Getter;
import lombok.Setter;

public class UserDetails {

    private static UserDetailsDto mInstance = null;

    @Getter
    @Setter
    private UserDetailsDto userDetailsDto;

    private UserDetails() {
        userDetailsDto = new UserDetailsDto();
    }

    public static UserDetailsDto getInstance() {
        if (mInstance == null) {
            mInstance = new UserDetailsDto();
        }
        return mInstance;
    }
}