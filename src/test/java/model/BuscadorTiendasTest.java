package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class BuscadorTiendasTest {
	private CalculadorDistanciaMock calculadorDistancia;
	private RepositorioTiendasMock repositorio;
	private BuscadorTiendas buscadorTiendas;
	
	private Producto producto;
	private Producto producto_2;
	private Tienda origen;

	@BeforeEach
	void setUp(){
		repositorio = new RepositorioTiendasMock();
		calculadorDistancia = new CalculadorDistanciaMock();
		buscadorTiendas = new BuscadorTiendas(repositorio, calculadorDistancia);
		
		producto = new Producto("P001", "Camisa", "Camisas", "", "Mujer");
		producto_2 = new Producto("P002", "Camisa", "Camisas", "", "Hombre");
	    origen = new Tienda("T001", "Origen", 41.0, 2.0, new Inventario());
		
	}

	@Test
	void testBuscadorTiendasConstructor() {
		assertThrows(
				IllegalArgumentException.class, () -> new BuscadorTiendas(null, calculadorDistancia)
		);
		assertThrows(
				IllegalArgumentException.class, () -> new BuscadorTiendas(repositorio, null)
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
        
        List<BuscadorTiendas.TiendaConDistancia> resultado =
        		buscadorTiendas.buscarTiendasConStock(
                        origen,
                        new LineaPeticion(producto, 2),
                        new RestriccionesBusqueda());
        // Verificamos las tiendas y su distancia
        assertEquals(3, resultado.size());
        assertEquals(cercana, resultado.get(0).tienda());//tienda mas cercana en primer lugar
        assertEquals(2.0, resultado.get(0).distancia(), 0.001);
        assertEquals(lejana, resultado.get(1).tienda());//lejana dentro del limite
        assertEquals(8.0, resultado.get(1).distancia(), 0.001);
        assertEquals(enElLimite, resultado.get(2).tienda());//en el limite
        assertEquals(10.0, resultado.get(2).distancia(), 0.001);
        //se eliminan las tiendas sin stock y fuera del limite de distancia
        
        //Ninguna tienda cumple con las restricciones de stock y distancia
        List<BuscadorTiendas.TiendaConDistancia> resultado1 =
        		buscadorTiendas.buscarTiendasConStock(
                        origen,
                        new LineaPeticion(producto_2, 1),
                        new RestriccionesBusqueda());
        assertNotNull(resultado1);
        assertTrue(resultado1.isEmpty());
        
          
	}
	
	

}
