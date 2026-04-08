package com.pocketup.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsuarioRegistroRequest {
    private String nombre;
    private String email;
    private String password;
}
