package com.pocketup.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "personajes")
public class Personaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre = "Mi Avatar";
    private Integer nivel = 1;
    private Integer xp = 0;
    private String rango = "Ahorrador Novato";
    private Integer skinActiva = 1;


    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @JsonIgnore
    private Usuario usuario;

    public Personaje() {}

    public  Personaje(Usuario usuario) {
        this.usuario = usuario;
    }
}