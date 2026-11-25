package model.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de los límites fijos de distancia y espera de una búsqueda
 *  se cubren los valores frontera y las particiones nulas, negativas, no finitas,
 * dentro y fuera del rango, comprueban la clase como caja negra mediante sus
 * resultados booleanos y valores máximos
 */
class RestriccionesBusquedaTest {
    private RestriccionesBusqueda restricciones;

    @BeforeEach
    void setUp() {
	restricciones = new RestriccionesBusqueda();
    }
    
    @Test
    void testLimitesDistanciasPermitidos() {
		// Caso interior: una distancia menor que el máximo está permitida.
		assertTrue(restricciones.permiteDistancia(8.5));
		// Límite superior exacto: 10 km se acepta; 10.1 km pertenece a la partición fuera de rango
		assertTrue(restricciones.permiteDistancia(10.0));
        assertFalse(restricciones.permiteDistancia(10.1));
		// Particiones inválidas: nulo y negativo.
        assertFalse(restricciones.permiteDistancia(null));
        assertFalse(restricciones.permiteDistancia(-1.0));
		// Límite inferior exacto: distancia cero es válida si las tiendas coinciden.
        assertTrue(restricciones.permiteDistancia(0.0));
		// Partición no finita: NaN y ambos infinitos se rechazan.
        assertFalse(restricciones.permiteDistancia(Double.NaN));
        assertFalse(restricciones.permiteDistancia(Double.NEGATIVE_INFINITY));
        assertFalse(restricciones.permiteDistancia(Double.POSITIVE_INFINITY));
        
    }
    
    @Test
    void testLimiteEsperaPermitida() {
		// Caso interior: una espera menor que el máximo se acepta
		assertTrue(restricciones.permiteEspera(60));
		// Límite superior exacto: 120 minutos se acepta y 121 se rechaza
		assertTrue(restricciones.permiteEspera(120));
		assertFalse(restricciones.permiteEspera(121));
		// Particiones inválidas: valor nulo y valores negativos, incluido el mínimo entero
		assertFalse(restricciones.permiteEspera(null));
        assertFalse(restricciones.permiteEspera(-60));// valor negativo
        assertFalse(restricciones.permiteEspera(Integer.MIN_VALUE));
		// Límite inferior exacto: una reposición inmediata (cero minutos) se acepta.
		assertTrue(restricciones.permiteEspera(0));
		// Valor máximo representable: queda fuera del rango permitido
        assertFalse(restricciones.permiteEspera(Integer.MAX_VALUE));
    }

	/** Comprueba que el getter expone el máximo de distancia configurado, 10 km */
	@Test
	void testGetDistanciaMaximaKm() {
		assertEquals(10.0, restricciones.getDistanciaMaximaKm(), 0.0);
	}
}