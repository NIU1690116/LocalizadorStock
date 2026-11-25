package model.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de construcción y validación de LineaPeticion
 * Comprueban las particiones válidas e inválidas de producto y cantidad, con
 * caja negra sobre los valores conservados y pruebas de límites para la cantidad.
 */
class LineaPeticionTest {
	
	private Producto producto;
	
	@BeforeEach
	void setUp() {
		 producto = new Producto("P001", "Camiseta", "Rojo", "camisetas", "mujer");
	}

	@Test
	void testCrearLineaPeticion() {
		LineaPeticion linea = new LineaPeticion(producto, 3);

		// Caja negra: una línea válida conserva la identidad del producto y la cantidad
        assertSame(producto, linea.getProducto());
        assertEquals(3, linea.getCantidad());
    }

	@Test
	void testCantidadValida() {
		// Prueba de límites: el mínimo positivo y el máximo entero son cantidades válidas
		LineaPeticion cantidadMinima = new LineaPeticion(producto, 1);
		LineaPeticion cantidadMaxima = new LineaPeticion(producto, Integer.MAX_VALUE);

		assertSame(producto, cantidadMinima.getProducto());
		assertEquals(1, cantidadMinima.getCantidad());
		assertSame(producto, cantidadMaxima.getProducto());
		assertEquals(Integer.MAX_VALUE, cantidadMaxima.getCantidad());
    }

	@Test
	void testCantidadNoValida() {
		// Particiones inválidas: cero y cantidad negativa incumplen la precondición (DbC).
		assertThrows(IllegalArgumentException.class, () -> new LineaPeticion(producto, 0));
		assertThrows(IllegalArgumentException.class, () -> new LineaPeticion(producto, -1));
    }

	@Test
	void testProductoNoEsValido() {
		// Partición inválida, DbC: el producto es obligatorio
        assertThrows(IllegalArgumentException.class, () -> new LineaPeticion(null, 2));
    }

}
