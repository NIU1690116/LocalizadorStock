package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.RestriccionesBusqueda;

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
        assertTrue(restricciones.permiteDistancia(0.0));// origen y destino pueden coincidir
        assertFalse(restricciones.permiteDistancia(Double.NaN));// distancia no numérica
        
    }
    
    
    @Test
    void testLimiteEsperaPermitida() {
    	assertTrue(restricciones.permiteEspera(60));//inferior al limite
    	assertTrue(restricciones.permiteEspera(120));//justo en el limite
    	assertFalse(restricciones.permiteEspera(121)); // superior al limite
    	
    	assertFalse(restricciones.permiteEspera(null));// valor nulo
        assertFalse(restricciones.permiteEspera(-60));// valor negativo
        assertTrue(restricciones.permiteEspera(0));// reposición inmediata
    }

}
