package test.java.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.model.Inventario;
import main.java.model.Producto;

class InventarioTest {
	
    private Producto producto;
    private Inventario inventario;
	
	@BeforeEach
	public void setUp() {
		producto = new Producto("P001", "Camiseta", "rojo", "camisetas", "unisex");
        inventario = new Inventario();
	}

    @Test
    void añadirProductosAcumulaElStock() {
        inventario.añadirProductos(producto, 10);
        inventario.añadirProductos(producto, 5);

        assertEquals(15, inventario.consultarStock(producto));
    }
    
    @Test
    void tieneStockHaySuficientesUnidades() {
        inventario.añadirProductos(producto, 10);

        assertTrue(inventario.tieneStock(producto, 5));//tiene suficientes unidades
        assertFalse(inventario.tieneStock(producto, 15));//no tiene suficientes unidades
    }

    @Test
    void retirarStockReduceLaCantidadDisponible() {
        inventario.añadirProductos(producto, 10);

        inventario.retirarStock(producto, 4);

        assertEquals(6, inventario.consultarStock(producto));
    }



}
