package model.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de construcción y validación de Reposicion
 * Combinan caja negra sobre los datos guardados, particiones válidas e
 * inválidas y límites de cantidad, las fechas se prueban como valores
 * obligatorios 
 */
class ReposicionTest {
	private Producto producto;
    private LocalDateTime fechaPrevista; 
	
    @BeforeEach
	void setUp() {
		producto = new Producto("P001", "Camisa", "Rojo", "Camisa", "Unisex");
		fechaPrevista = LocalDateTime.of(2026, 10, 22, 11, 0);
	}
    
    @Test
    void testCreaReposicionValida() {
		// Partición válida: producto, cantidad positiva y fecha no nula se conservan
    	Reposicion reposicion = new Reposicion(producto, 1, fechaPrevista);
        assertEquals(producto.getCodigo(), reposicion.getProducto().getCodigo());
        assertEquals(1, reposicion.getCantidad());
        assertEquals(fechaPrevista, reposicion.getFechaPrevista());

		// Valor extremo no nulo de LocalDateTime: el constructor no restringe la fecha a futuro
		Reposicion fechaExtrema = new Reposicion(producto, 1, LocalDateTime.MIN);
		assertEquals(LocalDateTime.MIN, fechaExtrema.getFechaPrevista());
    }

	@Test
	void testCantidadValidad() {
		// Límite inferior válido: una unidad es una cantidad aceptada
		Reposicion minima = new Reposicion(producto, 1, fechaPrevista);
		// Límite superior representable: el constructor admite Integer.MAX_VALUE
		Reposicion maxima = new Reposicion(producto, Integer.MAX_VALUE, fechaPrevista);

		assertEquals(1, minima.getCantidad());
		assertEquals(Integer.MAX_VALUE, maxima.getCantidad());
	}
    
    @Test
    void testRechazaProductoFechaCantidadInvalidos() {
		// Partición inválida: producto obligatorio ausente
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(null, 1, fechaPrevista));
		// Límite inválido: cero no es una cantidad positiva
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(producto, 0, fechaPrevista));
		// Partición inválida: cantidad negativa, incluido el entero mínim
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(producto, -1, fechaPrevista));
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(producto, Integer.MIN_VALUE, fechaPrevista));
		// Partición inválida: fecha prevista obligatoria ausente
        assertThrows(IllegalArgumentException.class, () -> new Reposicion(producto, 1, null));
    }

}
