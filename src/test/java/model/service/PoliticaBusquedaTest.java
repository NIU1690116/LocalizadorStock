package model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.Alternativa;
import model.domain.AlternativaOtraTienda;
import model.domain.AlternativaReposicion;
import model.domain.Inventario;
import model.domain.Producto;
import model.domain.Reposicion;
import model.domain.Tienda;

class PoliticaBusquedaTest {
	private PoliticaBusqueda politica;
	private Tienda tienda;
	private Producto producto;

	@BeforeEach
	void setUp() {
		politica = new PoliticaBusqueda();
		tienda = new Tienda("T001", "Tienda Centro", 41.0, 2.0, new Inventario());
		producto = new Producto("P001", "Camiseta", "rojo", "camisetas", "mujer");
	}
	//El rechazo de opciones que superan los 120 minutos o los 10 km le corresponde
	// al evaluador, antes de aplicar la política

	@Test
	void testPoliticaBusquedaInvalida() {
		assertThrows(IllegalArgumentException.class,
				() -> politica.calcularPuntuacion(null));
	}
	@Test
    void testCalcularPuntuacion() {
		//Alternativa otra tienda, total esperado = 0.50
		// Tiempo = 60 (mitad de 120) -> 0.70 * 0.5 = 0.35
		// Distancia = 5.0 (mitad de 10.0) -> 0.30 * 0.5 = 0.15
		Alternativa alternativa = new AlternativaOtraTienda(tienda, 60, 5.0);
		assertEquals(0.50, politica.calcularPuntuacion(alternativa), 0.0001);
		
		//alternativa reposición, total esperado = 0.70
		// Tiempo = 120  -> 0.70 * 1.0 = 0.70
		// Distancia = 0.0 -> 0.30 * 0.0 = 0.0
		Reposicion reposicion = new Reposicion(
				producto, 2, LocalDateTime.of(2026, 11, 15, 10, 0));
		Alternativa alternativa_2 = new AlternativaReposicion(tienda, reposicion, 120, 0.0);
		assertEquals(0.70, politica.calcularPuntuacion(alternativa_2), 0.0001);
		
		// Tiempo = 0  -> 0.70 * 0 = 0.0
		// Distancia = 10.0 -> 0.30 * 1.0 = 3.0
		// Total esperado = 0.30
		Alternativa alternativa_3 = new AlternativaOtraTienda(tienda, 0, 10.0);
		assertEquals(0.30, politica.calcularPuntuacion(alternativa_3), 0.0001);
		
		// Tiempo = 0  -> 0.70 * 0 = 0.0
		// Distancia = 0.0 -> 0.30 * 0.0 = 0.0
		// Total esperado = 0.0
		Alternativa alternativa_4 = new AlternativaOtraTienda(tienda, 0, 0.0);
		assertEquals(0.0, politica.calcularPuntuacion(alternativa_4), 0.0001);

		// Tiempo = 120  -> 0.70 * 1.0 = 0.70
		// Distancia = 10.0 -> 0.30 * 1.0 = 0.30
		// Total esperado = 1.0
		Alternativa alternativa_5 = new AlternativaOtraTienda(tienda, 120, 10.0);
		assertEquals(1.0, politica.calcularPuntuacion(alternativa_5), 0.0001);
		
    }


	
}
