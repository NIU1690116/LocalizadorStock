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

	@BeforeEach
	void setUp() {
		politica = new PoliticaBusqueda();
		tienda = new Tienda("T001", "Tienda Centro", 41.0, 2.0, new Inventario());
	}

	@Test
	void testPoliticaBusquedaInvalida() {
		assertThrows(IllegalArgumentException.class,
				() -> politica.calcularPuntuacion(null));
	}
	@Test
    void testCalculaLaPuntuacionNormalizada() {
		// Tiempo = 60 (mitad de 120) -> 0.70 * 0.5 = 0.35
		// Distancia = 5.0 (mitad de 10.0) -> 0.30 * 0.5 = 0.15
		// Total esperado = 0.50
		Alternativa alternativa = new AlternativaOtraTienda(tienda, 60, 5.0);
		assertEquals(0.50, politica.calcularPuntuacion(alternativa), 0.0001);
    }
	
	
}
