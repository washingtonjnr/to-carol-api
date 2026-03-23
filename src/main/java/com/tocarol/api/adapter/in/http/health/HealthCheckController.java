package com.tocarol.api.adapter.in.http.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tocarol.api.domain.port.in.CheckHealthUseCase;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    private final CheckHealthUseCase checkHealthUseCase;
    private final String name;
    private final String version;

    public HealthCheckController(
            CheckHealthUseCase checkHealthUseCase,
            @Value("${spring.application.name}") String name,
            @Value("${spring.application.version}") String version) {
        this.checkHealthUseCase = checkHealthUseCase;
        this.name = name;
        this.version = version;
    }

    @GetMapping
    public ResponseEntity<HealthResponse> check() {
        return ResponseEntity.ok(HealthMapper.toResponse(checkHealthUseCase.execute(), name, version));
    }
}