package com.pocketup.backend.service;

import com.pocketup.backend.model.Personaje;

public interface IPersonajeService {
    void sumarExperiencia(Long usuarioId, int cantidadXp);
    void cambiarSkin(Long usuarioId, Integer nuevaSkin);
}
