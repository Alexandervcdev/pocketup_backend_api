package com.pocketup.backend.service;

import com.pocketup.backend.model.Presupuesto;
import com.pocketup.backend.repository.IMovimientoRepository;
import com.pocketup.backend.repository.IPresupuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PresupuestoService implements IPresupuestoService{

    @Autowired
    private IPresupuestoRepository presupuesto_repository;

    @Autowired
    private IMovimientoRepository movimiento_repository;

    @Override
    public List<Presupuesto> listarPorUsuario(Long usuarioId) {
        List<Presupuesto> lista = presupuesto_repository.findByUsuarioId(usuarioId);

        // Obtenemos el mes actual (Ej: "2026-04")
        String mesActual = java.time.LocalDate.now().toString().substring(0, 7);
        String mesFiltro = mesActual + "%";

        for (Presupuesto p : lista) {
            BigDecimal gastado = movimiento_repository.sumarGastosPorCategoriaYMes(
                    usuarioId,
                    p.getCategoria().getId(),
                    "GASTO", // <-- Pasamos el texto exacto a fuego
                    mesFiltro
            );

            p.setMontoGastado(gastado != null ? gastado : BigDecimal.ZERO);
        }

        return lista;
    }

    @Override
    public Presupuesto save(Presupuesto presupuesto) {
        return presupuesto_repository.save(presupuesto);
    }

    @Override
    public Presupuesto update(Long id, Presupuesto presupuestoActualizado) {
        Presupuesto existente = presupuesto_repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado"));

        existente.setMontoLimite(presupuestoActualizado.getMontoLimite());
        return presupuesto_repository.save(existente);
    }

    @Override
    public void delete(Long id) {
        presupuesto_repository.deleteById(id);
    }
}
