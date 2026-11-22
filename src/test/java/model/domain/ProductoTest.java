package model.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductoTest {
	
	@BeforeEach
	public void setup() {
		
	}

	@Test
    void dosProductosConElMismoCodigoSonIguales() {
        Producto a = new Producto("P001", "Camiseta", "rojo", "camisetas", "hombre");
        Producto b = new Producto("P001", "Camiseta básica", "azul", "camisetas", "hombre");
        
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
	
	
	@Test
    void exponeSusAtributos() {
        Producto p = new Producto("P001", "Camiseta", "rojo", "camisetas", "mujer");
 
        assertEquals("P001", p.getCodigo());
        assertEquals("Camiseta", p.getNombre());
        assertEquals("rojo", p.getColor());
        assertEquals("camisetas", p.getFamilia());
        assertEquals("mujer", p.getGenero());
    }

	@Test
	void testSettersNoCambian() {
		Producto producto = new Producto("P001", "Camiseta", "rojo", "camisetas", "mujer");

		producto.setNombre("");
		producto.setColor(null);
		producto.setFamilia(" ");
		producto.setGenero("unisex");

		assertEquals("P001", producto.getCodigo());
		assertEquals("", producto.getNombre());
		assertNull(producto.getColor());
		assertEquals(" ", producto.getFamilia());
		assertEquals("unisex", producto.getGenero());

		producto.setNombre(null);
		producto.setColor("");
		producto.setFamilia(null);
		producto.setGenero("");

		assertEquals("P001", producto.getCodigo());
		assertNull(producto.getNombre());
		assertEquals("", producto.getColor());
		assertNull(producto.getFamilia());
		assertEquals("", producto.getGenero());
	}
	
	@Test
    void elCodigoNoPuedeSerNuloNiVacio() {
        assertThrows(IllegalArgumentException.class, () -> new Producto(null, "Camiseta", "rojo", "camisetas", "unisex"));
        assertThrows(IllegalArgumentException.class, () -> new Producto("", "Camiseta", "rojo", "camisetas", "unisex"));
        assertThrows(IllegalArgumentException.class, () -> new Producto("  ", "Camiseta", "rojo", "camisetas", "unisex"));
        assertThrows(IllegalArgumentException.class, () -> new Producto("\t", "Camiseta", "rojo", "camisetas", "unisex"));

        Producto codigoMinimoValido = new Producto("P", null, null, null, null);
        assertEquals("P", codigoMinimoValido.getCodigo());
        assertNull(codigoMinimoValido.getNombre());
        assertNull(codigoMinimoValido.getColor());
        assertNull(codigoMinimoValido.getFamilia());
        assertNull(codigoMinimoValido.getGenero());
    }
	
	@Test
	void productosConCodigosDistintosNoSonIguales() {
		Producto a = new Producto("P001", "Camiseta", "rojo", "camisetas", "hombre");
		Producto b = new Producto("P002", "Camiseta", "rojo", "camisetas", "hombre");

		assertNotEquals(a, b);
	}
}
