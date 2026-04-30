package com.pocketup.backend.repository;

import com.pocketup.backend.model.Presupuesto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IPresupuestoRepository extends JpaRepository<Presupuesto, Long> {
    List<Presupuesto> findByUsuarioId(Long usuarioId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Presupuesto p WHERE p.categoria.id = :categoriaId")
    void eliminarPorCategoriaId(@Param("categoriaId") Long categoriaId);

    // 1. CONSULTA PARA CREAR (save) - No pide ID porque es nuevo
    @Query("SELECT COUNT(p) > 0 FROM Presupuesto p " +
            "WHERE p.usuario.id = :usuarioId " +
            "AND p.categoria.id = :categoriaId " +
            "AND (:inicio <= p.fechaFin AND :fin >= p.fechaInicio)")
    boolean existeSolapamiento(
            @Param("usuarioId") Long usuarioId,
            @Param("categoriaId") Long categoriaId,
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin
    );

    // 2. CONSULTA PARA EDITAR (update) - Sí pide ID para ignorarlo
    @Query("SELECT COUNT(p) > 0 FROM Presupuesto p " +
            "WHERE p.usuario.id = :usuarioId " +
            "AND p.categoria.id = :categoriaId " +
            "AND (:inicio <= p.fechaFin AND :fin >= p.fechaInicio) " +
            "AND p.id != :presupuestoId")
    boolean existeSolapamientoEdicion(
            @Param("usuarioId") Long usuarioId,
            @Param("categoriaId") Long categoriaId,
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin,
            @Param("presupuestoId") Long presupuestoId
    );

}
