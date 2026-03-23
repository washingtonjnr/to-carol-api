package com.tocarol.api.domain.model;

public record HealthStatus(Status status) {
    public enum Status {
        UP, DOWN
    }
}
