package com.pocketup.backend.service;

import com.pocketup.backend.model.Categoria;

import java.util.List;

public interface ICategoriaService {
    List<Categoria> listarPorUsuario(Long usuarioId);
    Categoria save(Categoria categoria);
    Categoria update(Long id, Categoria categoriaActualizada);
    void delete(Long idABorrar);

}
