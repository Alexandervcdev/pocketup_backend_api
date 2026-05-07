package com.pocketup.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class ConfiguracionApp {

    @Id
    private Long id = 1L; // Siempre será 1, solo habrá una fila de configuración
    private int xpPorNivel = 100; // Valor por defecto
    private boolean mantenimientoActivo = false;
    private String tituloMantenimiento = "Mantenimiento en curso";
    private String mensajeMantenimiento = "Estamos mejorando PocketUp para ti. Volveremos pronto";
    public ConfiguracionApp() {}
}
