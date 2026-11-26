package model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de validación y conservación de datos de AlternativaReposicion
 * Aplican particiones de equivalencia para referencias obligatorias y caja negra
 * para comprobar los atributos observables de una alternativa válida
 */
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
		// Partición inválida, DbC: la tienda asociada es obligatoria
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaReposicion(null, reposicion, 45, 0.0));
		// Partición inválida, DbC: los datos de reposición son obligatorios
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaReposicion(tienda, null, 45, 0.0));
	}
	
	@Test
	void testAlternativaReposicionValida() {
		// Caja negra: una alternativa válida conserva tienda, reposición, tiempo y distancia.
		AlternativaReposicion alternativa = new AlternativaReposicion(
				tienda, reposicion, 45, 0.0);

		assertEquals(tienda, alternativa.getTienda());
		assertEquals(reposicion, alternativa.getReposicion());
		assertEquals(45, alternativa.getTiempoEstimadoMinutos());
		assertEquals(0.0, alternativa.getDistanciaKm(), 0.001);		
	}
}
