package model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Producto;
import model.Reposicion;

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
    void rechazaProductoFechaCantidadInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(null, 1, fechaPrevista));
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(producto, 0, fechaPrevista));
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(producto, -1, fechaPrevista));
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(producto, 1, null));
    }

}
