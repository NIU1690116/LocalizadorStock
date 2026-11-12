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
}
