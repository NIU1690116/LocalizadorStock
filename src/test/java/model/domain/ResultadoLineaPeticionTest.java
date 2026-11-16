package model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		assertThrows(IllegalArgumentException.class,
				() -> new ResultadoLineaPeticion(null, List.of(), alternativa, EstadoResolucion.RESUELTA));
		assertThrows(IllegalArgumentException.class,
				() -> new ResultadoLineaPeticion(linea, null, alternativa, EstadoResolucion.RESUELTA));
		assertThrows(IllegalArgumentException.class,
				() -> new ResultadoLineaPeticion(linea, List.of(), alternativa, null));
		//caso lista alternativas debe contener mejor alternativa
		assertThrows(IllegalArgumentException.class,
				() -> new ResultadoLineaPeticion(linea, null, null, EstadoResolucion.RESUELTA));
		assertThrows(IllegalArgumentException.class,
				() -> new ResultadoLineaPeticion(
						linea, List.of(), alternativa, EstadoResolucion.ALTERNATIVA_PROPUESTA));
		assertThrows(IllegalArgumentException.class,
				() -> new ResultadoLineaPeticion(
						linea, List.of(alternativa), null, EstadoResolucion.ALTERNATIVA_PROPUESTA));
	}
}
