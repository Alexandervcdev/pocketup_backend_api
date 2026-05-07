package com.pocketup.backend.service;

import com.pocketup.backend.dto.MovimientoRequest;
import com.pocketup.backend.dto.MovimientoResponse;
import com.pocketup.backend.model.Categoria;
import com.pocketup.backend.model.MovementType;
import com.pocketup.backend.model.Movimiento;
import com.pocketup.backend.model.Usuario;
import com.pocketup.backend.repository.ICategoriaRepository;
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
    @Autowired
    private ICategoriaRepository categoria_repository;
    @Autowired
    private PersonajeService personaje_service;

    @Override
    public MovimientoResponse saveMovement(MovimientoRequest movimiento_request) {
        // importe no sea negativo o cero
        if (movimiento_request.getImporte() == null || movimiento_request.getImporte().signum() <= 0) {
            throw new RuntimeException("El importe debe ser mayor a 0");
        }
        // 2. BUSCAMOS LA CATEGORÍA QUE ELIGIÓ EL USUARIO EN ANDROID
        Categoria categoria = categoria_repository.findById(movimiento_request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Usuario user = usuario_repository.findById(movimiento_request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Movimiento newMovimiento = new Movimiento();
        newMovimiento.setNombre(movimiento_request.getNombre());
        newMovimiento.setImporte(movimiento_request.getImporte());
        newMovimiento.setFecha(movimiento_request.getFecha());
        newMovimiento.setTipo(movimiento_request.getTipo());
        newMovimiento.setNota(movimiento_request.getNota());
        newMovimiento.setCategoria(categoria);
        newMovimiento.setUsuario(user);

        Movimiento guardado = movimiento_repository.save(newMovimiento);
        // Damos recompensa de XP (Ejemplo: 10 XP por registrar un gasto, 20 XP por un ingreso)
        int xpGanada = (guardado.getTipo() == MovementType.INGRESO) ? 20 : 10;
        personaje_service.sumarExperiencia(guardado.getUsuario().getId(), xpGanada);

        // Devolvemos el DTO en lugar de la entidad
        return mapToResponse(guardado);
    }

    @Override
    public MovimientoResponse updateMovement(Long id, MovimientoRequest request) {
        // 1. Buscamos el movimiento que queremos editar
        Movimiento existente = movimiento_repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        // 2. Buscamos la categoría (por si el usuario la cambió)
        Categoria categoria = categoria_repository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // 3. Actualizamos todos los campos con los nuevos datos del Request
        existente.setNombre(request.getNombre()); // <-- El nuevo campo
        existente.setImporte(request.getImporte());
        existente.setFecha(request.getFecha());
        existente.setTipo(request.getTipo());
        existente.setNota(request.getNota());
        existente.setCategoria(categoria);

        // 4. Guardamos en la base de datos
        Movimiento actualizado = movimiento_repository.save(existente);

        // 5. Devolvemos la respuesta mapeada
        return mapToResponse(actualizado);
    }

    // Método auxiliar de "Mapeo"
    private MovimientoResponse mapToResponse(Movimiento m) {
        return new MovimientoResponse(
                m.getId(),
                m.getNombre(),
                m.getImporte(),
                m.getFecha(),
                m.getTipo(),
                m.getNota(),
                m.getUsuario().getId(),
                m.getCategoria()
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
