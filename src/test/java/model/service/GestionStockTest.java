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
	
	@Test
	void testGestionStockDependenciasNulas() {
		assertThrows(IllegalArgumentException.class,
				() -> new GestionStock(null, buscadorTiendas, servicioReposicion,
						buscadorSimilares, evaluador, repositorio));
		assertThrows(IllegalArgumentException.class,
				() -> new GestionStock(validador, null, servicioReposicion,
						buscadorSimilares, evaluador, repositorio));
		assertThrows(IllegalArgumentException.class,
				() -> new GestionStock(validador, buscadorTiendas, null,
						buscadorSimilares, evaluador, repositorio));
		assertThrows(IllegalArgumentException.class,
				() -> new GestionStock(validador, buscadorTiendas, servicioReposicion,
						null, evaluador, repositorio));
		assertThrows(IllegalArgumentException.class,
				() -> new GestionStock(validador, buscadorTiendas, servicioReposicion,
						buscadorSimilares, null, repositorio));
		assertThrows(IllegalArgumentException.class,
				() -> new GestionStock(validador, buscadorTiendas, servicioReposicion,
						buscadorSimilares, evaluador, null));
		//buscar alternativa
		assertThrows(IllegalArgumentException.class,
	            () -> gestionStock.buscarAlternativas(null, origen, restricciones));
	    assertThrows(IllegalArgumentException.class,
	            () -> gestionStock.buscarAlternativas(peticion, null, restricciones));
	    assertThrows(IllegalArgumentException.class,
	            () -> gestionStock.buscarAlternativas(peticion, origen, null));
	    //peticion valida -> buscarAlternativas
	    when(validador.validarPeticion(peticion)).thenReturn(false);
	    assertThrows(IllegalArgumentException.class,
	            () -> gestionStock.buscarAlternativas(peticion, origen, restricciones));
	}
	
	@Test
	void testResolverLinea() {
	    origen.getInventario().añadirProductos(producto, 2);

	    ResultadoBusqueda resultado = gestionStock.buscarAlternativas(peticion, origen, restricciones);

	    ResultadoLineaPeticion resultadoLinea = resultado.getResultados().get(0);
	    assertEquals(EstadoResolucion.RESUELTA, resultadoLinea.getEstado());
	    assertNull(resultadoLinea.getMejorAlternativa());
	    assertTrue(resultado.estaResuelta());
	    //evaluamos que si la tienda tiene stock no se llame al evaluador o buscador externo
	    verifyNoInteractions(buscadorTiendas, servicioReposicion, buscadorSimilares, evaluador, repositorio);
	}
	
	@Test
	void testResolverLineaTiendaSinStock() {
		origen.getInventario().añadirProductos(producto, 1);
		Tienda destino = new Tienda("T002", "Norte", 41.01, 2.01, new Inventario());
		when(buscadorTiendas.buscarTiendasConStock(origen, peticion.getLineas().get(0), restricciones))
				.thenReturn(List.of(new BuscadorTiendas.TiendaConDistancia(destino, 2.5, 15)));
		Reposicion reposicion = new Reposicion(producto, 1, peticion.getFechaSolicitud().plusMinutes(60));
		when(servicioReposicion.consultarReposicion(origen, producto)).thenReturn(reposicion);
		when(validador.validarReposicion(
				any(Peticion.class), any(LineaPeticion.class), same(reposicion), same(restricciones)))
				.thenReturn(true);
		when(evaluador.seleccionarMejor(anyList())).thenAnswer(invocacion ->
				((List<Alternativa>) invocacion.getArgument(0)).get(0));

		ResultadoBusqueda resultado = gestionStock.buscarAlternativas(peticion, origen, restricciones);

		ResultadoLineaPeticion linea = resultado.getResultados().get(0);
		assertEquals(EstadoResolucion.ALTERNATIVA_PROPUESTA, linea.getEstado());
		assertEquals(2, linea.getAlternativas().size());
		assertSame(linea.getAlternativas().get(0), linea.getMejorAlternativa());
	}
	
	@Test
	void testBuscarProductosSimilares() {
		Tienda tiendaSimilar = new Tienda("T003", "Sur", 41.02, 2.02, new Inventario());
		AlternativaProductoSimilar similar = new AlternativaProductoSimilar(
				new Producto("P002", "Camisa", "Rojo", "Camisa", "Unisex"),
				tiendaSimilar, 100.0, 20, 4.0);
		when(buscadorTiendas.buscarTiendasConStock(origen, peticion.getLineas().get(0), restricciones))
				.thenReturn(List.of());
		when(servicioReposicion.consultarReposicion(origen, producto)).thenReturn(null);
		when(repositorio.obtenerTiendas()).thenReturn(List.of(origen, tiendaSimilar));
		when(buscadorSimilares.buscarSimilares(peticion.getLineas().get(0), origen, List.of(origen, tiendaSimilar)))
				.thenReturn(List.of(similar));
	}
	
	
}
