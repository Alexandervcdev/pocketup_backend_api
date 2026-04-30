package com.pocketup.backend.service;
import com.pocketup.backend.model.Meta;
import com.pocketup.backend.repository.IMetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MetaService implements IMetaService {

    @Autowired
    private IMetaRepository meta_repository;
    @Autowired
    private IPersonajeService personaje_service;

    @Override
    public List<Meta> listarPorUsuario(Long usuarioId) {
        return meta_repository.findByUsuarioId(usuarioId);
    }

    @Override
    public Meta save(Meta meta) {
        java.time.LocalDate hoy = java.time.LocalDate.now();
        java.time.LocalDate fechaMeta = java.time.LocalDate.parse(meta.getFechaLimite());
        if (fechaMeta.isBefore(hoy)) {
            throw new RuntimeException("La fecha límite no puede ser anterior a hoy");
        }
        // Por seguridad, asegurarnos de que al crear empiece en 0 si no mandaron nada
        if (meta.getMontoActual() == null) {
            meta.setMontoActual(BigDecimal.ZERO);
        }
        return meta_repository.save(meta);
    }

    @Override
    public Meta update(Long id, Meta metaActualizada) {
        Meta existente = meta_repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));

        existente.setNombre(metaActualizada.getNombre());
        existente.setIcono(metaActualizada.getIcono());
        existente.setColor(metaActualizada.getColor());
        existente.setMontoObjetivo(metaActualizada.getMontoObjetivo());
        existente.setFechaLimite(metaActualizada.getFechaLimite());

        return meta_repository.save(existente);
    }

    @Override
    public void delete(Long id) {
        meta_repository.deleteById(id);
    }

    @Override
    public Meta agregarFondos(Long id, BigDecimal cantidadAAnadir) {
        Meta existente = meta_repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));

        // Guardamos si ya estaba completada antes de sumar
        boolean yaEstabaCompletada = existente.getMontoActual().compareTo(existente.getMontoObjetivo()) >= 0;

        BigDecimal nuevoTotal = existente.getMontoActual().add(cantidadAAnadir);

        // Tope de seguridad
        if (nuevoTotal.compareTo(existente.getMontoObjetivo()) > 0) {
            nuevoTotal = existente.getMontoObjetivo();
        }

        existente.setMontoActual(nuevoTotal);
        Meta metaGuardada = meta_repository.save(existente);

        // --- PUNTO 5: PREMIO DE XP ---
        // Si no estaba completada y ahora sí lo está, ¡PREMIO!
        boolean ahoraCompletada = metaGuardada.getMontoActual().compareTo(metaGuardada.getMontoObjetivo()) >= 0;

        if (!yaEstabaCompletada && ahoraCompletada) {
            // Le damos 100 XP por completar una meta (un nivel entero)
            personaje_service.sumarExperiencia(metaGuardada.getUsuario().getId(), 100);
        }

        return metaGuardada;
    }
}