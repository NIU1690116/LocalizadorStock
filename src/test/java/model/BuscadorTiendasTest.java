package model;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class BuscadorTiendasTest {
	private CalculadorDistancia calculadorDistancia;
	private RepositorioTiendas repositorio;
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
	    
	    cercana = repositorio.crearTiendaConStock("T1", producto, 3);
	    lejana = repositorio.crearTiendaConStock("T2", producto, 3);
	    fueraDelLimite = repositorio.crearTiendaConStock("T3", "Fuera del límite", 3);
        sinStock = repositorio.crearTienda("T4", "Sin stock");
	}

	@Test
	void testBuscadorTiendas() {
		
	}

	@Test
	void testBuscarTiendasConStock() {
	}

}
