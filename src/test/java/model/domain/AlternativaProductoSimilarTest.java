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
		producto = new Producto("P01", "Chaqueta", "azul", "chaquetas", "mujer");
		tienda = new Tienda("T1", "Tienda calle principal", 41.0, 2.0, new Inventario());
		
	}

	@Test
	void testAlternativaProductoSimilarConstructor() {
		assertThrows(IllegalArgumentException.class, () -> {
            new AlternativaProductoSimilar(null, tienda, 50.0);
        });
		assertThrows(IllegalArgumentException.class, () -> {
            new AlternativaProductoSimilar(producto, null, 50.0);
        });
	}
	
	@Test
	void testPorcentajeSimilitudValido() {
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, 0.0));
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, -1.0));//particion negativa
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, 100.01));
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, 150.01));//particion alta
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, Double.NaN));
		assertThrows(IllegalArgumentException.class,
				() -> new AlternativaProductoSimilar(producto, tienda, Double.POSITIVE_INFINITY));

		assertEquals(100.0,
				new AlternativaProductoSimilar(producto, tienda, 100.0).gradoSimilitud());
	}
	


}
