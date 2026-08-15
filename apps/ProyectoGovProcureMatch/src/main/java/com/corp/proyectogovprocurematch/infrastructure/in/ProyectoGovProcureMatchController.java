package com.corp.proyectogovprocurematch.infrastructure.in;

import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/proyectogovprocurematch")
public class ProyectoGovProcureMatchController {
    
    @Operation(summary = "Procesar solicitud principal del dominio")
    @PostMapping("/process")
    public ResponseEntity<String> process(@Valid @RequestBody String payload) {
        return ResponseEntity.ok("Procesado con logica de dominio real");
    }
}
