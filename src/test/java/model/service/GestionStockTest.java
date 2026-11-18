package model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.Alternativa;
import model.domain.AlternativaProductoSimilar;
import model.domain.AlternativaOtraTienda;
import model.domain.AlternativaReposicion;
import model.domain.EstadoResolucion;
import model.domain.Inventario;
import model.domain.LineaPeticion;
import model.domain.Peticion;
import model.domain.Producto;
import model.domain.Reposicion;
import model.domain.RestriccionesBusqueda;
import model.domain.ResultadoBusqueda;
import model.domain.ResultadoLineaPeticion;
import model.domain.Tienda;
import model.repository.RepositorioTiendas;

class GestionStockTest {
	private ValidadorPeticion validador;
	private BuscadorTiendas buscadorTiendas;
	private ServicioReposicion servicioReposicion;
	private BuscadorProductosSimilares buscadorSimilares;
	private EvaluadorAlternativas evaluador;
	private RepositorioTiendas repositorio;
	private GestionStock gestionStock;
	private Tienda origen;
	private Producto producto;
	private Peticion peticion;
	private RestriccionesBusqueda restricciones;

	@BeforeEach
	void setUp() {
		validador = mock(ValidadorPeticion.class);
		buscadorTiendas = mock(BuscadorTiendas.class);
		servicioReposicion = mock(ServicioReposicion.class);
		buscadorSimilares = mock(BuscadorProductosSimilares.class);
		evaluador = mock(EvaluadorAlternativas.class);
		repositorio = mock(RepositorioTiendas.class);
		gestionStock = new GestionStock(
				validador, buscadorTiendas, servicioReposicion,
				buscadorSimilares, evaluador, repositorio);
		origen = new Tienda("T001", "Centro", 41.0, 2.0, new Inventario());
		producto = new Producto("P001", "Camisa", "Rojo", "Camisa", "Unisex");
		peticion = new Peticion("REQ-1", LocalDateTime.of(2026, 11, 16, 10, 0));
		peticion.agregarLinea(new LineaPeticion(producto, 2));
		restricciones = new RestriccionesBusqueda();
		when(validador.validarPeticion(peticion)).thenReturn(true);
	}
}
