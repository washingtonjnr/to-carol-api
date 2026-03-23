package com.tocarol.api.adapter.in.http.health;

import com.tocarol.api.domain.model.HealthStatus;
import com.tocarol.api.domain.port.in.CheckHealthUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthCheckController.class)
@TestPropertySource(properties = {
        "spring.application.name=To Carol",
        "spring.application.version=1.0.0"
})
class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CheckHealthUseCase checkHealthUseCase;

    @Test
    void get_returnsOkWithHealthStatus() throws Exception {
        when(checkHealthUseCase.execute())
                .thenReturn(new HealthStatus(HealthStatus.Status.UP));

        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.name").value("To Carol"))
                .andExpect(jsonPath("$.version").value("1.0.0"));
    }
}
