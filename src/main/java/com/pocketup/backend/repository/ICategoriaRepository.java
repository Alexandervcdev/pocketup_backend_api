package com.pocketup.backend.repository;

import com.pocketup.backend.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
    // Busca categorías globales (sistema) y las personalizadas de un usuario
    @Query("SELECT c FROM Categoria c WHERE c.usuario IS NULL OR c.usuario.id = :usuarioId")
    List<Categoria> findByUsuarioIdOrGlobal(Long usuarioId);

    // Para buscar la categoría que utilizaremos como "General o default"
    Optional<Categoria> findByNombreAndUsuarioIsNull(String nombre);
}
