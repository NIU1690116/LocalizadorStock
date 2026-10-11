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
	
	void añadirProductosActualizaElStock() {
        inventario.añadirProductos(producto, 10);

        assertEquals(10, inventario.consultarStock(producto));
    }

    @Test
    void añadirProductosDoVecesAcumulaElStock() {
        inventario.añadirProductos(producto, 10);
        inventario.añadirProductos(producto, 5);

        assertEquals(15, inventario.consultarStock(producto));
    }
    
    void tieneStockTestDevuelveTrueSiHayCantidadSuficiente() {
        inventario.añadirProductos(camiseta, 5); 

        assertTrue(inventario.tieneStock(camiseta, 3));
        assertTrue(inventario.tieneStock(camiseta, 5));
        assertFalse(inventario.tieneStock(camiseta, 6));
    }



}
