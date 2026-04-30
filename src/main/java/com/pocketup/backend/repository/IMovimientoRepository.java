package com.pocketup.backend.repository;

import com.pocketup.backend.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IMovimientoRepository extends JpaRepository<Movimiento, Long> {

    /**
     * MAGIA DE SPRING BOOT:
     * Solo con llamar al método "findByUsuarioIdOrderByFechaDesc", Spring crea una consulta SQL
     * que busca todos los movimientos de un usuario específico y los ordena de más nuevos a más antiguos.
     * ¡Perfecto para tu pantalla de Inicio en Android!
     * * @param usuarioId El ID del usuario.
     * @return Lista de movimientos ordenados cronológicamente.
     */
    List<Movimiento> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    List<Movimiento> findByCategoriaId(Long categoriaId);


    @Query("SELECT SUM(m.importe) FROM Movimiento m " +
            "WHERE m.usuario.id = :usuarioId " +
            "AND m.categoria.id = :categoriaId " +
            "AND m.tipo = 'GASTO' " +
            "AND m.fecha BETWEEN :inicio AND :fin")
    BigDecimal sumarGastosPorCategoriaYRango(
            @Param("usuarioId") Long usuarioId,
            @Param("categoriaId") Long categoriaId,
            @Param("inicio") LocalDate inicio, // <-- Cambiado a LocalDate
            @Param("fin") LocalDate fin       // <-- Cambiado a LocalDate
    );
}