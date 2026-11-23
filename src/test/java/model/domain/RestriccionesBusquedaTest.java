package model.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestriccionesBusquedaTest {
    private RestriccionesBusqueda restricciones;

    @BeforeEach
    void setUp() {
    	restricciones = new RestriccionesBusqueda();
    }
    
    @Test
    void testLimitesDistanciasPermitidos() {
		// Decision + condition coverage: combina nulo, no finito y ambos límites.
    	assertTrue(restricciones.permiteDistancia(8.5));//inferior al limite
    	assertTrue(restricciones.permiteDistancia(10.0));//justo en el limite
        assertFalse(restricciones.permiteDistancia(10.1)); // superior al limite
        
        assertFalse(restricciones.permiteDistancia(null));// valor nulo
        assertFalse(restricciones.permiteDistancia(-1.0));// valor negativo
        assertTrue(restricciones.permiteDistancia(0.0));// origen y destino pueden coincidir
        assertFalse(restricciones.permiteDistancia(Double.NaN));// distancia no numérica
        assertFalse(restricciones.permiteDistancia(Double.NEGATIVE_INFINITY));
        assertFalse(restricciones.permiteDistancia(Double.POSITIVE_INFINITY));
        
    }
    
    
    @Test
    void testLimiteEsperaPermitida() {
		// Decision + condition coverage: combina nulo, valores negativos y ambos límites.
    	assertTrue(restricciones.permiteEspera(60));//inferior al limite
    	assertTrue(restricciones.permiteEspera(120));//justo en el limite
    	assertFalse(restricciones.permiteEspera(121)); // superior al limite
    	
    	assertFalse(restricciones.permiteEspera(null));// valor nulo
        assertFalse(restricciones.permiteEspera(-60));// valor negativo
        assertFalse(restricciones.permiteEspera(Integer.MIN_VALUE));
        assertTrue(restricciones.permiteEspera(0));// reposición inmediata
        assertFalse(restricciones.permiteEspera(Integer.MAX_VALUE));
    }

	@Test
	void testGetDistanciaMaximaKm() {
		assertEquals(10.0, restricciones.getDistanciaMaximaKm(), 0.0);
	}

}
