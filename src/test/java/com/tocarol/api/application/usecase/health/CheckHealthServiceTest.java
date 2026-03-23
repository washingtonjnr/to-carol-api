package com.tocarol.api.application.usecase.health;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.tocarol.api.domain.model.HealthStatus;

class CheckHealthServiceTest {

    private final CheckHealthUseCase service = new CheckHealthUseCase();

    @Test
    void execute_returnsStatusUp() {
        HealthStatus result = service.execute();

        assertThat(result.status()).isEqualTo(HealthStatus.Status.UP);
    }
}
