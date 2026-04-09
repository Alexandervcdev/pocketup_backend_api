package com.pocketup.backend.service;

import com.pocketup.backend.dto.UsuarioLoginRequest;
import com.pocketup.backend.dto.UsuarioRegistroRequest;
import com.pocketup.backend.dto.UsuarioRequest;
import com.pocketup.backend.model.Usuario;
import com.pocketup.backend.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService{
    @Autowired
    private IUsuarioRepository usuario_repository;

    @Override
    public Usuario registerOrLoginSocial(UsuarioRegistroRequest user) {
        Optional<Usuario> user_exist = usuario_repository.findByEmail(user.getEmail());
        if (user_exist.isPresent()) {
            return user_exist.get();
        }
        Usuario new_user = new Usuario();
        new_user.setNombre(user.getNombre());
        new_user.setEmail(user.getEmail());
        // Guardamos el UID de Firebase como password (o algo que indique que es social)
        new_user.setPassword(user.getPassword());
        new_user.setNivel(1);
        new_user.setXp(0);
        new_user.setMoneda("EUR");
        return usuario_repository.save(new_user);
    }

    @Override
    public Usuario saveUser(UsuarioRegistroRequest user) {
        Optional<Usuario> user_exist = usuario_repository.findByEmail(user.getEmail());
        if(user_exist.isPresent()){
            throw new RuntimeException("El correo electrónico ya está registrado");
        }
        if (user.getNombre().length() < 3) {
            throw new RuntimeException("El nombre es demasiado corto");
        }

        if(user.getPassword().length() < 8){
            throw new RuntimeException("La contraseña debe tener al menos 8 caracteres");
        }
        if(!user.getEmail().contains("@")){
            throw new RuntimeException("Formato de correo inválido");
        }

        Usuario new_user = new Usuario();
        new_user.setNombre(user.getNombre());
        new_user.setEmail(user.getEmail());
        new_user.setPassword(user.getPassword()); //Cifrar despues

        new_user.setNivel(1);
        new_user.setXp(0);
        new_user.setMoneda("EUR");
        return usuario_repository.save(new_user);
    }

    @Override
    public Optional<Usuario> findUser(UsuarioRequest user_request) {
        if(user_request.getId() != null){
            return usuario_repository.findById(user_request.getId());
        }
        if (user_request.getEmail() != null) {
            return usuario_repository.findByEmail(user_request.getEmail());
        }
        return Optional.empty();
    }

    @Override
    public Usuario login(UsuarioLoginRequest loginReq) {
        return null;
    }

}
