package com.pocketup.backend.service;

import com.pocketup.backend.dto.UsuarioLoginRequest;
import com.pocketup.backend.dto.UsuarioRegistroRequest;
import com.pocketup.backend.dto.UsuarioRequest;
import com.pocketup.backend.model.Usuario;

import java.util.Optional;

public interface IUsuarioService {
    Usuario registerOrLoginSocial(UsuarioRegistroRequest user);
    Usuario saveUser(UsuarioRegistroRequest usario);
    /**
     * Busca una única persona basándose en criterios dinámicos.
     * @param UsuarioRequest Objeto que contiene los posibles campos de búsqueda.
     * @return Un Optional que contiene la persona si se encuentra, o vacío si no.
     */
    Optional <Usuario> findUser(UsuarioRequest user_request);
    Usuario login(UsuarioLoginRequest loginReq);
}
