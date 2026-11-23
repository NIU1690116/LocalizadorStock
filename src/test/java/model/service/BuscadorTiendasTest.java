package model.service;

import model.distance.CalculadorDistanciaMock;

import model.distance.EstimadorTiempoViaje;
import model.domain.Inventario;
import model.domain.LineaPeticion;
import model.domain.Producto;
import model.domain.RestriccionesBusqueda;
import model.domain.Tienda;
import model.repository.RepositorioTiendasMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class BuscadorTiendasTest {
	private CalculadorDistanciaMock calculadorDistancia;
	private RepositorioTiendasMock repositorio;
	private BuscadorTiendas buscadorTiendas;
	private EstimadorTiempoViaje estimadorTiempoViaje;
	
	private Producto producto;
	private Producto producto_2;
	private Tienda origen;

	@BeforeEach
	void setUp(){
		repositorio = new RepositorioTiendasMock();
		calculadorDistancia = new CalculadorDistanciaMock();
		estimadorTiempoViaje = mock(EstimadorTiempoViaje.class);
		buscadorTiendas = new BuscadorTiendas(repositorio, calculadorDistancia, estimadorTiempoViaje);
		
		producto = new Producto("P001", "Camisa", "Camisas", "", "Mujer");
		producto_2 = new Producto("P002", "Camisa", "Camisas", "", "Hombre");
	    origen = new Tienda("T001", "Origen", 41.0, 2.0, new Inventario());
	}

	@Test
	void testBuscadorTiendasConstructor() {
		assertDoesNotThrow(() -> new BuscadorTiendas(
				repositorio, calculadorDistancia, estimadorTiempoViaje));
		assertThrows(
				IllegalArgumentException.class,
				() -> new BuscadorTiendas(null, calculadorDistancia, estimadorTiempoViaje)
		);
		assertThrows(
				IllegalArgumentException.class,
				() -> new BuscadorTiendas(repositorio, null, estimadorTiempoViaje)
		);
		assertThrows(
				IllegalArgumentException.class,
				() -> new BuscadorTiendas(repositorio, calculadorDistancia, null)
		);
	}
	
	@Test
	void testBuscadorTiendasConStockParametrosNulos() {
		LineaPeticion lineaValida = new LineaPeticion(producto, 2);
        RestriccionesBusqueda restriccionesValidas = new RestriccionesBusqueda();

        // Validamos que el método buscarTiendasConStock lanza IllegalArgumentException si le pasas nulls
        assertThrows(IllegalArgumentException.class, () -> 
            buscadorTiendas.buscarTiendasConStock(null, lineaValida, restriccionesValidas)
        );
        assertThrows(IllegalArgumentException.class, () -> 
            buscadorTiendas.buscarTiendasConStock(origen, null, restriccionesValidas)
        );
        assertThrows(IllegalArgumentException.class, () -> 
            buscadorTiendas.buscarTiendasConStock(origen, lineaValida, null)
        );
	}
	
	@Test
	void testBuscarTiendasConStock() {
		// Decision + condition coverage: distancia y stock se varían independientemente.
	    Tienda cercana = repositorio.crearTiendaConStock(
                "T1", "Cercana", producto, 3);
	    Tienda lejana = repositorio.crearTiendaConStock(
                "T2", "Lejana", producto, 3);
	    Tienda fueraDelLimite = repositorio.crearTiendaConStock(
                "T3", "Fuera del límite", producto, 3);
	    Tienda sinStock = repositorio.crearTienda(
                "T4", "Sin stock");
        Tienda enElLimite = repositorio.crearTiendaConStock(
                "T5", "En el límite", producto, 2);
        
		//dentro del limite
		calculadorDistancia.configurarDistancia(cercana, 2.0);
        calculadorDistancia.configurarDistancia(lejana, 8.0);
        //sin stock
        calculadorDistancia.configurarDistancia(sinStock, 1.0);
        //fuera del limite
        calculadorDistancia.configurarDistancia(fueraDelLimite, 10.1);  
        //en el limite
        calculadorDistancia.configurarDistancia(enElLimite, 10.0);
        
        when(estimadorTiempoViaje.estimarMinutos(origen, cercana)).thenReturn(12);
        when(estimadorTiempoViaje.estimarMinutos(origen, lejana)).thenReturn(48);
        when(estimadorTiempoViaje.estimarMinutos(origen, enElLimite)).thenReturn(70);
        
        List<BuscadorTiendas.TiendaConDistancia> resultado =
        		buscadorTiendas.buscarTiendasConStock(
                        origen,
                        new LineaPeticion(producto, 2),
                        new RestriccionesBusqueda());
        // Verificamos las tiendas y su distancia
        assertEquals(2.0, resultado.get(0).distancia(), 0.001);
        assertEquals(12, resultado.get(0).tiempoEstimadoMinutos());
        assertEquals(lejana, resultado.get(1).tienda());//lejana dentro del limite
        assertEquals(8.0, resultado.get(1).distancia(), 0.001);
        assertEquals(48, resultado.get(1).tiempoEstimadoMinutos());
        assertEquals(enElLimite, resultado.get(2).tienda());//en el limite
        assertEquals(10.0, resultado.get(2).distancia(), 0.001);
        assertEquals(70, resultado.get(2).tiempoEstimadoMinutos());
        //se eliminan las tiendas sin stock y fuera del limite de distancia
		for (BuscadorTiendas.TiendaConDistancia candidata : resultado) {
			assertNotEquals(origen.getId(), candidata.tienda().getId());
			assertTrue(new RestriccionesBusqueda().permiteDistancia(candidata.distancia()));
			assertTrue(candidata.tienda().getInventario().hayStockSuficiente(producto, 2));
		}
		for (int i = 1; i < resultado.size(); i++) {
			assertTrue(resultado.get(i - 1).distancia() <= resultado.get(i).distancia());
		}
        
        //Ninguna tienda cumple con las restricciones de stock y distancia
        List<BuscadorTiendas.TiendaConDistancia> resultado1 =
        		buscadorTiendas.buscarTiendasConStock(
                        origen,
                        new LineaPeticion(producto_2, 1),
                        new RestriccionesBusqueda());
        assertNotNull(resultado1);
        assertTrue(resultado1.isEmpty());  
          
	}

	@Test
	void testExcluirTiendaOrigen() {
		Tienda origenEnRepositorio = repositorio.crearTiendaConStock(
				origen.getId(), origen.getNombre(), producto, 5);
		Tienda alternativa = repositorio.crearTiendaConStock(
				"T2", "Alternativa", producto, 5);
		calculadorDistancia.configurarDistancia(alternativa, 2.0);

		List<BuscadorTiendas.TiendaConDistancia> resultado =
				buscadorTiendas.buscarTiendasConStock(
						origen,
						new LineaPeticion(producto, 2),
						new RestriccionesBusqueda());

		assertEquals(1, resultado.size());
		assertEquals(alternativa, resultado.get(0).tienda());
		assertNotEquals(origenEnRepositorio.getId(), resultado.get(0).tienda().getId());
	}
	
}

