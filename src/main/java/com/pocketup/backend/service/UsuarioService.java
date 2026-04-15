package com.pocketup.backend.service;

import com.pocketup.backend.dto.UsuarioLoginRequest;
import com.pocketup.backend.dto.UsuarioRegistroRequest;
import com.pocketup.backend.dto.UsuarioRequest;
import com.pocketup.backend.dto.UsuarioUpdateRequest;
import com.pocketup.backend.model.Usuario;
import com.pocketup.backend.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService{
    @Autowired
    private IUsuarioRepository usuario_repository;

    /**
     * Sincroniza la autenticación social (ej. Google/Firebase) con la base de datos local.
     * Verifica si el usuario ya existe por su correo electrónico; si es así, lo devuelve (Login).
     * Si no existe, lo registra automáticamente usando el UID proporcionado como contraseña
     * y le asigna los valores iniciales por defecto (Nivel 1, 0 XP, Moneda EUR).
     *
     * @param user DTO con los datos extraídos del proveedor de identidad (Nombre, Email, UID).
     * @return El objeto Usuario sincronizado (ya sea existente o recién creado).
     */
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

    /**
     * Gestiona el registro manual de un nuevo usuario en el sistema.
     * Aplica reglas de validación de negocio críticas:
     * - Unicidad del correo electrónico.
     * - Longitud mínima del nombre (3 caracteres).
     * - Longitud mínima de la contraseña (8 caracteres).
     * - Formato básico del correo (debe contener '@').
     * Si alguna validación falla, lanza una RuntimeException que será interceptada por el blindaje.
     * Si tiene éxito, guarda al usuario con estadísticas iniciales por defecto.
     *
     * @param user DTO con los datos ingresados en el formulario de registro.
     * @return El objeto Usuario recién persistido en la base de datos.
     * @throws RuntimeException Si se viola alguna regla de validación de negocio.
     */
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

    /**
     * Método utilitario de búsqueda dinámica para localizar un usuario en la base de datos.
     * Permite buscar tanto por ID único como por correo electrónico, dependiendo
     * de los datos disponibles en la petición.
     *
     * @param user_request DTO que contiene el ID o el Email a buscar.
     * @return Un Optional que contiene el Usuario si fue encontrado, o un Optional vacío si no existe.
     */
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

    /**
     * Procesa la solicitud de inicio de sesión manual.
     * 1. Busca al usuario por su correo electrónico utilizando el método findUser.
     * 2. Si no lo encuentra, lanza una excepción genérica por seguridad.
     * 3. Si lo encuentra, compara la contraseña ingresada con la almacenada.
     * Nota de seguridad: Se utilizan mensajes de error genéricos ("Credenciales incorrectas")
     * para evitar la enumeración de usuarios (no revelar si el fallo fue el correo o la clave).
     *
     * @param login_req DTO con las credenciales (Email y Contraseña) ingresadas por el usuario.
     * @return El objeto Usuario autenticado si las credenciales son correctas.
     * @throws RuntimeException Si el usuario no existe o la contraseña no coincide.
     */
    @Override
    public Usuario login(UsuarioLoginRequest login_req) {
        UsuarioRequest search = new UsuarioRequest();
        search.setEmail(login_req.getEmail());
        Usuario usuario = findUser(search)
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));
        // Una vez encontrado, lógica de seguridad
        if (!usuario.getPassword().equals(login_req.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }
        return usuario;
    }

    @Override
    public Usuario updateUser(Long id, UsuarioUpdateRequest user_update_request) {
        Usuario user = usuario_repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        if (user_update_request.getNombre() != null && !user_update_request.getNombre().trim().isEmpty()) {
            user.setNombre(user_update_request.getNombre());
        }
        if (user_update_request.getPais() != null) {
            user.setPais(user_update_request.getPais());
        }
        if (user_update_request.getIdioma() != null) {
            user.setIdioma(user_update_request.getIdioma());
        }
        if (user_update_request.getMoneda() != null) {
            user.setMoneda(user_update_request.getMoneda());
        }
        return usuario_repository.save(user);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuario_repository.findById(id);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (usuario_repository.existsById(id)) {
            usuario_repository.deleteById(id);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

}
