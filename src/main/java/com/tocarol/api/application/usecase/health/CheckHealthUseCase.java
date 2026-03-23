package com.tocarol.api.application.usecase.health;

import org.springframework.stereotype.Service;

import com.tocarol.api.domain.model.HealthStatus;
import com.tocarol.api.domain.port.in.CheckHealthPort;

@Service
public class CheckHealthUseCase implements CheckHealthPort {

    @Override
    public HealthStatus execute() {
        return new HealthStatus(HealthStatus.Status.UP);
    }
}