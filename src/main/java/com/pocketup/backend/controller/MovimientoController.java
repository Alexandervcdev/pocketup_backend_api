package com.pocketup.backend.controller;

import com.pocketup.backend.dto.MovimientoRequest;
import com.pocketup.backend.dto.MovimientoResponse;
import com.pocketup.backend.model.Movimiento;
import com.pocketup.backend.service.IMovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos") // Ruta base para todo lo relacionado con ingresos/gastos
public class MovimientoController {

    @Autowired
    private IMovimientoService movimiento_service;

    /**
     * Endpoint para crear un nuevo movimiento (Ingreso o Gasto).
     */
    @PostMapping("/nuevo")
    public ResponseEntity<MovimientoResponse> saveMovement(@RequestBody MovimientoRequest movimiento_request) {
        return ResponseEntity.ok(movimiento_service.saveMovement(movimiento_request));
    }

    /**
     * Endpoint para obtener el historial de un usuario.
     */
    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<MovimientoResponse>> listarMovimientos(@PathVariable Long id) {
        return ResponseEntity.ok(movimiento_service.GetMovements(id));
    }
}