package com.pocketup.backend.repository;

import com.pocketup.backend.model.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMetaRepository extends JpaRepository<Meta, Long> {
    List<Meta> findByUsuarioId(Long usuarioId);
}
