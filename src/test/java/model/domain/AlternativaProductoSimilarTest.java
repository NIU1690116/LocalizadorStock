package model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de validación y datos conservados por AlternativaProductoSimilar
 * Test caja negra para los atributos observables, particiones de equivalencia
 * para referencias obligatorias y similitudes inválidas, y límites para las
 * métricas heredadas de viaje
 */
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
		// Particiones inválidas, DbC: producto y tienda son referencias obligatorias.
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(null, tienda, 50.0, 12, 3.5));
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, null, 50.0, 12, 3.5));
	}

	@Test
	void rechazaGradosDeSimilitudFueraDelRango() {
		// Límite inferior excluido y partición negativa: la similitud debe ser mayor que cero
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, 0.0, 12, 3.5));
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, -1.0, 12, 3.5));
		// Límite superior: un porcentaje mayor que 100 no es válido
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, 100.01, 12, 3.5));
		// Particiones no finitas: NaN e infinito positivo se rechazan
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, Double.NaN, 12, 3.5));
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, Double.POSITIVE_INFINITY, 12, 3.5));
	}

	@Test
	void esUnaAlternativaConTiempoYDistanciaEstimados() {
		// Caja negra: un porcentaje intermedio válido y sus datos se conservan
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
		// Partición inválida, límite: el tiempo de viaje no puede ser negativo
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, 50.0, -1, 3.5));
		// Partición inválida / límite: la distancia no puede ser negativa
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, 50.0, 12, -1.0));
		// Partición no finita: la distancia NaN no es admisible
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, 50.0, 12, Double.NaN));
	}
}
