package com.pocketup.backend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    // Datos de Perfil (RF 2.1 y 2.3)
    private String nombre;
    private String fotoUrl;
    private String pais;
    private String idioma;
    private String moneda; // Ej: "EUR", "USD"

    // Datos de Gamificación (RF 8.2)
    private Integer nivel = 1;
    private Integer xp = 0;
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Movimiento> movimientos = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(String email, String password, String nombre, String fotoUrl, String pais, String idioma, String moneda, Integer nivel, Integer xp) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.fotoUrl = fotoUrl;
        this.pais = pais;
        this.idioma = idioma;
        this.moneda = moneda;
        this.nivel = nivel;
        this.xp = xp;
    }
}
