package com.pocketup.backend.repository;

import com.pocketup.backend.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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

}