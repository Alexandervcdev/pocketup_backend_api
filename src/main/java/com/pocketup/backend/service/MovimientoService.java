package com.pocketup.backend.service;

import com.pocketup.backend.dto.MovimientoRequest;
import com.pocketup.backend.dto.MovimientoResponse;
import com.pocketup.backend.model.Movimiento;
import com.pocketup.backend.model.Usuario;
import com.pocketup.backend.repository.IMovimientoRepository;
import com.pocketup.backend.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoService implements IMovimientoService {
    @Autowired
    private IMovimientoRepository movimiento_repository;
    @Autowired
    private IUsuarioRepository usuario_repository;

    @Override
    public MovimientoResponse saveMovement(MovimientoRequest movimiento_request) {
        // importe no sea negativo o cero
        if (movimiento_request.getImporte() == null || movimiento_request.getImporte().signum() <= 0) {
            throw new RuntimeException("El importe debe ser mayor a 0");
        }
        Usuario user = usuario_repository.findById(movimiento_request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Movimiento newMovimiento = new Movimiento();
        newMovimiento.setImporte(movimiento_request.getImporte());
        newMovimiento.setFecha(movimiento_request.getFecha());
        newMovimiento.setTipo(movimiento_request.getTipo());
        newMovimiento.setNota(movimiento_request.getNota());
        newMovimiento.setUsuario(user);
        
        Movimiento guardado = movimiento_repository.save(newMovimiento);
        // Devolvemos el DTO en lugar de la entidad
        return mapToResponse(guardado);
    }

    // Método auxiliar de "Mapeo"
    private MovimientoResponse mapToResponse(Movimiento m) {
        return new MovimientoResponse(
                m.getId(),
                m.getImporte(),
                m.getFecha(),
                m.getTipo(),
                m.getNota(),
                m.getUsuario().getId()
        );
    }

    @Override
    public List<MovimientoResponse> GetMovements(Long usuarioId) {
        if (!usuario_repository.existsById(usuarioId)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        List<Movimiento> movimientos = movimiento_repository.findByUsuarioIdOrderByFechaDesc(usuarioId);
        return movimientos.stream()
                .map(this::mapToResponse) // mapeo de [Movimiento] a [MovimientosResponse]
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteMovements(List<Long> ids) {
        movimiento_repository.deleteAllByIdInBatch(ids);
    }
}
