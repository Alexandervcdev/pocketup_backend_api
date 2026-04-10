package com.pocketup.backend.dto;

import com.pocketup.backend.model.MovementType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO que recibe los datos limpios y seguros desde la aplicación Android.
 * Solo contiene lo que el usuario puede rellenar en el formulario.
 */
@Getter
@Setter
public class MovimientoRequest {

    private BigDecimal importe;
    private LocalDate fecha;
    private MovementType tipo;
    private String nota;

    // Necesitamos saber a quién pertenece este movimiento.
    // Android nos enviará el ID del usuario logueado.
    private Long usuarioId;
}