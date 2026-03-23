package com.tocarol.api.application.usecase.health;

import org.springframework.stereotype.Service;

import com.tocarol.api.domain.model.HealthStatus;
import com.tocarol.api.domain.port.in.CheckHealthUseCase;

@Service
public class CheckHealthService implements CheckHealthUseCase {

    @Override
    public HealthStatus execute() {
        return new HealthStatus(HealthStatus.Status.UP);
    }
}