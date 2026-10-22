package test.java.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.model.LineaPeticion;
import main.java.model.Producto;

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
    void testCantidadValida() {
		assertThrows(IllegalArgumentException.class, () -> new LineaPeticion(producto, 0));
		assertThrows(IllegalArgumentException.class, () -> new LineaPeticion(producto, -1));
    }

}
