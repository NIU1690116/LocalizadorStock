package test.java.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import main.java.model.Producto;
import main.java.model.Reposicion;

class ReposicionTest {
    private Producto producto = new Producto("P001", "Camisa", "Rojo", "Camisa", "Unisex");
    private LocalDateTime prevista = LocalDateTime.of(2026, 10, 22, 11, 0);

    @Test
    void testCreaReposicionValida() {
    	Reposicion reposicion = new Reposicion(producto, 1, prevista);
        assertEquals(producto.getCodigo(), reposicion.getProducto().getCodigo());
        assertEquals(1, reposicion.getCantidad());
        assertEquals(prevista, reposicion.getFechaPrevista());
    }
    
    @Test
    void rechazaProductoFechaCantidadInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(null, 1, prevista));
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(producto, 0, prevista));
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(producto, -1, prevista));
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(producto, 1, null));
    }

}
