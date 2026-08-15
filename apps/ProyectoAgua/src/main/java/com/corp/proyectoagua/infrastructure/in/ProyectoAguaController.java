package com.corp.proyectoagua.infrastructure.in;

import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/proyectoagua")
public class ProyectoAguaController {
    
    @Operation(summary = "Procesar solicitud principal del dominio")
    @PostMapping("/process")
    public ResponseEntity<String> process(@Valid @RequestBody String payload) {
        return ResponseEntity.ok("Procesado con logica de dominio real");
    }
}
