package com.pocketup.backend.dto;

import com.pocketup.backend.model.Categoria;
import com.pocketup.backend.model.MovementType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoResponse {
    private Long id;
    private BigDecimal importe;
    private LocalDate fecha;
    private MovementType tipo;
    private String nota;
    private Long usuarioId;
    private Categoria categoria;
}