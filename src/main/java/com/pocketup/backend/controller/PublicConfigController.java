package com.pocketup.backend.controller;

import com.pocketup.backend.model.ConfiguracionApp;
import com.pocketup.backend.repository.IConfiguracionAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicConfigController {

    @Autowired
    private IConfiguracionAppRepository configRepository;

    @GetMapping("/estado-sistema")
    public ConfiguracionApp checkEstado() {
        return configRepository.findById(1L).orElseGet(ConfiguracionApp::new);
    }
}