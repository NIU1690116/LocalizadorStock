package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.LineaPeticion;
import model.Producto;

class LineaPeticionTest {
	
	private Producto producto;
	
	@BeforeEach
	void setUp() {
		 producto = new Producto("P001", "Camiseta", "Rojo", "camisetas", "mujer");
	}

	@Test
	void testCrearLineaPeticion() {
		LineaPeticion linea = new LineaPeticion(producto, 3);

        assertEquals(producto.getCodigo(), linea.getProducto().getCodigo());
        assertEquals(3, linea.getCantidad());
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
