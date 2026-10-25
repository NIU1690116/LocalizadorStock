package test.java.model;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import main.java.model.BuscadorTiendas;
import main.java.model.CalculadorDistancia;
import main.java.model.Inventario;
import main.java.model.LineaPeticion;
import main.java.model.Producto;
import main.java.model.RepositorioTiendas;
import main.java.model.RestriccionesBusqueda;
import main.java.model.Tienda;

class BuscadorTiendasTest {
	CalculadorDistancia calculadorDistancia;
	RepositorioTiendas repositorio;
	BuscadorTiendas buscadorTiendas;
	
	Producto producto;
	Tienda origen;
	Tienda destino;
	Inventario inventario;
	
	private LineaPeticion linea;
    private RestriccionesBusqueda restricciones;
	

	@BeforeEach
	void setUp(){
		producto = new Producto("P001", "Camisa", "Rojo", "Camisa", "Hombre");
		inventario = new Inventario();
		inventario.añadirProductos(producto, 3);
		Tienda origen = new Tienda(
		        "T001", "Tienda cercana", 41.4, 2.1, inventario);
		
		linea = new LineaPeticion(producto, 3);
        restricciones = new RestriccionesBusqueda();

		repositorio = new RepositorioTiendasMock();
		calculadorDistancia = mock(CalculadorDistancia.class);//mockito
		buscadorTiendas = new BuscadorTiendas(repositorio, calculadorDistancia);
	}

	@Test
	void testBuscadorTiendas() {

	}

	@Test
	void testBuscarTiendasConStock() {
		fail("Not yet implemented");
	}

}
