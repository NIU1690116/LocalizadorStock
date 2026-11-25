package model.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de operaciones de stock, máximos configurados y consistencia de mapas
 * Usa caja negra para operaciones públicas, particiones y límites para cantidades
 * y caja blanca para validar directamente los estados internos del inventario
 */
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
        // Caja negra: varias adiciones se acumulan en el stock existente
        inventario.añadirProductos(producto, 10);
        inventario.añadirProductos(producto, 5);

        assertEquals(15, inventario.consultarStock(producto));
        
        inventario.añadirProductos(producto, 1);
        assertEquals(16, inventario.consultarStock(producto));
        // Particiones inválidas, DbC: cero y una cantidad negativa no se admiten
        assertThrows(IllegalArgumentException.class, () -> inventario.añadirProductos(producto, 0));
        assertThrows(IllegalArgumentException.class, () -> inventario.añadirProductos(producto, -1));
    }
    
    @Test
    void testHaySuficientesUnidades() {
        inventario.añadirProductos(producto, 10);

        // Particiones de equivalencia: cantidad menor, mayor y exactamente igual al stock
        assertTrue(inventario.hayStockSuficiente(producto, 5));//tiene suficientes unidades
        assertFalse(inventario.hayStockSuficiente(producto, 15));//no tiene suficientes unidades
        assertTrue(inventario.hayStockSuficiente(producto, 10)); // cantidad exacta
    }

    @Test
    void retirarStockReduceLaCantidadDisponible() {
        // Caja negra: retirar una cantidad válida reduce el stock en esa cantidad
        inventario.añadirProductos(producto, 10);

        inventario.retirarStock(producto, 4);

        assertEquals(6, inventario.consultarStock(producto));
    }

    @Test
    void retirarMasUnidadesDeLasDisponiblesLanzaExcepcion() {
        // DbC: retirar más que el stock incumple la precondición de disponibilidad.
        inventario.añadirProductos(producto, 3);

        assertThrows(IllegalArgumentException.class, () -> inventario.retirarStock(producto, 5));

        // El stock debe permanecer intacto tras el error.
        assertEquals(3, inventario.consultarStock(producto));
    }

    @Test
    void retirarCantidadNoPositivaLanzaExcepcion() {
        // Caja blanca, límite inválido: cero cubre la rama cantidad <= 0
        assertThrows(IllegalArgumentException.class, () -> inventario.retirarStock(producto, 0));
    }

    @Test
    void consultarStockSuficienteConCantidadNegativaLanzaExcepcion() {
        // Partición inválida, DbC: el mínimo fuera de rango es una cantidad negativa.
        assertThrows(IllegalArgumentException.class, () -> inventario.hayStockSuficiente(producto, -1));
    }

    @Test
    void consultarStockConProductoNuloLanzaExcepcion() {
        // Caja blanca, partición inválida: cubre el rechazo del producto nulo
        assertThrows(IllegalArgumentException.class, () -> inventario.consultarStock(null));
    }

    @Test
    void agregarCantidadNoPermitidaLanzaExcepcion() {
        // Particiones inválidas de la cantidad añadida: cero y negativa
        assertThrows(IllegalArgumentException.class, () -> inventario.añadirProductos(producto, 0));
        assertThrows(IllegalArgumentException.class, () -> inventario.añadirProductos(producto, -5));
    }

    @Test
    void consultarStockDeProductoNoRegistrado() {
        Producto productoNoRegistrado = new Producto("P002", "Pantalón", "Negro", "Adulto", "Ropa");

        // Partición válida sin registro: la consulta informa stock cero
        assertEquals(0, inventario.consultarStock(productoNoRegistrado));
    }
    
    @Test
    void establecerStockMaximoValidoYConsultarlo() {
        // Caja negra: un máximo no negativo se almacena y se puede consultar
        inventario.setStockMaximo(producto, 50);
        assertEquals(50, inventario.consultarStockMaximo(producto));
    }

    @Test
    void testStockDentroDelLimite() {
        // Caja negra: se configura un máximo de cinco y se parte de stock cero
        inventario.setStockMaximo(producto, 5);
        assertEquals(0, inventario.consultarStock(producto));

        inventario.añadirProductos(producto, 3);
        assertEquals(3, inventario.consultarStock(producto));

        inventario.retirarStock(producto, 3);
        assertEquals(0, inventario.consultarStock(producto));

        // Límite superior exacto: se permite llegar al máximo configurado
        inventario.añadirProductos(producto, 5);
        assertEquals(5, inventario.consultarStock(producto));

        // Límite fuera de rango: una unidad adicional se rechaza sin cambiar el stock
        assertThrows(IllegalArgumentException.class,
                () -> inventario.añadirProductos(producto, 1));
        assertEquals(5, inventario.consultarStock(producto));
        assertEquals(5, inventario.consultarStockMaximo(producto));
    }
    
    @Test
    void establecerStockMaximoNegativoLanzaExcepcion() {
        // Partición inválida, DbC: no se permite configurar un máximo negativo
        assertThrows(IllegalArgumentException.class, () -> inventario.setStockMaximo(producto, -5));
    }
    
    @Test
    void añadirProductosDentroDelStockMaximoPermiteOperacion() {
        inventario.setStockMaximo(producto, 20);
        
        // Caso interior: se permite añadir stock por debajo del máximo
        inventario.añadirProductos(producto, 10); // Añadir por debajo del máximo
        assertEquals(10, inventario.consultarStock(producto));

        // Límite superior exacto: se alcanza el máximo (10 + 10 = 20)
        inventario.añadirProductos(producto, 10);
        assertEquals(20, inventario.consultarStock(producto));
    }

    @Test
    void testRestriccionesYLimitesDeStockMaximo() {
        // Límite máximo cero: una adición positiva debe rechazarse
        inventario.setStockMaximo(producto, 0);
        assertThrows(IllegalArgumentException.class, () -> inventario.añadirProductos(producto, 1));
        assertEquals(0, inventario.consultarStock(producto));

        // Partición inválida: no se permite fijar un máximo menor que el stock actual
        Inventario inventarioConStock = new Inventario();
        inventarioConStock.añadirProductos(producto, 5);
        assertThrows(IllegalArgumentException.class, () -> inventarioConStock.setStockMaximo(producto, 4));
        assertEquals(0, inventarioConStock.consultarStockMaximo(producto));
    }
    
    @Test
    void testDesbordeStock() {
        // Límite superior representable: Integer.MAX_VALUE se acepta como stock inicial
        inventario.añadirProductos(producto, Integer.MAX_VALUE);

        // Desbordamiento: sumar una unidad más se rechaza y conserva el stock
        assertThrows(IllegalArgumentException.class, () -> inventario.añadirProductos(producto, 1));
        assertEquals(Integer.MAX_VALUE, inventario.consultarStock(producto));
    }

	@Test
	void testProductosConStockSuficiente() {
		Producto productoConStock = new Producto("P002", "Chaqueta", "azul", "chaquetas", "unisex");
		inventario.añadirProductos(producto, 3);
		inventario.añadirProductos(productoConStock, 1);

		// Particiones de stock: devuelve el producto con al menos dos unidades y omite el que tiene una
		List<Producto> disponibles = inventario.productosConStockSuficiente(2);
		assertEquals(List.of(producto), disponibles);
		// Particiones inválidas, límites: el mínimo debe ser positivo
		assertThrows(IllegalArgumentException.class, () -> inventario.productosConStockSuficiente(0));
		assertThrows(IllegalArgumentException.class, () -> inventario.productosConStockSuficiente(-1));
		
	}

	@Test
	void validaEstadosInternosDelInventario() {
		// Caja blanca: estado vacío válido y particiones de mapa nulo
		Map<Producto, Integer> stockValido = new HashMap<>();
		Map<Producto, Integer> maximosValidos = new HashMap<>();
		assertTrue(Inventario.estadoInternoValido(stockValido, maximosValidos));
		assertFalse(Inventario.estadoInternoValido(null, maximosValidos));
		assertFalse(Inventario.estadoInternoValido(stockValido, null));
		
		// Caja blanca: se rechazan claves de producto nulas
		Map<Producto, Integer> stockClaveNula = new HashMap<>();
		stockClaveNula.put(null, 1);
		assertFalse(Inventario.estadoInternoValido(stockClaveNula, maximosValidos));

		// Caja blanca: se rechazan valores de stock nulos y negativos
		Map<Producto, Integer> stockCantidadNula = new HashMap<>();
		stockCantidadNula.put(producto, null);
		assertFalse(Inventario.estadoInternoValido(stockCantidadNula, maximosValidos));

		Map<Producto, Integer> stockNegativo = new HashMap<>();
		stockNegativo.put(producto, -1);
		assertFalse(Inventario.estadoInternoValido(stockNegativo, maximosValidos));
		
		// Invariante, DbC: el stock no puede superar el máximo 
		Map<Producto, Integer> stockSobreMaximo = new HashMap<>();
		stockSobreMaximo.put(producto, 2);
		Map<Producto, Integer> maximoConfigurado = new HashMap<>();
		maximoConfigurado.put(producto, 1);
		assertFalse(Inventario.estadoInternoValido(stockSobreMaximo, maximoConfigurado));
		
		// Caja blanca: las claves y valores del mapa de máximos también deben ser válidos
		Map<Producto, Integer> maximoClaveNula = new HashMap<>();
		maximoClaveNula.put(null, 1);
		assertFalse(Inventario.estadoInternoValido(stockValido, maximoClaveNula));

		Map<Producto, Integer> maximoValorNulo = new HashMap<>();
		maximoValorNulo.put(producto, null);
		assertFalse(Inventario.estadoInternoValido(stockValido, maximoValorNulo));

		Map<Producto, Integer> maximoNegativo = new HashMap<>();
		maximoNegativo.put(producto, -1);
		assertFalse(Inventario.estadoInternoValido(stockValido, maximoNegativo));
	}
}
