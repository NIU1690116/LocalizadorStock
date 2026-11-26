package model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de las condiciones de construcción de ResultadoLineaPeticion
 * Se centra en particiones inválidas de los datos obligatorios y en la
 * consistencia entre la lista, la mejor alternativa y el estado. Verifica el
 * contrato del constructor (DbC) desde su comportamiento
 */
class ResultadoLineaPeticionTest {
	private LineaPeticion linea;
	private Tienda tienda;
	private Alternativa alternativa;

	@BeforeEach
	void setUp() {
		Producto producto = new Producto("P001", "Camiseta", "rojo", "camisetas", "mujer");
		linea = new LineaPeticion(producto, 2);
		tienda = new Tienda("T002", "Tienda Centro", 41.0, 2.0, new Inventario());
		alternativa = new AlternativaOtraTienda(tienda, 15, 3.0);
	}

	
	@Test
	void testResultadoLineaPeticionInvalido() {
		// Partición inválida: no se proporciona la línea de petición obligatoria.
		assertThrows(IllegalArgumentException.class,
				() -> new ResultadoLineaPeticion(null, List.of(), alternativa, EstadoResolucion.RESUELTA));
		// Partición inválida: la lista de alternativas es nula.
		assertThrows(IllegalArgumentException.class,
				() -> new ResultadoLineaPeticion(linea, null, alternativa, EstadoResolucion.RESUELTA));
		// Partición inválida: el estado de resolución es nulo.
		assertThrows(IllegalArgumentException.class,
				() -> new ResultadoLineaPeticion(linea, List.of(alternativa), alternativa, null));
		// Partición inválida: la lista sigue siendo obligatoria aunque no haya alternativa mejor.
		assertThrows(IllegalArgumentException.class,
				() -> new ResultadoLineaPeticion(linea, null, null, EstadoResolucion.RESUELTA));
		// Invariante: la alternativa marcada como mejor debe estar en la lista
		assertThrows(IllegalArgumentException.class,
				() -> new ResultadoLineaPeticion(
						linea, List.of(), alternativa, EstadoResolucion.ALTERNATIVA_PROPUESTA));
		// Invariante: el estado de alternativa propuesta requiere una alternativa mejor
		assertThrows(IllegalArgumentException.class,
				() -> new ResultadoLineaPeticion(
						linea, List.of(alternativa), null, EstadoResolucion.ALTERNATIVA_PROPUESTA));
	}
}
