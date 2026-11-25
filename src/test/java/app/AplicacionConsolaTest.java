package app;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import controller.StockController;
import model.distance.CalculadorDistanciaGeografica;
import model.distance.EstimadorTiempoViaje;
import model.domain.Inventario;
import model.domain.Producto;
import model.domain.RestriccionesBusqueda;
import model.domain.Tienda;
import model.repository.ProveedorReposicion;
import model.repository.RepositorioTiendas;
import model.service.BuscadorProductosSimilares;
import model.service.BuscadorTiendas;
import model.service.EvaluadorAlternativas;
import model.service.GestionStock;
import model.service.PoliticaBusqueda;
import model.service.ServicioReposicion;
import model.service.ValidadorPeticion;
import view.VistaLocalizadorStock;

/**
 * Pruebas de integración de la aplicación de consola (test de consola para cubrir statement coverage)
 * Comprueba la validación de dependencias, el flujo de entrada y salida, la
 * creación de la demo y la coordinación con controlador y vista mediante datos
 * de ejemplo 
 */
class AplicacionConsolaTest {
	//Verifica que el constructor rechaza las dependencias obligatorias nulas.
	@Test
	void rechazaDependenciasNulas() {
		// Statement coverage: ejecuta la excepción del constructor ante dependencias obligatorias nulas.
		assertThrows(IllegalArgumentException.class,
				() -> new AplicacionConsola(null, null, null, null, null));
	}

	//Comprueba que el menú termina limpiamente cuando la entrada se agota
	@Test
	void salirConEntradaAgotadaEnElMenu() {
		// Statement coverage: cubre la salida del menú cuando no hay otra línea que leer.
		ByteArrayOutputStream salida = new ByteArrayOutputStream();
		crearAplicacion("", salida).ejecutar();

		assertTrue(salida.toString(StandardCharsets.UTF_8).contains("LOCALIZADOR DE STOCK"));
	}

	//Verifica los mensajes cuando no hay tiendas o productos disponibles
	@Test
	void informaCuandoNoHayTiendasNiProductosYOmiteTiendasNulas() {
		// Statement coverage: cubre tiendas vacías y el continue del catálogo al encontrar null.
		ByteArrayOutputStream sinTiendas = new ByteArrayOutputStream();
		crearAplicacion("1\n0\n", sinTiendas, List.of()).ejecutar();
		assertTrue(sinTiendas.toString(StandardCharsets.UTF_8).contains("No hay tiendas disponibles"));

		Tienda sinProductos = new Tienda("T002", "Vacía", 41.0, 2.0, new Inventario());
		ByteArrayOutputStream sinCatalogo = new ByteArrayOutputStream();
		crearAplicacion("1\n0\n", sinCatalogo, java.util.Arrays.asList(null, sinProductos)).ejecutar();
		assertTrue(sinCatalogo.toString(StandardCharsets.UTF_8).contains("No hay productos disponibles"));
	}

	//Comprueba que cada selección finaliza la búsqueda si se agota la entrada
	@Test
	void finalizaLaBusquedaSiSeAgotaLaEntradaEnCadaSeleccion() {
		// Statement coverage: ejecuta las tres salidas tempranas por EOF en tienda, producto y cantidad.
		Producto producto = new Producto("P001", "Camisa", "Rojo", "Camisa", "Unisex");
		Inventario inventario = new Inventario();
		inventario.añadirProductos(producto, 2);
		Tienda tienda = new Tienda("T001", "Centro", 41.0, 2.0, inventario);
		List<Tienda> tiendas = List.of(tienda);

		crearAplicacion("1\n", new ByteArrayOutputStream(), tiendas).ejecutar();
		crearAplicacion("1\n1\n", new ByteArrayOutputStream(), tiendas).ejecutar();
		crearAplicacion("1\n1\n1\n", new ByteArrayOutputStream(), tiendas).ejecutar();
	}

	//Comprueba el tratamiento de una respuesta afirmativa y el fin de entrada posterior
	@Test
	void procesaRespuestaAfirmativaYRespuestaFinalAgotada() {
		// Statement coverage: cubre el true de leerSiNo y el false devuelto al agotarse la entrada.
		ByteArrayOutputStream salida = new ByteArrayOutputStream();
		crearAplicacion("1\n1\n1\n1\nsi\n1\n1\n1\n", salida).ejecutar();

		assertTrue(salida.toString(StandardCharsets.UTF_8).contains("Camiseta básica x 1"));
	}

	//Verifica que el método main construye y ejecuta la aplicación
	@Test
	void mainCreaYEjecutaLaAplicacionConLasEntradasDelSistema() {
		// Statement coverage: ejecuta el punto de entrada main con sus streams estándar.
		var entradaOriginal = System.in;
		var salidaOriginal = System.out;
		ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();
		try {
			System.setIn(new ByteArrayInputStream("0\n".getBytes(StandardCharsets.UTF_8)));
			System.setOut(new PrintStream(salidaCapturada, true, StandardCharsets.UTF_8));
			AplicacionConsola.main(new String[0]);
		} finally {
			System.setIn(entradaOriginal);
			System.setOut(salidaOriginal);
		}
		assertTrue(salidaCapturada.toString(StandardCharsets.UTF_8).contains("Hasta pronto."));
	}

	//Comprueba que se puede seleccionar una tienda, producto y cantidad
	@Test
	void permiteElegirTiendaProductoYCantidadYVerElResultado() {
		// Loop testing (bucle anidado 2): una tienda y un producto en el catálogo.
		ByteArrayOutputStream salida = new ByteArrayOutputStream();
		AplicacionConsola aplicacion = crearAplicacion("1\n1\n1\n3\nn\n0\n", salida);

		aplicacion.ejecutar();

		String texto = salida.toString(StandardCharsets.UTF_8);
		assertTrue(texto.contains("LOCALIZADOR DE STOCK"));
		assertTrue(texto.contains("1) Nueva búsqueda"));
		assertTrue(texto.contains("Tienda actual"));
		assertTrue(texto.contains("Centro (latitud 41.0000, longitud 2.0000)"));
		assertTrue(texto.contains("Camiseta básica"));
		assertTrue(texto.contains("Selecciona el número del producto:"));
		assertTrue(texto.contains("Camiseta básica x 3: resuelta en la tienda actual."));
	}

	/**
	 * Verifica los casos de stock local, otra tienda, reposición, producto similar
	 * y ausencia de solución que ofrece la configuración demo
	 */
	@Test
	void laDemoPermiteProbarStockReposicionSimilarYSinSolucion() {
		// Loop testing (bucle anidado 2): varias tiendas, cada una con varios productos disponibles.
		String entradas = String.join("\n",
				"1", "1", "1", "1", "n",
				"1", "1", "1", "3", "n",
				"1", "1", "2", "3", "n",
				"1", "1", "3", "1", "n",
				"1", "1", "5", "1", "n",
				"0", "");
		ByteArrayOutputStream salidaBytes = new ByteArrayOutputStream();
		PrintStream salida = new PrintStream(salidaBytes, true, StandardCharsets.UTF_8);
		AplicacionConsola aplicacion = AplicacionConsola.crearDemo(
				new Scanner(new ByteArrayInputStream(entradas.getBytes(StandardCharsets.UTF_8))), salida);

		aplicacion.ejecutar();

		String texto = salidaBytes.toString(StandardCharsets.UTF_8);
		assertTrue(texto.contains("Centro (latitud 41.3870, longitud 2.1700)"));
		assertTrue(texto.contains("Norte (latitud 41.3970, longitud 2.1700)"));
		assertTrue(texto.contains("Sur (latitud 41.3770, longitud 2.1700)"));
		assertTrue(texto.contains("Far (latitud 41.5500, longitud 2.1700)"));
		assertTrue(texto.contains("Camiseta básica x 1: resuelta en la tienda actual."));
		assertTrue(texto.contains("Camiseta básica x 3: recoger en Norte"));
		assertTrue(texto.contains("Pantalón de vestir x 3: esperar reposición en Centro"));
		assertTrue(texto.contains("Chaqueta roja x 1: producto similar: Chaqueta azul en Sur"));
		assertTrue(texto.contains("Botas de montaña x 1: no se encontró una solución."));
	}

	// Comprueba que las entradas numéricas y de sí/no inválidas se vuelven a pedir
	@Test
	void vuelveAPedirDatosCuandoLaEntradaNoEsValida() {
		ByteArrayOutputStream salida = new ByteArrayOutputStream();
		AplicacionConsola aplicacion = crearAplicacion(
				"1\n0\n1\ntexto\n1\n0\n2\nquizas\nn\n0\n", salida);

		aplicacion.ejecutar();

		String texto = salida.toString(StandardCharsets.UTF_8);
		assertTrue(texto.contains("Introduce un número entre 1 y 1."));
		assertTrue(texto.contains("Introduce un número entre 1 y 2147483647."));
		assertTrue(texto.contains("Responde s para sí o n para no."));
		assertTrue(texto.contains("Camiseta básica x 2: resuelta en la tienda actual."));
	}

	//Crea una aplicación de prueba con una tienda que tiene stock de camiseta
	private AplicacionConsola crearAplicacion(String textoEntrada, ByteArrayOutputStream salidaBytes) {
		Producto camiseta = new Producto("P001", "Camiseta básica", "Rojo", "Camiseta", "Unisex");
		Inventario inventario = new Inventario();
		inventario.añadirProductos(camiseta, 5);
		Tienda tienda = new Tienda("T001", "Centro", 41.0, 2.0, inventario);
		return crearAplicacion(textoEntrada, salidaBytes, List.of(tienda));
	}

	//Construye una aplicación de prueba con las tiendas recibidas y dependencias reales o simples
	private AplicacionConsola crearAplicacion(
			String textoEntrada, ByteArrayOutputStream salidaBytes, List<Tienda> tiendas) {
		RepositorioTiendas repositorio = () -> tiendas;

		CalculadorDistanciaGeografica calculador = new CalculadorDistanciaGeografica();
		EstimadorTiempoViaje estimador = (origen, destino) -> 0;
		RestriccionesBusqueda restricciones = new RestriccionesBusqueda();
		ProveedorReposicion proveedor = (tiendaConsulta, productoConsulta) -> null;
		GestionStock gestionStock = new GestionStock(
				new ValidadorPeticion(),
				new BuscadorTiendas(repositorio, calculador, estimador),
				new ServicioReposicion(proveedor),
				new BuscadorProductosSimilares(calculador, estimador),
				new EvaluadorAlternativas(new PoliticaBusqueda(), restricciones),
				repositorio);
		StockController controlador = new StockController(gestionStock);
		PrintStream salida = new PrintStream(salidaBytes, true, StandardCharsets.UTF_8);
		return new AplicacionConsola(
				controlador,
				new VistaLocalizadorStock(salida),
				repositorio,
				new Scanner(new ByteArrayInputStream(textoEntrada.getBytes(StandardCharsets.UTF_8))),
				salida);
	}
}
