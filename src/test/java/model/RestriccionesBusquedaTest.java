package test.java.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.model.RestriccionesBusqueda;

class RestriccionesBusquedaTest {
    private final LocalDateTime limite = LocalDateTime.of(2026, 10, 22, 12, 0);
    private RestriccionesBusqueda restricciones;

    @BeforeEach
    void setUp() {
    	restricciones = new RestriccionesBusqueda(10.0, 120, limite);
    }
    
    @Test
    void testLimitesPermitidos() {
    	assertTrue(restricciones.permiteDistancia(8.5));//inferior al limite
    	assertTrue(restricciones.permiteDistancia(10.0));//justo en el limite
        assertFalse(restricciones.permiteDistancia(10.1)); // superior al limite
    }

}
