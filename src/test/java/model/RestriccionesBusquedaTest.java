package test.java.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import main.java.model.RestriccionesBusqueda;

class RestriccionesBusquedaTest {
    private final LocalDateTime limite = LocalDateTime.of(2026, 10, 22, 12, 0);
    private RestriccionesBusqueda restricciones;

    @BeforeEach
    void setUp() {
    	restricciones = new RestriccionesBusqueda(10.0, 120);
    }
    
    @Test
    void testLimitesPermitidos() {
    	
    }

}
