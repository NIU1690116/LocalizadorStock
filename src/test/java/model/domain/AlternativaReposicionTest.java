package model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlternativaReposicionTest {
	Producto producto;
	Tienda tienda;
	Reposicion reposicion;
	
	@BeforeEach
	void setUp(){
		producto = new Producto("P010", "Camiseta", "verde", "camisetas", "mujer");
		tienda = new Tienda("T003", "Tienda Norte", 41.0, 2.0, new Inventario());
		reposicion = new Reposicion(
				producto, 3, LocalDateTime.of(2026, 11, 12, 15, 0));
	}

	@Test
	void testAlternativaReposicion() {

	}
}
