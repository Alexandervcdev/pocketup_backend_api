package com.pocketup.backend.dto;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsuarioUpdateRequest {
    private String nombre;
    private String pais;
    private String idioma;
    private String moneda;
}