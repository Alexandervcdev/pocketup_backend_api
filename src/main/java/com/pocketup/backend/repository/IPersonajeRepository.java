package com.pocketup.backend.repository;

import com.pocketup.backend.model.Personaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IPersonajeRepository extends JpaRepository<Personaje, Long> {
    Optional<Personaje> findByUsuarioId(Long usuarioId);
}
