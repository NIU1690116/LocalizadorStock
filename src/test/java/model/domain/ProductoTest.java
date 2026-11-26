package model.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de identidad, validación y acceso a los datos de Producto
 * Combinan caja negra sobre sus operaciones públicas, particiones de equivalencia
 * de códigos y atributos, pruebas de límites para el código y cobertura de ramas
 * de equals
 */
class ProductoTest {
	
	@Test
    void dosProductosConElMismoCodigoSonIguales() {
        Producto a = new Producto("P001", "Camiseta", "rojo", "camisetas", "hombre");
        Producto b = new Producto("P001", "Camiseta básica", "azul", "camisetas", "hombre");
        
        // La identidad depende del código aunque cambien los atributos descriptivos
        assertEquals(a, b);
        // Contrato de igualdad: objetos iguales deben compartir hashCode
        assertEquals(a.hashCode(), b.hashCode());
    }
	
	@Test
    void exponeSusAtributos() {
        Producto p = new Producto("P001", "Camiseta", "rojo", "camisetas", "mujer");
 
        // Caja negra: cada getter devuelve el valor inicial correspondiente.
        assertEquals("P001", p.getCodigo());
        assertEquals("Camiseta", p.getNombre());
        assertEquals("rojo", p.getColor());
        assertEquals("camisetas", p.getFamilia());
        assertEquals("mujer", p.getGenero());
    }

	@Test
	void testSettersNoCambian() {
		Producto producto = new Producto("P001", "Camiseta", "rojo", "camisetas", "mujer");

		// Particiones de setters: se aceptan texto vacío, null, espacios y texto normal.
		producto.setNombre("");
		producto.setColor(null);
		producto.setFamilia(" ");
		producto.setGenero("unisex");

		assertEquals("P001", producto.getCodigo());
		assertEquals("", producto.getNombre());
		assertNull(producto.getColor());
		assertEquals(" ", producto.getFamilia());
		assertEquals("unisex", producto.getGenero());

		// Se vuelven a probar null y vacío en atributos distintos para confirmar que
		// los setters no aplican validación ni alteran el código identificador.
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
        // Particiones inválidas del código: null, vacío, espacios y tabulador
        assertThrows(IllegalArgumentException.class, () -> new Producto(null, "Camiseta", "rojo", "camisetas", "unisex"));
        assertThrows(IllegalArgumentException.class, () -> new Producto("", "Camiseta", "rojo", "camisetas", "unisex"));
        assertThrows(IllegalArgumentException.class, () -> new Producto("  ", "Camiseta", "rojo", "camisetas", "unisex"));
        assertThrows(IllegalArgumentException.class, () -> new Producto("\t", "Camiseta", "rojo", "camisetas", "unisex"));

        // Límite válido mínimo: un carácter no blanco basta, los atributos descriptivos pueden ser null
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

		// Partición de identidad: códigos distintos representan productos distintos
		assertNotEquals(a, b);
		// Caja blanca: cubre la rama de equals para un objeto de otro tipo
		assertNotEquals(a, "P001");
	}
}
