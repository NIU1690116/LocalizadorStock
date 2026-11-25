package model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Pruebas de construcción de AlternativaOtraTienda
 * Cubren la partición inválida de tienda destino nula y una instancia válida
 * mediante caja negra sobre sus atributos observables
 */
class AlternativaOtraTiendaTest {

	@Test
	void testTiendaDestinoNula() {
		// Partición inválida, DbC: la tienda destino es obligatoria
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaOtraTienda(null, 18, 3.2));
	}
	
	@Test
	void testAlternativaOtraTienda() {
		// Caja negra: se conservan la tienda destino, el tiempo y la distancia válidos
		Tienda tienda = new Tienda("T002", "Tienda Centro", 41.0, 2.0, new Inventario());
		AlternativaOtraTienda alternativa = new AlternativaOtraTienda(tienda, 18, 3.2);

		assertEquals(tienda, alternativa.getTienda());
		assertEquals(18, alternativa.getTiempoEstimadoMinutos());
		assertEquals(3.2, alternativa.getDistanciaKm(), 0.001);
	}
}
