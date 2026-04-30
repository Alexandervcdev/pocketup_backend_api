package com.pocketup.backend.service;

import com.pocketup.backend.model.Presupuesto;
import com.pocketup.backend.repository.IMovimientoRepository;
import com.pocketup.backend.repository.IPresupuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate; // Importante

@Service
public class PresupuestoService implements IPresupuestoService {

    @Autowired
    private IPresupuestoRepository presupuesto_repository;

    @Autowired
    private IMovimientoRepository movimiento_repository;

    @Override
    public List<Presupuesto> listarPorUsuario(Long usuarioId) {
        List<Presupuesto> lista = presupuesto_repository.findByUsuarioId(usuarioId);

        for (Presupuesto p : lista) {
            if (p.getFechaInicio() != null && p.getFechaFin() != null) {
                // Como ya son LocalDate, los pasamos directo al MovimientoRepository
                BigDecimal gastado = movimiento_repository.sumarGastosPorCategoriaYRango(
                        usuarioId,
                        p.getCategoria().getId(),
                        p.getFechaInicio(),
                        p.getFechaFin()
                );
                p.setMontoGastado(gastado != null ? gastado : BigDecimal.ZERO);
            } else {
                p.setMontoGastado(BigDecimal.ZERO);
            }
        }
        return lista;
    }

    @Override
    public Presupuesto save(Presupuesto presupuesto) {
        // Pasamos los LocalDate directamente al repositorio
        boolean solapado = presupuesto_repository.existeSolapamiento(
                presupuesto.getUsuarioId(),
                presupuesto.getCategoriaId(),
                presupuesto.getFechaInicio(),
                presupuesto.getFechaFin()
        );

        if (solapado) {
            throw new RuntimeException("Ya existe un presupuesto para esta categoría en esas fechas.");
        }

        return presupuesto_repository.save(presupuesto);
    }

    @Override
    public Presupuesto update(Long id, Presupuesto presupuestoActualizado) {
        // Verificamos si al editar las fechas o la categoría, choca con OTRO presupuesto
        boolean solapado = presupuesto_repository.existeSolapamientoEdicion(
                presupuestoActualizado.getUsuarioId(),
                presupuestoActualizado.getCategoriaId(),
                presupuestoActualizado.getFechaInicio(),
                presupuestoActualizado.getFechaFin(),
                id // <-- Le decimos que ignore su propio ID
        );

        if (solapado) {
            throw new RuntimeException("Ya existe un presupuesto para esta categoría en esas fechas.");
        }

        Presupuesto existente = presupuesto_repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado"));

        // Actualizamos los campos, incluyendo las nuevas fechas
        existente.setMontoLimite(presupuestoActualizado.getMontoLimite());
        existente.setFechaInicio(presupuestoActualizado.getFechaInicio());
        existente.setFechaFin(presupuestoActualizado.getFechaFin());

        // Si el usuario cambió la categoría al editar
        if (presupuestoActualizado.getCategoriaId() != null) {
            existente.setCategoriaId(presupuestoActualizado.getCategoriaId());
        }

        return presupuesto_repository.save(existente);
    }

    @Override
    public void delete(Long id) {
        presupuesto_repository.deleteById(id);
    }
}