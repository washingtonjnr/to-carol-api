package com.tocarol.api.domain.port.in;

import com.tocarol.api.domain.model.HealthStatus;

public interface CheckHealthUseCase {
    HealthStatus execute();
}
