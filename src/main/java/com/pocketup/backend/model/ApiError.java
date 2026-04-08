package com.pocketup.backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApiError {
    private String error; // Le llamo "error" para que coincida con la lectura en Android
    private int status;

    public ApiError(String error, int status) {
        this.error = error;
        this.status = status;
    }
}
