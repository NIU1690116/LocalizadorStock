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

    }

}
