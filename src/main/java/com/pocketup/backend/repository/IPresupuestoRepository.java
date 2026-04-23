package com.pocketup.backend.repository;

import com.pocketup.backend.model.Presupuesto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPresupuestoRepository extends JpaRepository<Presupuesto, Long> {
    List<Presupuesto> findByUsuarioId(Long usuarioId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Presupuesto p WHERE p.categoria.id = :categoriaId")
    void eliminarPorCategoriaId(@Param("categoriaId") Long categoriaId);

}
