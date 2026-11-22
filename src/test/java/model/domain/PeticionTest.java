package model.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
	void testConstructorParametrosValidos() {
		assertThrows(IllegalArgumentException.class,
				() -> new Peticion(null, LocalDateTime.now()));
		assertThrows(IllegalArgumentException.class,
				() -> new Peticion("  ", LocalDateTime.now()));
		assertThrows(IllegalArgumentException.class,
				() -> new Peticion("SOL002", null));
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
	void testGetLineasVistaNoModificable() {
		List<LineaPeticion> lineas = peticion.getLineas();
		LineaPeticion linea = new LineaPeticion(camiseta, 2);

		peticion.agregarLinea(linea);

		assertEquals(List.of(linea), lineas);
		assertThrows(UnsupportedOperationException.class, () -> lineas.add(linea));
		assertThrows(UnsupportedOperationException.class, lineas::clear);
	}
	
	
	

}
