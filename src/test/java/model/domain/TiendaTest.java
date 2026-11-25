package model.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de construcción y consulta de Tienda
 * Aplican particiones de equivalencia para datos obligatorios y pruebas de
 * límites de coordenadas, además de verificar el acceso a los datos y la
 * delegación de la consulta de stock
 */
class TiendaTest {
	
	private InventarioMock inventarioMock;
	
	@BeforeEach
	public void setUp() {
		// mock sin mockito
		inventarioMock = new InventarioMock();
	}

	/**
	 * Comprueba que una tienda válida conserva su identificador, nombre, ubicación
	 * e inventario.
	 * Tipo: caja negra de construcción y lectura de atributos
	 */
	@Test
	void creacionTiendaValida() {
		Tienda tienda = new Tienda("T01", "Tienda Diagonal", 41.3851, 2.1734, inventarioMock);
		
		assertNotNull(tienda);
		assertEquals("T01", tienda.getId());
		assertEquals("Tienda Diagonal", tienda.getNombre());
		assertEquals(41.3851, tienda.getLatitud());
        assertEquals(2.1734, tienda.getLongitud());
        assertEquals(inventarioMock, tienda.getInventario());
	}
	/**
	 * Comprueba el rechazo de identificadores nulos, vacíos o en blanco.
	 * Tipo: particiones de equivalencia para identificadores inválidos.
	 */
	@Test
    void idInvalidoLanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> new Tienda(null, "Tienda", 0, 0, inventarioMock));
        assertThrows(IllegalArgumentException.class, () -> new Tienda("", "Tienda", 0, 0, inventarioMock));
        assertThrows(IllegalArgumentException.class, () -> new Tienda("   ", "Tienda", 0, 0, inventarioMock));
    }
	
	/**
	 * Comprueba el rechazo de nombres nulos, vacíos o en blanco.
	 * Tipo: particiones de equivalencia para nombres inválidos.
	 */
	@Test
	void nombreNuloOVacioLanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> new Tienda("T001", null, 0, 0, inventarioMock));
		assertThrows(IllegalArgumentException.class, () -> new Tienda("T001", "", 0, 0, inventarioMock));
		assertThrows(IllegalArgumentException.class, () -> new Tienda("T001", "   ", 0, 0, inventarioMock));
	}
	
	/**
	 * Comprueba los límites inferior y superior de latitud y los valores justo
	 * fuera del rango permitido.
	 * Tipo: pruebas de límites en -90 y 90 grados y particiones dentro/fuera de rango.
	 */
	@Test
    void testLatitudRangosPermitidos() {
		// Valores por debajo y por encima de los límites permitidos
        assertThrows(IllegalArgumentException.class, () -> new Tienda("T001", "Tienda", -90.1, 0, inventarioMock));
        assertThrows(IllegalArgumentException.class, () -> new Tienda("T001", "Tienda", 90.1, 0, inventarioMock));
        // Valores límite exactos (-90 y 90)
        assertDoesNotThrow(() -> new Tienda("T001", "Tienda Sur", -90.0, 0, inventarioMock));
        assertDoesNotThrow(() -> new Tienda("T001", "Tienda Norte", 90.0, 0, inventarioMock)); 
    }
	
	/**
	 * Comprueba los límites inferior y superior de longitud y valores justo fuera
	 * del rango permitido.
	 * Tipo: pruebas de límites en -180 y 180 grados y particiones dentro/fuera de rango.
	 */
	@Test
    void testLongitudRangosPermitidos() {
		// Valores por debajo y por encima de los límites permitidos
        assertThrows(IllegalArgumentException.class, () -> new Tienda("T001", "Tienda", 0, -180.1, inventarioMock));
        assertThrows(IllegalArgumentException.class, () -> new Tienda("T001", "Tienda", 0, 180.1, inventarioMock));
        // Valores límite exactos (-180 y 180)
        assertDoesNotThrow(() -> new Tienda("T001", "Tienda Oeste", 0, -180.0, inventarioMock));
        assertDoesNotThrow(() -> new Tienda("T001", "Tienda Este", 0, 180.0, inventarioMock));
    }

	/**
	 * Comprueba el rechazo de coordenadas no finitas, incluyendo NaN e infinitos.
	 * Tipo: particiones de equivalencia para valores numéricos no válidos.
	 */
	@Test
	void testLatitudLongitudNoFinita() {
		//latitud no finita
		assertThrows(IllegalArgumentException.class,
				() -> new Tienda("T001", "Tienda", Double.NaN, 0, inventarioMock));
		assertThrows(IllegalArgumentException.class,
				() -> new Tienda("T001", "Tienda", Double.POSITIVE_INFINITY, 0, inventarioMock));
		
		assertThrows(IllegalArgumentException.class,
				() -> new Tienda("T001", "Tienda", 0, Double.NaN, inventarioMock));
		assertThrows(IllegalArgumentException.class,
				() -> new Tienda("T001", "Tienda", 0, Double.NEGATIVE_INFINITY, inventarioMock));
	}
	
	/**
	 * Comprueba que no se puede construir una tienda sin inventario
	 * Tipo: partición de equivalencia de dependencia nula y contrato DbC del constructor.
	 */
	@Test
    void testInventarioNuloLanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> new Tienda("T001", "Tienda", 0, 0, null));
    }
	
	/**
	 * Comprueba que la tienda devuelve el stock informado por su inventario
	 * Tipo: caja negra de delegación entre tienda e inventario
	 */
	@Test 
	void testConsultarStockInventario() {
		Producto producto = new Producto("P001", "Camisa", "Azul", "M", "Ropa");
		inventarioMock.añadirProductos(producto, 12);
        Tienda tienda = new Tienda("T001", "Tienda Barcelona", 0, 0, inventarioMock);
        
        assertEquals(12, tienda.consultarStock(producto));
	}

}
