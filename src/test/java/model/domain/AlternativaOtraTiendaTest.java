package model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AlternativaOtraTiendaTest {

	@Test
	void testTiendaDestinoNula() {
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaOtraTienda(null, 18, 3.2));
	}
	
	@Test
	void testAlternativaOtraTienda() {
		Tienda tienda = new Tienda("T002", "Tienda Centro", 41.0, 2.0, new Inventario());
		AlternativaOtraTienda alternativa = new AlternativaOtraTienda(tienda, 18, 3.2);

		assertEquals(tienda, alternativa.getTienda());
		assertEquals(18, alternativa.getTiempoEstimadoMinutos());
		assertEquals(3.2, alternativa.getDistanciaKm(), 0.001);
	}
}
