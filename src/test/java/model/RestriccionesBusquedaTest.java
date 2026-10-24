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
    void testLimitesDistanciasPermitidos() {
    	assertTrue(restricciones.permiteDistancia(8.5));//inferior al limite
    	assertTrue(restricciones.permiteDistancia(10.0));//justo en el limite
        assertFalse(restricciones.permiteDistancia(10.1)); // superior al limite
        
        assertFalse(restricciones.permiteDistancia(null));// valor nulo
        assertFalse(restricciones.permiteDistancia(-1.0));// valor negativo
        assertFalse(restricciones.permiteDistancia(0.0));// valor 0
        
    }
    
    
    @Test
    void testLimiteEsperaPermitida() {
    	assertTrue(restricciones.permiteEsperaMin(60));//inferior al limite
    	assertTrue(restricciones.permiteEspera(120));//justo en el limite
    	assertFalse(restricciones.permiteEspera(121)); // superior al limite
    	
    	assertFalse(restricciones.permiteEspera(null));// valor nulo
        assertFalse(restricciones.permiteEspera(-60));// valor negativo
        assertFalse(restricciones.permiteEspera(0));// valor 0 			
    }

}
