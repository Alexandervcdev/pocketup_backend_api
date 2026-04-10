package com.pocketup.backend.service;

import com.pocketup.backend.dto.MovimientoRequest;
import com.pocketup.backend.dto.MovimientoResponse;
import com.pocketup.backend.model.Movimiento;

import java.util.List;

public interface IMovimientoService {

    /**
     * Registra un nuevo ingreso o gasto en la base de datos.
     * @param request El DTO con los datos del movimiento enviados desde Android.
     * @return El movimiento guardado.
     */
    MovimientoResponse saveMovement(MovimientoRequest request);

    /**
     * Obtiene el historial completo de movimientos de un usuario, ordenado del más reciente al más antiguo.
     * @param usuarioId El ID del usuario.
     * @return Lista de movimientos.
     */
    List<MovimientoResponse> GetMovements(Long usuarioId);
}