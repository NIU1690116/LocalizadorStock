package test.java.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.model.Inventario;
import main.java.model.Tienda;

class TiendaTest {
	
	private Inventario inventarioMock;
	
	@BeforeEach
	public void setUp() {
		inventarioMock = new Inventario();
		
	}

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
	@Test
    void idInvalidoLanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> new Tienda(null, "Tienda", 0, 0, inventarioMock));
        assertThrows(IllegalArgumentException.class, () -> new Tienda("", "Tienda", 0, 0, inventarioMock));
        assertThrows(IllegalArgumentException.class, () -> new Tienda("   ", "Tienda", 0, 0, inventarioMock));
    }
	
	@Test
	void nombreNuloOVacioLanzaExcepcion() {
		assertThrows(IllegalArgumentException.class, () -> new Tienda("T001", null, 0, 0, inventarioMock));
		assertThrows(IllegalArgumentException.class, () -> new Tienda("T001", "", 0, 0, inventarioMock));
		assertThrows(IllegalArgumentException.class, () -> new Tienda("T001", "   ", 0, 0, inventarioMock));
	}
	
	@Test
    void testLatitudRangosPermitidos() {
		// Valores por debajo y por encima de los límites permitidos
        assertThrows(IllegalArgumentException.class, () -> new Tienda("T001", "Tienda", -90.1, 0, inventarioMock));
        assertThrows(IllegalArgumentException.class, () -> new Tienda("T001", "Tienda", 90.1, 0, inventarioMock));
        // Valores límite exactos (-90 y 90)
        assertDoesNotThrow(() -> new Tienda("T001", "Tienda Sur", -90.0, 0, inventarioMock));
        assertDoesNotThrow(() -> new Tienda("T001", "Tienda Norte", 90.0, 0, inventarioMock)); 
    }
	
	@Test
    void testLongitudRangosPermitidos() {
		// Valores por debajo y por encima de los límites permitidos
        assertThrows(IllegalArgumentException.class, () -> new Tienda("T001", "Tienda", 0, -180.1, inventarioMock));
        assertThrows(IllegalArgumentException.class, () -> new Tienda("T001", "Tienda", 0, 180.1, inventarioMock));
        // Valores límite exactos (-180 y 180)
        assertDoesNotThrow(() -> new Tienda("T001", "Tienda Oeste", 0, -180.0, inventarioMock));
        assertDoesNotThrow(() -> new Tienda("T001", "Tienda Este", 0, 180.0, inventarioMock));
    }
	
	@Test
    void inventarioNuloLanzaExcepcion() {
        
    }
	
	
	
}
