package model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.Alternativa;
import model.domain.AlternativaOtraTienda;
import model.domain.AlternativaReposicion;
import model.domain.Inventario;
import model.domain.Producto;
import model.domain.Reposicion;
import model.domain.RestriccionesBusqueda;
import model.domain.Tienda;

class EvaluadorAlternativasTest {
	private EvaluadorAlternativas evaluador;
	private Tienda tienda;
	private Reposicion reposicion;

	@BeforeEach
	void setUp() {
		evaluador = new EvaluadorAlternativas(new PoliticaBusqueda(), new RestriccionesBusqueda());
		tienda = new Tienda("T001", "Tienda Centro", 41.0, 2.0, new Inventario());
		Producto producto = new Producto("P001", "Camiseta", "rojo", "camisetas", "mujer");
		reposicion = new Reposicion(producto, 2, LocalDateTime.of(2026, 11, 15, 10, 0));
	}
	
	@Test
	void testPoliticaYRestriccionesValidas() {
		assertThrows(IllegalArgumentException.class,
				() -> new EvaluadorAlternativas(null, new RestriccionesBusqueda()));
		assertThrows(IllegalArgumentException.class,
				() -> new EvaluadorAlternativas(new PoliticaBusqueda(), null));
	}
	
	@Test
	void tesListaAlternativaValida() {
		assertThrows(IllegalArgumentException.class,//lista nula
				() -> evaluador.seleccionarMejor(null));
		assertNull(evaluador.seleccionarMejor(List.of()));//null si no hay alternativas(lista vacia)
	}
	
	@Test
	void testCumpleRestricciones() {
		Alternativa espera = new AlternativaReposicion(tienda, reposicion, 120, 0.0);

		assertEquals(espera, evaluador.seleccionarMejor(List.of(espera)));
	}
	
	

}
