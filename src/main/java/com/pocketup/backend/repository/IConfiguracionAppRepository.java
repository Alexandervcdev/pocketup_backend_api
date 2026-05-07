package com.pocketup.backend.repository;

import com.pocketup.backend.model.ConfiguracionApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IConfiguracionAppRepository extends JpaRepository<ConfiguracionApp, Long> {
}
