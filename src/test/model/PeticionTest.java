package test.java.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import main.java.model.LineaPeticion;
import main.java.model.Peticion;
import main.java.model.Producto;

class PeticionTest {
	private Peticion peticion;
    private Producto camiseta;
    private Producto pantalon;

	@BeforeEach
	void setUp() {
		peticion = new Peticion("SOL001", LocalDateTime.of(2026, 10, 22, 10, 0));
		camiseta = new Producto("P001", "Camiseta", "Rojo", "camisetas", "mujer");
        pantalon = new Producto("P002", "Pantalon", "Azul", "pantalones", "mujer");
	}

	@Test
	void testCrearPeticion() {
		assertEquals("SOL001", peticion.getId());
        assertEquals(LocalDateTime.of(2026, 10, 22, 10, 0), peticion.getFechaSolicitud());
	}
	
	@Test
	void testAgregarLineas() {
		assertTrue(peticion.getLineas().isEmpty());
		LineaPeticion linea = new LineaPeticion(camiseta, 2);

        peticion.agregarLinea(linea);
        assertEquals(1, peticion.getLineas().size());
        
        assertThrows(IllegalArgumentException.class, () -> peticion.agregarLinea(null));
	}
	
	@Test
    void peticionPuedeContenerVariosProductos() {
        peticion.agregarLinea(new LineaPeticion(camiseta, 2));
        peticion.agregarLinea(new LineaPeticion(pantalon, 1));

        assertEquals(2, peticion.getLineas().size());
        assertSame(camiseta, peticion.getLineas().get(0).getProducto());
        assertSame(pantalon, peticion.getLineas().get(1).getProducto());
    }
	
	

}
