package com.tocarol.api.adapter.in.http.health;

import com.tocarol.api.domain.model.HealthStatus;

public final class HealthMapper {

    private HealthMapper() {}

    public static HealthResponse toResponse(HealthStatus healthStatus, String name, String version) {
        return new HealthResponse(healthStatus.status().name(), name, version);
    }
}