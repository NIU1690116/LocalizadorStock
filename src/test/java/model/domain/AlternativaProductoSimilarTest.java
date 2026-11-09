package model.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlternativaProductoSimilarTest {
	private Producto producto;
    private Tienda tienda;
    private Inventario inventario;
    
	@BeforeEach
	void setUp() throws Exception {
		producto = new Producto("P01", "Chaqueta", "azul", "chaquetas", "unisex");
		inventario.añadirProductos(producto, 4);
		tienda = new Tienda("T1", "Tienda calle principal", 41.0, 2.0, inventario);
		
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
