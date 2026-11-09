package model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
	private Producto solicitado, solicitado2;

	@BeforeEach
	void setUp() {
		buscador = new BuscadorProductosSimilares();
		solicitado = new Producto("P001", "Camiseta", "rojo", "camisetas", "mujer");
		solicitado2 = new Producto("P002", "Camiseta", "azul", "camisetas", "mujer");
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
		assertFalse(buscador.coincide("Camisa", "Pantalon"));
    }
	
	@Test
	void testCalcularSimilitud() {
		BuscadorProductosSimilares buscador = new BuscadorProductosSimilares();
	    double similitud = buscador.calcularSimilitud(solicitado, solicitado2);
	    
	    assertEquals(66.66666666666667, similitud, 0.0001);
    }
	

}
