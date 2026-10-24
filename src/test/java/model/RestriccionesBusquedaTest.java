package test.java.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.model.RestriccionesBusqueda;

class RestriccionesBusquedaTest {
    private RestriccionesBusqueda restricciones;

    @BeforeEach
    void setUp() {
    	restricciones = new RestriccionesBusqueda();
    }
    
    @Test
    void testLimitesPermitidos() {
    	assertTrue(restricciones.permiteDistancia(8.5));//inferior al limite
    	assertTrue(restricciones.permiteDistancia(10.0));//justo en el limite
        assertFalse(restricciones.permiteDistancia(10.1)); // superior al limite
        
        assertFalse(restricciones.permiteDistancia(null));// valor nulo
        assertFalse(restricciones.permiteDistancia(-1.0));// valor negativo
        assertFalse(restricciones.permiteDistancia(0.0));// valor 0
        
    }
    
    
    @Test
    void 

}
