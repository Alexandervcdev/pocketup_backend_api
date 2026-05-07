package com.pocketup.backend.service;

import com.pocketup.backend.model.ConfiguracionApp;
import com.pocketup.backend.model.Personaje;
import com.pocketup.backend.repository.IConfiguracionAppRepository;
import com.pocketup.backend.repository.IPersonajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonajeService implements IPersonajeService {
    @Autowired
    private IPersonajeRepository personaje_repository;

    @Autowired
    private IConfiguracionAppRepository configRepository; // Inyectamos la BD de ajustes


    @Override
    public void sumarExperiencia(Long usuarioId, int cantidadXp) {
        Personaje personaje = personaje_repository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Avatar no encontrado para este usuario"));

        // Leemos la configuración en tiempo real (si no existe, creamos la de por defecto con 100)
        ConfiguracionApp config = configRepository.findById(1L).orElseGet(() -> {
            ConfiguracionApp nueva = new ConfiguracionApp();
            return configRepository.save(nueva);
        });

        int nuevaXpTotal = personaje.getXp() + cantidadXp;
        personaje.setXp(nuevaXpTotal);

        // ¡Usamos la variable dinámica de la base de datos!
        int nuevoNivel = (nuevaXpTotal / config.getXpPorNivel()) + 1;

        if (nuevoNivel > personaje.getNivel()) {
            personaje.setNivel(nuevoNivel);
            actualizarRango(personaje);
        }
        personaje_repository.save(personaje);
    }

    @Override
    public void cambiarSkin(Long usuarioId, Integer nuevaSkin) {
        Personaje personaje = personaje_repository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Avatar no encontrado"));

        int nivelRequerido = 1;
        if (nuevaSkin == 2) nivelRequerido = 2;
        if (nuevaSkin == 3) nivelRequerido = 5;
        if (nuevaSkin == 4) nivelRequerido = 10;
        if (nuevaSkin == 5) nivelRequerido = 20;

        if (personaje.getNivel() < nivelRequerido) {
            throw new RuntimeException("Nivel insuficiente para esta skin");
        }

        personaje.setSkinActiva(nuevaSkin);
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
