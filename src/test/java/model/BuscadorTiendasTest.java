package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class BuscadorTiendasTest {
	private CalculadorDistanciaMock calculadorDistancia;
	private RepositorioTiendasMock repositorio;
	private BuscadorTiendas buscadorTiendas;
	
	private Producto producto;
	private Tienda origen;
	private Tienda cercana;
	private Tienda lejana;
	private Tienda fueraDelLimite;
    private Tienda sinStock;

	@BeforeEach
	void setUp(){
		repositorio = new RepositorioTiendasMock();
		calculadorDistancia = new CalculadorDistanciaMock();
		buscadorTiendas = new BuscadorTiendas(repositorio, calculadorDistancia);
		
		producto = new Producto("P001", "Leche", "Blanco", "Alimento", "Adulto");
	    origen = new Tienda("O", "Origen", 41.0, 2.0, new Inventario());
	    
	    cercana = repositorio.crearTiendaConStock(
                "T1", "Cercana", producto, 3);
        lejana = repositorio.crearTiendaConStock(
                "T2", "Lejana", producto, 3);
        fueraDelLimite = repositorio.crearTiendaConStock(
                "T3", "Fuera del límite", producto, 3);
        sinStock = repositorio.crearTienda(
                "T4", "Sin stock");
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
	void testBuscarTiendasConStock() {
		//dentro del limite
		calculadorDistancia.configurarDistancia(cercana, 2.0);
        calculadorDistancia.configurarDistancia(lejana, 8.0);
        calculadorDistancia.configurarDistancia(fueraDelLimite, 10.1);
        calculadorDistancia.configurarDistancia(sinStock, 1.0);
		
        List<BuscadorTiendas.TiendaConDistancia> resultado = buscadorTiendas.buscarTiendasConStock(
        		origen, 
        		new LineaPeticion(producto, 2), 
        		new RestriccionesBusqueda());
        
        assertEquals(2, resultado.size());
        
        // Verificamos las tiendas y su distancia
        assertEquals(cercana, resultado.get(0).tienda());
        assertEquals(2.0, resultado.get(0).distancia(), 0.001);
        assertEquals(lejana, resultado.get(1).tienda());
        assertEquals(8.0, resultado.get(1).distancia(), 0.001);
	}

}
