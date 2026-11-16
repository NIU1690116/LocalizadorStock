package model.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ResultadoBusquedaTest {

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
