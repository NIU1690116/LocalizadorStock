package model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlternativaProductoSimilarTest {
	private Producto producto;
	private Tienda tienda;

	@BeforeEach
	void setUp() {
		producto = new Producto("P01", "Chaqueta", "azul", "chaquetas", "mujer");
		tienda = new Tienda("T1", "Tienda calle principal", 41.0, 2.0, new Inventario());
	}

	@Test
	void rechazaProductoOTiendaNulos() {
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(null, tienda, 50.0, 12, 3.5));
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, null, 50.0, 12, 3.5));
	}

	@Test
	void rechazaGradosDeSimilitudFueraDelRango() {
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, 0.0, 12, 3.5));
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, -1.0, 12, 3.5));
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, 100.01, 12, 3.5));
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, Double.NaN, 12, 3.5));
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, Double.POSITIVE_INFINITY, 12, 3.5));
	}

	@Test
	void esUnaAlternativaConTiempoYDistanciaEstimados() {
		AlternativaProductoSimilar alternativa =
				new AlternativaProductoSimilar(producto, tienda, 66.7, 12, 3.5);

		assertEquals(12, alternativa.getTiempoEstimadoMinutos());
		assertEquals(3.5, alternativa.getDistanciaKm());
		assertSame(producto, alternativa.productoSimilar());
		assertSame(tienda, alternativa.tienda());
		assertEquals(66.7, alternativa.gradoSimilitud());
	}

	@Test
	void rechazaMetricasDeViajeInvalidas() {
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, 50.0, -1, 3.5));
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, 50.0, 12, -1.0));
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, 50.0, 12, Double.NaN));
	}
}
