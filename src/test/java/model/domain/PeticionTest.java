package model.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de creación y gestión de líneas de Peticion
 * Verifican comportamiento observable, particiones válidas e inválidas de los
 * datos obligatorios, la lista no modificable y la conservación del orden de las
 * líneas, incluyen implementación DbC al añadir entradas
 */
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
		// Caja negra: caso válido que verifica los valores observables iniciales
		assertEquals("SOL001", peticion.getId());
        assertEquals(LocalDateTime.of(2026, 10, 22, 10, 0), peticion.getFechaSolicitud());
	}

	@Test
	void testConstructorParametrosValidos() {
		// Particiones de equivalencia inválidas: identificador nulo o en blanco
		assertThrows(IllegalArgumentException.class,
				() -> new Peticion(null, LocalDateTime.now()));
		assertThrows(IllegalArgumentException.class,
				() -> new Peticion("  ", LocalDateTime.now()));
		// DbC / partición inválida: falta la fecha de solicitud obligatoria
		assertThrows(IllegalArgumentException.class,
				() -> new Peticion("SOL002", null));
	}
	
	@Test
	void testAgregarLineas() {
		// Caja negra: partición válida vacía al inicio y tras añadir una línea
		assertTrue(peticion.getLineas().isEmpty());
		LineaPeticion linea = new LineaPeticion(camiseta, 2);

		// DbC: añadir una línea válida aumenta el tamaño de la colección
        peticion.agregarLinea(linea);
        assertEquals(1, peticion.getLineas().size());
        
		// DbC partición inválida: no se permite añadir una línea nula
        assertThrows(IllegalArgumentException.class, () -> peticion.agregarLinea(null));
	}

	@Test
	void testAgregarLinea() {
		List<LineaPeticion> lineas = peticion.getLineas();
		int numeroLineasInicial = lineas.size();
		LineaPeticion linea = new LineaPeticion(pantalon, 3);

		// Caja negra: la vista obtenida antes de añadir refleja la nueva línea y su identidad
		peticion.agregarLinea(linea);

		assertEquals(numeroLineasInicial + 1, lineas.size());
		assertSame(linea, lineas.get(numeroLineasInicial));
		assertTrue(lineas.stream().allMatch(lineaPeticion ->
				lineaPeticion != null
						&& lineaPeticion.getProducto() != null
						&& lineaPeticion.getCantidad() > 0));
	}

	@Test
	void testGetLineasVistaNoModificable() {
		List<LineaPeticion> lineas = peticion.getLineas();
		LineaPeticion linea = new LineaPeticion(camiseta, 2);

		// Caja negra: la vista de solo lectura refleja cambios hechos mediante la petición
		peticion.agregarLinea(linea);

		assertEquals(List.of(linea), lineas);
		// Particiones de modificación inválida: ni añadir ni borrar desde la vista está permitido
		assertThrows(UnsupportedOperationException.class, () -> lineas.add(linea));
		assertThrows(UnsupportedOperationException.class, lineas::clear);
	}
	
	@Test
    void testPeticionPuedeContenerVariosProductos() {
		// Caja negra, prueba de secuencia: varias líneas conservan el orden de inserción
        peticion.agregarLinea(new LineaPeticion(camiseta, 2));
        peticion.agregarLinea(new LineaPeticion(pantalon, 1));

        assertEquals(2, peticion.getLineas().size());
        assertSame(camiseta, peticion.getLineas().get(0).getProducto());
        assertSame(pantalon, peticion.getLineas().get(1).getProducto());
    }
	
}
