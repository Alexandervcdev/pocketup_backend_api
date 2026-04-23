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

    @Override
    public List<Meta> listarPorUsuario(Long usuarioId) {
        return meta_repository.findByUsuarioId(usuarioId);
    }

    @Override
    public Meta save(Meta meta) {
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

        // Sumamos lo que ya tenía más lo nuevo que metemos a la hucha
        BigDecimal nuevoTotal = existente.getMontoActual().add(cantidadAAnadir);
        existente.setMontoActual(nuevoTotal);

        return meta_repository.save(existente);
    }
}