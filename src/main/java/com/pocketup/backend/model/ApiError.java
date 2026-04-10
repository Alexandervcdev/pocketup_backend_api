package com.pocketup.backend.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Modelo de datos (POJO) que estandariza la estructura de las respuestas HTTP fallidas.
 * Es la pieza central del "Sistema de Blindaje" en el backend. Cuando ocurre una
 * excepción de negocio (ej. credenciales inválidas), el GlobalExceptionHandler
 * empaqueta el mensaje en esta clase y lo serializa a JSON.
 * Nota de arquitectura: La propiedad "error" está explícitamente nombrada así
 * para garantizar el contrato exacto de deserialización con la clase ErrorUtil en Android.
 */
@Getter @Setter
public class ApiError {
    private String error; // Le llamo "error" para que coincida con la lectura en Android
    private int status;

    public ApiError(String error, int status) {
        this.error = error;
        this.status = status;
    }
}
