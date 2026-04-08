package com.pocketup.backend.service;

import com.pocketup.backend.dto.UsuarioRegistroRequest;
import com.pocketup.backend.model.Usuario;

public interface IUsuarioService {
    public Usuario saveUser(UsuarioRegistroRequest usario);
}
