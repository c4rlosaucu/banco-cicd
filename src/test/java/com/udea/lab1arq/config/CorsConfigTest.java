package com.udea.lab1arq.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CorsConfigTest {

    /** Expone la configuracion registrada para poder verificarla. */
    static class InspectableRegistry extends CorsRegistry {
        Map<String, CorsConfiguration> configs() {
            return getCorsConfigurations();
        }
    }

    @Test
    void addCorsMappings_permiteSoloLosOrigenesConfigurados() {
        CorsConfig config = new CorsConfig(new String[]{"http://localhost:5173", "https://mi-front.example.com"});
        InspectableRegistry registry = new InspectableRegistry();

        config.addCorsMappings(registry);

        CorsConfiguration cors = registry.configs().get("/api/**");
        assertNotNull(cors);
        assertEquals(List.of("http://localhost:5173", "https://mi-front.example.com"), cors.getAllowedOrigins());
        assertFalse(cors.getAllowedOrigins().contains("*"));
    }
}