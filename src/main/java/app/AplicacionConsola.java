package app;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import controller.StockController;
import model.distance.CalculadorDistanciaGeografica;
import model.distance.EstimadorTiempoViaje;
import model.domain.Inventario;
import model.domain.Peticion;
import model.domain.Producto;
import model.domain.Reposicion;
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

public class AplicacionConsola {
	private final StockController controlador;
	private final VistaLocalizadorStock vista;
	private final RepositorioTiendas repositorioTiendas;
	private final Scanner entrada;
	private final PrintStream salida;
	private int siguienteIdPeticion = 1;

	public AplicacionConsola(
			StockController controlador,
			VistaLocalizadorStock vista,
			RepositorioTiendas repositorioTiendas,
			Scanner entrada,
			PrintStream salida) {
		if (controlador == null || vista == null || repositorioTiendas == null
				|| entrada == null || salida == null) {
			throw new IllegalArgumentException("Las dependencias de la aplicación son obligatorias");
		}
		this.controlador = controlador;
		this.vista = vista;
		this.repositorioTiendas = repositorioTiendas;
		this.entrada = entrada;
		this.salida = salida;
	}

	public void ejecutar() {
		salida.println("LOCALIZADOR DE STOCK");
		salida.println("Demo con datos de ejemplo y tiempos de viaje aproximados.");
		while (true) {
			salida.println();
			salida.println("1) Nueva búsqueda");
			salida.println("0) Salir");
			Integer opcion = leerEntero("Elige una opción: ", 0, 1);
			if (opcion == null || opcion == 0) {
				salida.println("Hasta pronto.");
				return;
			}
			realizarBusqueda();
		}
	}

	private void realizarBusqueda() {
		List<Tienda> tiendas = repositorioTiendas.obtenerTiendas();
		if (tiendas == null || tiendas.isEmpty()) {
			salida.println("No hay tiendas disponibles para buscar.");
			return;
		}
		List<Producto> productos = obtenerCatalogo(tiendas);
		if (productos.isEmpty()) {
			salida.println("No hay productos disponibles en el catálogo.");
			return;
		}

		mostrarTiendas(tiendas);
		Integer indiceTienda = leerEntero("Tienda actual: ", 1, tiendas.size());
		if (indiceTienda == null) {
			return;
		}
		Tienda tiendaActual = tiendas.get(indiceTienda - 1);

		Peticion peticion = new Peticion(
				"REQ-" + siguienteIdPeticion++, LocalDateTime.now());
		boolean agregarOtraLinea;
		do {
			mostrarProductos(productos);
			Integer indiceProducto = leerEntero("Producto: ", 1, productos.size());
			if (indiceProducto == null) {
				return;
			}
			Integer cantidad = leerEntero("Cantidad (mayor que cero): ", 1, Integer.MAX_VALUE);
			if (cantidad == null) {
				return;
			}
			peticion.agregarLinea(new model.domain.LineaPeticion(
					productos.get(indiceProducto - 1), cantidad));
			agregarOtraLinea = leerSiNo("¿Añadir otro producto? (s/n): ");
		} while (agregarOtraLinea);

		vista.mostrarResultado(controlador.consultar(
				peticion, tiendaActual, new RestriccionesBusqueda()));
	}

	private List<Producto> obtenerCatalogo(List<Tienda> tiendas) {
		Map<String, Producto> productosPorCodigo = new TreeMap<>();
		for (Tienda tienda : tiendas) {
			if (tienda == null) {
				continue;
			}
			for (Producto producto : tienda.getInventario().productosConStockSuficiente(1)) {
				productosPorCodigo.putIfAbsent(producto.getCodigo(), producto);
			}
		}
		return new ArrayList<>(productosPorCodigo.values());
	}

	private void mostrarTiendas(List<Tienda> tiendas) {
		salida.println("Tiendas disponibles:");
		for (int indice = 0; indice < tiendas.size(); indice++) {
			salida.printf(Locale.ROOT, "%d) %s%n", indice + 1, tiendas.get(indice).getNombre());
		}
	}

	private void mostrarProductos(List<Producto> productos) {
		salida.println("Productos disponibles:");
		for (int indice = 0; indice < productos.size(); indice++) {
			Producto producto = productos.get(indice);
			salida.printf(Locale.ROOT, "%d) %s%n", indice + 1, producto.getNombre());
		}
	}

	private Integer leerEntero(String mensaje, int minimo, int maximo) {
		while (entrada.hasNextLine()) {
			salida.print(mensaje);
			String texto = entrada.nextLine().trim();
			try {
				int numero = Integer.parseInt(texto);
				if (numero >= minimo && numero <= maximo) {
					return numero;
				}
			} catch (NumberFormatException excepcion) {
				// Se repite el aviso de entrada inválida y se vuelve a pedir el dato.
			}
			salida.printf(Locale.ROOT, "Introduce un número entre %d y %d.%n", minimo, maximo);
		}
		return null;
	}

	private boolean leerSiNo(String mensaje) {
		while (entrada.hasNextLine()) {
			salida.print(mensaje);
			String respuesta = entrada.nextLine().trim().toLowerCase(Locale.ROOT);
			if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
				return true;
			}
			if (respuesta.equals("n") || respuesta.equals("no")) {
				return false;
			}
			salida.println("Responde s para sí o n para no.");
		}
		return false;
	}

	public static void main(String[] args) {
		List<Tienda> tiendas = crearTiendasDemo();
		RepositorioTiendas repositorio = () -> tiendas;
		CalculadorDistanciaGeografica calculadorDistancia = new CalculadorDistanciaGeografica();
		EstimadorTiempoViaje estimadorTiempo = (origen, destino) -> {
			double distanciaKm = calculadorDistancia.calcularKm(origen, destino);
			return (int) Math.ceil(distanciaKm * 2.0);
		};
		ProveedorReposicion proveedorReposicion = (tienda, producto) -> {
			if (tienda.getId().equals("T001") && producto.getCodigo().equals("P001")) {
				return new Reposicion(producto, 2, LocalDateTime.now().plusMinutes(45));
			}
			return null;
		};
		RestriccionesBusqueda restricciones = new RestriccionesBusqueda();
		GestionStock gestionStock = new GestionStock(
				new ValidadorPeticion(),
				new BuscadorTiendas(repositorio, calculadorDistancia, estimadorTiempo),
				new ServicioReposicion(proveedorReposicion),
				new BuscadorProductosSimilares(calculadorDistancia, estimadorTiempo),
				new EvaluadorAlternativas(new PoliticaBusqueda(), restricciones),
				repositorio);
		StockController controlador = new StockController(gestionStock);
		Scanner entrada = new Scanner(System.in, java.nio.charset.StandardCharsets.UTF_8);
		PrintStream salida = new PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8);
		AplicacionConsola aplicacion = new AplicacionConsola(
				controlador, new VistaLocalizadorStock(salida), repositorio, entrada, salida);
		aplicacion.ejecutar();
	}

	private static List<Tienda> crearTiendasDemo() {
		Producto camiseta = new Producto("P001", "Camiseta básica", "Rojo", "Camiseta", "Unisex");
		Producto camisetaAzul = new Producto("P002", "Camiseta azul", "Azul", "Camiseta", "Unisex");

		Inventario inventarioCentro = new Inventario();
		inventarioCentro.añadirProductos(camiseta, 1);
		Inventario inventarioNorte = new Inventario();
		inventarioNorte.añadirProductos(camiseta, 5);
		Inventario inventarioSur = new Inventario();
		inventarioSur.añadirProductos(camisetaAzul, 5);

		return List.of(
				new Tienda("T001", "Centro", 41.3870, 2.1700, inventarioCentro),
				new Tienda("T002", "Norte", 41.3970, 2.1700, inventarioNorte),
				new Tienda("T003", "Sur", 41.3770, 2.1700, inventarioSur));
	}
}
