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

	}

}
