package model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.AlternativaProductoSimilar;
import model.domain.Inventario;
import model.domain.LineaPeticion;
import model.domain.Producto;
import model.domain.Tienda;

class BuscadorProductosSimilaresTest {
	//se utilizara mockito
	private BuscadorProductosSimilares buscador;
	private Producto productoBase, producto3de3, producto2de3, producto1de3, producto0de3;

	@BeforeEach
	void setUp() {
		buscador = new BuscadorProductosSimilares();
		// Producto de referencia con el que se comparará
		productoBase = new Producto("P001", "Camiseta", "rojo", "camisetas", "mujer");

		// Productos candidatos con su grado de similitud explícito 
		producto3de3 = new Producto("P002", "Camiseta", "rojo", "camisetas", "mujer"); // Coinciden las 3: color, familia, género
		producto2de3 = new Producto("P003", "Camiseta", "azul", "camisetas", "mujer"); // Coinciden 2: familia y género, cambia color
		producto1de3 = new Producto("P004", "Camiseta", "azul", "pantalones", "mujer"); // Coincide 1: sólo género
		producto0de3 = new Producto("P005", "Pantalon",  "negro", "pantalones", "hombre"); // Coinciden 0
	}


	@Test
	void testArgumentosObligatoriosNulos() {
		assertThrows(
	            IllegalArgumentException.class,
	            () -> buscador.buscarSimilares((Producto) null, List.of()));
		assertThrows(
	            IllegalArgumentException.class,
	            () -> buscador.buscarSimilares(productoBase, null));
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
		
		// Ejemplo con 1 de 3 (33.33%)
        double similitud1de3 = buscador.calcularSimilitud(productoBase, producto1de3);
        assertEquals(33.33, similitud1de3, 0.01);
        
        // Ejemplo con 2 de 3 (66.67%)
        double similitud2de3 = buscador.calcularSimilitud(productoBase, producto2de3);
        assertEquals(66.67, similitud2de3, 0.01);
        
        // Ejemplo con 3 de 3 (100.0%)
        double similitud3de3 = buscador.calcularSimilitud(productoBase, producto3de3);
        assertEquals(100.0, similitud3de3, 0.01);
        
        // Ejemplo con 0 de 3 (0.0%)
        double similitud0de3 = buscador.calcularSimilitud(productoBase, producto0de3);
        assertEquals(0.0, similitud0de3, 0.01);
	    
    }
	
	@Test
	void buscaProductosSimilaresConStockSuficienteEnLaTienda() {
		Tienda tienda = mock(Tienda.class);
		Inventario inventario = mock(Inventario.class);
		when(tienda.getInventario()).thenReturn(inventario);
		when(inventario.productosConStockSuficiente(2)).thenReturn(List.of(
				producto3de3,
				producto2de3,
				producto1de3,
				producto0de3));

		List<AlternativaProductoSimilar> alternativas = buscador.buscarSimilares(
				new LineaPeticion(productoBase, 2), List.of(tienda));

		assertNotNull(alternativas);
		assertEquals(3, alternativas.size());
		//orden de mayor a menor porcentaje de similitud
		assertTrue(alternativas.stream().anyMatch(a ->
				a.productoSimilar().equals(producto3de3) && a.gradoSimilitud() == 100.0));
		assertTrue(alternativas.stream().anyMatch(a ->
				a.productoSimilar().equals(producto2de3)
						&& Math.abs(a.gradoSimilitud() - 66.67) < 0.01));
		assertTrue(alternativas.stream().anyMatch(a ->
				a.productoSimilar().equals(producto1de3)
						&& Math.abs(a.gradoSimilitud() - 33.33) < 0.01));
	}

}
