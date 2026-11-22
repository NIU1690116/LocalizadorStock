package model.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReposicionTest {
	private Producto producto;
    private LocalDateTime fechaPrevista; 
	
    @BeforeEach
	void setUp() {
		producto = new Producto("P001", "Camisa", "Rojo", "Camisa", "Unisex");
		fechaPrevista = LocalDateTime.of(2026, 10, 22, 11, 0);
	}
    
    @Test
    void testCreaReposicionValida() {
    	Reposicion reposicion = new Reposicion(producto, 1, fechaPrevista);
        assertEquals(producto.getCodigo(), reposicion.getProducto().getCodigo());
        assertEquals(1, reposicion.getCantidad());
        assertEquals(fechaPrevista, reposicion.getFechaPrevista());
    }

	@Test
	void testCantidadValidad() {
		Reposicion minima = new Reposicion(producto, 1, fechaPrevista);
		Reposicion maxima = new Reposicion(producto, Integer.MAX_VALUE, fechaPrevista);

		assertEquals(1, minima.getCantidad());
		assertEquals(Integer.MAX_VALUE, maxima.getCantidad());
	}
    
    @Test
    void testRechazaProductoFechaCantidadInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(null, 1, fechaPrevista));
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(producto, 0, fechaPrevista));
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(producto, -1, fechaPrevista));
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(producto, Integer.MIN_VALUE, fechaPrevista));
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(producto, 1, null));
    }

}
