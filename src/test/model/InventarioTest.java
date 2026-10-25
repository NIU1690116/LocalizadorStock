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
        
        inventario.añadirProductos(producto, 1);
        assertEquals(16, inventario.consultarStock(producto));
        // solo se permiten cantidades positivas mayores a 0
        assertThrows(IllegalArgumentException.class, () -> inventario.añadirProductos(producto, 0));
        assertThrows(IllegalArgumentException.class, () -> inventario.añadirProductos(producto, -1));
    }
    
    @Test
    void testHaySuficientesUnidades() {
        inventario.añadirProductos(producto, 10);

        assertTrue(inventario.hayStockSuficiente(producto, 5));//tiene suficientes unidades
        assertFalse(inventario.hayStockSuficiente(producto, 15));//no tiene suficientes unidades
        assertTrue(inventario.hayStockSuficiente(producto, 10)); // cantidad exacta
    }

    @Test
    void retirarStockReduceLaCantidadDisponible() {
        inventario.añadirProductos(producto, 10);

        inventario.retirarStock(producto, 4);

        assertEquals(6, inventario.consultarStock(producto));
    }

    @Test
    void retirarMasUnidadesDeLasDisponiblesLanzaExcepcion() {
        inventario.añadirProductos(producto, 3);

        assertThrows(IllegalArgumentException.class, () -> inventario.retirarStock(producto, 5));

        // El stock debe permanecer intacto tras el error.
        assertEquals(3, inventario.consultarStock(producto));
    }

    @Test
    void agregarCantidadNoPermitidaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> inventario.añadirProductos(producto, 0));
        assertThrows(IllegalArgumentException.class, () -> inventario.añadirProductos(producto, -5));
    }

    @Test
    void consultarStockDeProductoNoRegistrado() {
        Producto productoNoRegistrado = new Producto("P002", "Pantalón", "Negro", "Adulto", "Ropa");

        assertEquals(0, inventario.consultarStock(productoNoRegistrado));
    }
    
    @Test
    void establecerStockMaximoValidoYConsultarlo() {
        inventario.setStockMaximo(producto, 50);
        assertEquals(50, inventario.consultarStockMaximo(producto));
    }
    
    @Test
    void establecerStockMaximoNegativoLanzaExcepcion() {
        // Valor inválido 
        assertThrows(IllegalArgumentException.class, () -> inventario.setStockMaximo(producto, -5));
    }
    
    @Test
    void añadirProductosDentroDelStockMaximoPermiteOperacion() {
        inventario.setStockMaximo(producto, 20);
        
        inventario.añadirProductos(producto, 10); // Añadir por debajo del máximo
        assertEquals(10, inventario.consultarStock(producto));

        inventario.añadirProductos(producto, 10);// llegar exactamente al máximo 10 + 10 = 20
        assertEquals(20, inventario.consultarStock(producto));
    }

}
