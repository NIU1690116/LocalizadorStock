package model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Pruebas de las validaciones y atributos comunes de Alternativa
 * Usa una subclase mínima para poder ejercitar la base abstracta y combina caja
 * negra sobre los datos conservados con particiones inválidas y límites válidos
 */
class AlternativaTest {
	
	private static class AlternativaDePrueba extends Alternativa {
		// Subclase mínima: permite instanciar la base abstracta sin añadir comportamiento
		private AlternativaDePrueba(int tiempoEstimadoMinutos, double distanciaKm) {
			super(tiempoEstimadoMinutos, distanciaKm);
		}
	}
	
	@Test
	void testLimitesValidos() {
		// Partición inválida: el tiempo no puede ser negativo
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaDePrueba(-1, 5.0));
		// Partición inválida: la distancia no puede ser negativa
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaDePrueba(10, -1.0));
		// Particiones no finitas: NaN e infinito positivo se rechazan
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaDePrueba(10, Double.NaN));
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaDePrueba(10, Double.POSITIVE_INFINITY));
	}
	
	@Test
	void testAlternativaValida() {
		// Caja negra: valores interiores válidos se conservan en los getters
		Alternativa alternativa = new AlternativaDePrueba(25, 4.5);

		assertEquals(25, alternativa.getTiempoEstimadoMinutos());
		assertEquals(4.5, alternativa.getDistanciaKm(), 0.001);
	}

	@Test
	void testTiempoYDistanciaEnCeroSonValidos() {
		// Prueba de límite inferior: cero es válido para tiempo y distancia
		Alternativa alternativa = new AlternativaDePrueba(0, 0.0);

		assertEquals(0, alternativa.getTiempoEstimadoMinutos());
		assertEquals(0.0, alternativa.getDistanciaKm(), 0.0);
	}

}
