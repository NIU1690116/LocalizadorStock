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

import model.distance.CalculadorDistancia;
import model.distance.EstimadorTiempoViaje;
import model.domain.AlternativaProductoSimilar;
import model.domain.Inventario;
import model.domain.LineaPeticion;
import model.domain.Producto;
import model.domain.Tienda;

class BuscadorProductosSimilaresTest {
	//se utilizara mockito
	private BuscadorProductosSimilares buscador;
	private Producto productoBase, producto3de3, producto2de3, producto1de3, producto0de3;
	private CalculadorDistancia calculadorDistancia;
	private EstimadorTiempoViaje estimadorTiempoViaje;
	private Tienda origen;

	@BeforeEach
	void setUp() {
		calculadorDistancia = mock(CalculadorDistancia.class);
		estimadorTiempoViaje = mock(EstimadorTiempoViaje.class);
		buscador = new BuscadorProductosSimilares(calculadorDistancia, estimadorTiempoViaje);
		origen = mock(Tienda.class);
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
		assertThrows(IllegalArgumentException.class,
				() -> new BuscadorProductosSimilares(null, estimadorTiempoViaje));
		assertThrows(IllegalArgumentException.class,
				() -> new BuscadorProductosSimilares(calculadorDistancia, null));
		assertThrows(
	            IllegalArgumentException.class,
			() -> buscador.buscarSimilares((Producto) null, origen, List.of()));
		assertThrows(
	            IllegalArgumentException.class,
	            () -> buscador.buscarSimilares(productoBase, origen, null));
		assertThrows(
	            IllegalArgumentException.class,
	            () -> buscador.buscarSimilares((LineaPeticion) null, origen, List.of()));
		assertThrows(
				IllegalArgumentException.class,
				() -> buscador.buscarSimilares(productoBase, null, List.of()));
	}
	
	@Test
	void testNormalizar() {
		// Path coverage: recorrido para null, texto en blanco y texto válido
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
		BuscadorProductosSimilares buscador = new BuscadorProductosSimilares(
				mock(CalculadorDistancia.class), mock(EstimadorTiempoViaje.class));
		
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
		// Loop testing: una tienda y varios productos candidatos.
		Tienda tienda = mock(Tienda.class);
		Inventario inventario = mock(Inventario.class);
		when(tienda.getInventario()).thenReturn(inventario);
		when(inventario.productosConStockSuficiente(2)).thenReturn(List.of(
				producto3de3,
				producto2de3,
				producto1de3,
				producto0de3));

		when(calculadorDistancia.calcularKm(origen, tienda)).thenReturn(3.5);
		when(estimadorTiempoViaje.estimarMinutos(origen, tienda)).thenReturn(12);
		List<AlternativaProductoSimilar> alternativas = buscador.buscarSimilares(
				new LineaPeticion(productoBase, 2), origen, List.of(tienda));

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
		assertEquals(12, alternativas.get(0).getTiempoEstimadoMinutos());
		assertEquals(3.5, alternativas.get(0).getDistanciaKm());
	}

	@Test
	void buscarSimilaresConCeroTiendasDevuelveListaVacia() {
		// Loop testing: cero iteraciones del bucle exterior
		assertTrue(buscador.buscarSimilares(productoBase, origen, List.of()).isEmpty());
	}

}
