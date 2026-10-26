<<<<<<< HEAD
package test.java.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.model.BuscadorTiendas;
import main.java.model.CalculadorDistancia;
import main.java.model.Inventario;
import main.java.model.LineaPeticion;
import main.java.model.Producto;
import main.java.model.RepositorioTiendas;
import main.java.model.RestriccionesBusqueda;
import main.java.model.Tienda;
=======
package model;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import model.BuscadorTiendas;
import model.CalculadorDistancia;
import model.Inventario;
import model.LineaPeticion;
import model.Producto;
import model.RepositorioTiendas;
import model.RestriccionesBusqueda;
import model.Tienda;
>>>>>>> 1ffe5f9695c990272a0450b5a61117523034bce9

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
<<<<<<< HEAD
		Tienda origen = new Tienda(
		        "T1", "Tienda cercana", 41.4, 2.1, inventario);
		inventario = new Inventario();
		inventario.añadirProductos(producto, 3);
=======
		inventario = new Inventario();
		inventario.añadirProductos(producto, 3);
		Tienda origen = new Tienda(
		        "T001", "Tienda cercana", 41.4, 2.1, inventario);
>>>>>>> 1ffe5f9695c990272a0450b5a61117523034bce9
		
		linea = new LineaPeticion(producto, 3);
        restricciones = new RestriccionesBusqueda();

		repositorio = new RepositorioTiendasMock();
<<<<<<< HEAD
=======
		calculadorDistancia = mock(CalculadorDistancia.class);//mockito
>>>>>>> 1ffe5f9695c990272a0450b5a61117523034bce9
		buscadorTiendas = new BuscadorTiendas(repositorio, calculadorDistancia);
	}

	@Test
	void testBuscadorTiendas() {

	}

	@Test
	void testBuscarTiendasConStock() {
<<<<<<< HEAD
		fail("Not yet implemented");
=======
		
>>>>>>> 1ffe5f9695c990272a0450b5a61117523034bce9
	}

}
