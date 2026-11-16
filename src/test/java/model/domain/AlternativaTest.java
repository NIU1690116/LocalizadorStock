package model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AlternativaTest {
	
	private static class AlternativaDePrueba extends Alternativa {
		private AlternativaDePrueba(int tiempoEstimadoMinutos, double distanciaKm) {
			super(tiempoEstimadoMinutos, distanciaKm);
		}
	}
	
	@Test
	void testLimitesValidos() {
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaDePrueba(-1, 5.0));//tiempo negativo
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaDePrueba(10, -1.0));//distancia negativa
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaDePrueba(10, Double.NaN));//distancia finita
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaDePrueba(10, Double.POSITIVE_INFINITY));// distancia finita

	}
	
	@Test
	void testAlternativaValida() {
		Alternativa alternativa = new AlternativaDePrueba(25, 4.5);

		assertEquals(25, alternativa.getTiempoEstimadoMinutos());
		assertEquals(4.5, alternativa.getDistanciaKm(), 0.001);
	}

}
