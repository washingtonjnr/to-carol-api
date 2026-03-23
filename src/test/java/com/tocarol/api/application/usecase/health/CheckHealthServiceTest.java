package com.tocarol.api.application.usecase.health;

import com.tocarol.api.domain.model.HealthStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CheckHealthServiceTest {

    private final CheckHealthService service = new CheckHealthService();

    @Test
    void execute_returnsStatusUp() {
        HealthStatus result = service.execute();

        assertThat(result.status()).isEqualTo(HealthStatus.Status.UP);
    }
}
