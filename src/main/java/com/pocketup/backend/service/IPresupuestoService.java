package com.pocketup.backend.service;

import com.pocketup.backend.model.Presupuesto;

import java.util.List;

public interface IPresupuestoService {
    List<Presupuesto> listarPorUsuario(Long usuarioId);
    Presupuesto save(Presupuesto presupuesto);
    Presupuesto update(Long id, Presupuesto presupuestoActualizado);
    void delete(Long id);
}
