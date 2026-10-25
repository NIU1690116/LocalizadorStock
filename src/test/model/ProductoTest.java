package test.java.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.model.Producto;

class ProductoTest {
	
	@BeforeEach
	public void setup() {
		
	}

	@Test
    void dosProductosConElMismoCodigoSonIguales() {
        Producto a = new Producto("P001", "Camiseta", "rojo", "camisetas", "hombre");
        Producto b = new Producto("P001", "Camiseta básica", "azul", "camisetas", "hombre");
        
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
    void elCodigoNoPuedeSerNuloNiVacio() {
        assertThrows(IllegalArgumentException.class, () -> new Producto(null, "Camiseta", "rojo", "camisetas", "unisex"));
        assertThrows(IllegalArgumentException.class, () -> new Producto("  ", "Camiseta", "rojo", "camisetas", "unisex"));
    }
}
