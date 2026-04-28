package com.pocketup.backend.service;

import com.pocketup.backend.model.Personaje;
import com.pocketup.backend.repository.IPersonajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonajeService implements IPersonajeService {
    @Autowired
    private IPersonajeRepository personaje_repository;

    // Constante: Cuánta XP se necesita para subir un nivel
    private static final int XP_POR_NIVEL = 100;


    @Override
    public void sumarExperiencia(Long usuarioId, int cantidadXp) {
        Personaje personaje = personaje_repository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Avatar no encontrado para este usuario"));
        int nuevaXpTotal = personaje.getXp() + cantidadXp;
        personaje.setXp(nuevaXpTotal);
        int nuevoNivel = (nuevaXpTotal / XP_POR_NIVEL) + 1;
        // 4. Si ha subido de nivel, actualizamos y verificamos si cambia de rango
        if (nuevoNivel > personaje.getNivel()) {
            personaje.setNivel(nuevoNivel);
            actualizarRango(personaje);
            // Aquí en el futuro podríamos añadir lógica para desbloquear Skins
        }
        personaje_repository.save(personaje);
    }

    private void actualizarRango(Personaje personaje) {
        int nivel = personaje.getNivel();
        if (nivel >= 20) {
            personaje.setRango("Lobo de Wall Street");
        } else if (nivel >= 10) {
            personaje.setRango("Maestro del Ahorro");
        } else if (nivel >= 5) {
            personaje.setRango("Inversor Estratégico");
        } else if (nivel >= 2) {
            personaje.setRango("Aprendiz Financiero");
        } else {
            personaje.setRango("Ahorrador Novato");
        }
    }
}
