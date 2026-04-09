package com.pocketup.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsuarioLoginRequest {
    private String email;
    private String password;
}
