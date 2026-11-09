package model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.AlternativaProductoSimilar;
import model.domain.Inventario;
import model.domain.LineaPeticion;
import model.domain.Producto;
import model.domain.Tienda;

class BuscadorProductosSimilaresTest {
	private BuscadorProductosSimilares buscador;
	private Producto solicitado;

	@BeforeEach
	void setUp() {
		buscador = new BuscadorProductosSimilares();
		solicitado = new Producto("P001", "Camiseta", "rojo", "camisetas", "mujer");
	}


	@Test
	void testArgumentosObligatoriosNulos() {
		assertThrows(
	            IllegalArgumentException.class,
	            () -> buscador.buscarSimilares((Producto) null, List.of()));
		assertThrows(
	            IllegalArgumentException.class,
	            () -> buscador.buscarSimilares(solicitado, null));
		assertThrows(
	            IllegalArgumentException.class,
	            () -> buscador.buscarSimilares((LineaPeticion) null, List.of()));
	}
	
	@Test
	void testNormalizar() {
	    assertEquals("camisa", buscador.normalizar("  CAMISA  "));
	    assertNull(buscador.normalizar(null));
	    assertNull(buscador.normalizar("   "));
    }
	
	@Test
	void testCoincide() {
		assertTrue(buscador.coincide("  Camisa ", "camisa"));
    }
	

}
