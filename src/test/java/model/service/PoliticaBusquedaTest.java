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
	void testPoliticaBusqueda() {
		assertThrows(IllegalArgumentException.class,
				() -> politica.calcularPuntuacion(null));
	}
}
