package controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.EstadoResolucion;
import model.domain.Inventario;
import model.domain.LineaPeticion;
import model.domain.Peticion;
import model.domain.Producto;
import model.domain.RestriccionesBusqueda;
import model.domain.ResultadoBusqueda;
import model.domain.ResultadoLineaPeticion;
import model.domain.Tienda;
import model.service.GestionStock;

/**
 * Prueba unitaria del controlador con GestionStock simulado
 * Comprueba la delegación de la consulta y la propagación del resultado usando
 * una prueba de caja negra con el mock
 */
class StockControllerTest {
	private GestionStock gestionStock;
	private StockController controlador;
	private Peticion peticion;
	private Tienda tienda;
	private RestriccionesBusqueda restricciones;
	private ResultadoBusqueda resultado;

	/** Prepara el controlador, el mock y los datos válidos compartidos por el caso. */
	@BeforeEach
	void setUp() {
		gestionStock = mock(GestionStock.class);
		controlador = new StockController(gestionStock);
		peticion = new Peticion("REQ-1", LocalDateTime.of(2026, 11, 18, 10, 0));
		peticion.agregarLinea(new LineaPeticion(
				new Producto("P001", "Camisa", "Rojo", "Camisa", "Unisex"), 1));
		tienda = new Tienda("T001", "Centro", 41.0, 2.0, new Inventario());
		restricciones = new RestriccionesBusqueda();
		ResultadoLineaPeticion linea = new ResultadoLineaPeticion(
				peticion.getLineas().get(0), List.of(), null, EstadoResolucion.RESUELTA);
		resultado = new ResultadoBusqueda(List.of(linea));
	}

	/**
	 * Comprueba que consultar delega los mismos datos al servicio y devuelve su resultado
	 * Tipo: prueba unitaria de caja negra con mockito, verifica la interacción con el
	 * servicio y que la respuesta sea la misma instancia configurada
	 */
	@Test
	void testGestionController() {
		when(gestionStock.buscarAlternativas(peticion, tienda, restricciones)).thenReturn(resultado);
		ResultadoBusqueda respuesta = controlador.consultar(peticion, tienda, restricciones);
		assertSame(resultado, respuesta);
	}
}
