package model.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineaPeticionTest {
	
	private Producto producto;
	
	@BeforeEach
	void setUp() {
		 producto = new Producto("P001", "Camiseta", "Rojo", "camisetas", "mujer");
	}

	@Test
	void testCrearLineaPeticion() {
		LineaPeticion linea = new LineaPeticion(producto, 3);

        assertSame(producto, linea.getProducto());
        assertEquals(3, linea.getCantidad());
    }

	@Test
	void testCantidadValida() {
		LineaPeticion cantidadMinima = new LineaPeticion(producto, 1);
		LineaPeticion cantidadMaxima = new LineaPeticion(producto, Integer.MAX_VALUE);

		assertSame(producto, cantidadMinima.getProducto());
		assertEquals(1, cantidadMinima.getCantidad());
		assertSame(producto, cantidadMaxima.getProducto());
		assertEquals(Integer.MAX_VALUE, cantidadMaxima.getCantidad());
    }

	@Test
	void testCantidadNoValida() {
		assertThrows(IllegalArgumentException.class, () -> new LineaPeticion(producto, 0));
		assertThrows(IllegalArgumentException.class, () -> new LineaPeticion(producto, -1));
    }

	@Test
	void testProductoNoEsValido() {
        assertThrows(IllegalArgumentException.class, () -> new LineaPeticion(null, 2));
    }

}
