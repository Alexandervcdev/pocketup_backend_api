package com.pocketup.backend.repository;

import com.pocketup.backend.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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


    @Query(value = "SELECT SUM(importe) FROM movimiento " +
            "WHERE usuario_id = :usuarioId " +
            "AND categoria_id = :categoriaId " +
            "AND tipo = :tipo " +
            "AND fecha LIKE :mesAnio",
            nativeQuery = true) // <-- ESTO APAGA EL TRADUCTOR HQL Y LO HACE INFALIBLE
    BigDecimal sumarGastosPorCategoriaYMes(
            @Param("usuarioId") Long usuarioId,
            @Param("categoriaId") Long categoriaId,
            @Param("tipo") String tipo, // <-- Recibimos un String simple
            @Param("mesAnio") String mesAnio);
}