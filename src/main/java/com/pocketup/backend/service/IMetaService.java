package com.pocketup.backend.service;

import com.pocketup.backend.model.Meta;

import java.math.BigDecimal;
import java.util.List;

public interface IMetaService {
    List<Meta> listarPorUsuario(Long usuarioId);
    Meta save(Meta meta);
    Meta update(Long id, Meta metaActualizada);
    void delete(Long id);

    // MÉTODO ESTRELLA: Para sumar dinero a la hucha directamente
    Meta agregarFondos(Long id, BigDecimal cantidadAAnadir);
}
