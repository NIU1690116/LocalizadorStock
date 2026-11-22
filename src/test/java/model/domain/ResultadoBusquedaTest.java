package model.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ResultadoBusquedaTest {
	@Test
	void testResultadoBusquedaParametrosNulos() {
		assertThrows(IllegalArgumentException.class, () -> new ResultadoBusqueda(null));
		assertThrows(IllegalArgumentException.class, () -> new ResultadoBusqueda(List.of()));

	}

	@Test
	void testResultadosListaNoModificable() {

	}
	
	@Test
	void testResultadoBusqueda() {
		//un resultado de busqueda esta resuelto si cada resultado linea tiene 
		//EstadoResolucion como RESUELTA con alternativa propuesta
		ResultadoLineaPeticion conAlternativa = resultadoConAlternativa("P002");
		ResultadoBusqueda resultado = new ResultadoBusqueda(List.of(
				resultadoLinea("P001", EstadoResolucion.RESUELTA), conAlternativa));
		assertTrue(resultado.estaResuelta());
		
		ResultadoBusqueda resultado_2 = new ResultadoBusqueda(List.of(
				resultadoLinea("P001", EstadoResolucion.RESUELTA),
				resultadoLinea("P002", EstadoResolucion.RESUELTA)));
		assertTrue(resultado_2.estaResuelta());
		
		ResultadoBusqueda resultado_3 = new ResultadoBusqueda(List.of(
				resultadoLinea("P001", EstadoResolucion.RESUELTA),
				resultadoLinea("P002", EstadoResolucion.SIN_SOLUCION)));

		assertFalse(resultado_3.estaResuelta());
		
	}

	private ResultadoLineaPeticion resultadoLinea(String codigo, EstadoResolucion estado) {
		Producto producto = new Producto(codigo, "Producto", "rojo", "camisetas", "mujer");
		return new ResultadoLineaPeticion(
				new LineaPeticion(producto, 1), List.of(), null, estado);
	}

	private ResultadoLineaPeticion resultadoConAlternativa(String codigo) {
		Producto producto = new Producto(codigo, "Producto", "rojo", "camisetas", "mujer");
		LineaPeticion linea = new LineaPeticion(producto, 1);
		Tienda tienda = new Tienda("T001", "Tienda Centro", 41.0, 2.0, new Inventario());
		Alternativa alternativa = new AlternativaOtraTienda(tienda, 15, 3.0);
		return new ResultadoLineaPeticion(
				linea, List.of(alternativa), alternativa, EstadoResolucion.ALTERNATIVA_PROPUESTA);
	}
}
