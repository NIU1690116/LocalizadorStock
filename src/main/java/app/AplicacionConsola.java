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

/**
 * Coordina la interacción por consola para realizar búsquedas de stock
 * Recibe las dependencias de entrada, salida, presentación, consulta y acceso
 * a tiendas, presenta las opciones, recoge los datos de la petición y delega
 * la búsqueda en el controlador. También ayuda a construir una configuración
 * para demostrar el funcionamiento con tiendas, inventarios y servicios de ejemplo
 */
public class AplicacionConsola {
	private final StockController controlador;
	private final VistaLocalizadorStock vista;
	private final RepositorioTiendas repositorioTiendas;
	private final Scanner entrada;
	private final PrintStream salida;
	private int siguienteIdPeticion = 1;

	/**
	 * Crea la aplicación con las dependencias necesarias para ejecutarla.
	 * @param controlador componente que consulta las alternativas de stock
	 * @param vista componente que presenta el resultado de la búsqueda
	 * @param repositorioTiendas fuente de las tiendas disponibles
	 * @param entrada flujo desde el que se leen las respuestas del usuario
	 * @param salida flujo en el que se muestran menús y mensajes
	 * @throws IllegalArgumentException si alguna dependencia es null
	 * Lógica: valida que ninguna dependencia falte y guarda todas para su uso
	 * durante la interacción.
	 */
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

	/**
	 * Ejecuta el menú principal hasta que el usuario elige salir o termina la entrada
	 * Lógica: muestra el encabezado y ofrece iniciar búsquedas o salir; para una
	 * opción válida de búsqueda invoca {@link #realizarBusqueda()}.
	 */
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

	/**
	 * Recoge los datos de una búsqueda, crea su petición y muestra el resultado
	 * Lógica: comprueba que haya tiendas y productos, solicita tienda, productos
	 * y cantidades, crea líneas de petición y delega la consulta y presentación.
	 * Si faltan datos disponibles o se termina la entrada, vuelve al menú
	 */
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
			Integer indiceProducto = leerEntero("Selecciona el número del producto: ", 1, productos.size());
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

	/**
	 * Construye un catálogo único con productos que tienen al menos una unidad
	 * @param tiendas tiendas cuyos inventarios se consultan
	 * @return productos sin duplicados, ordenados por código
	 * Lógica: recorre tiendas no nulas y añade el primer producto de cada código
	 * a un mapa ordenado; devuelve sus valores como lista.
	 */
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

	/**
	 * Imprime las tiendas numeradas con su nombre y coordenadas
	 * @param tiendas lista de tiendas que se muestra
	 * Lógica: recorre la lista y escribe cada tienda con un índice empezando en 1.
	 */
	private void mostrarTiendas(List<Tienda> tiendas) {
		salida.println("Tiendas disponibles:");
		for (int indice = 0; indice < tiendas.size(); indice++) {
			Tienda tienda = tiendas.get(indice);
			salida.printf(Locale.ROOT, "%d) %s (latitud %.4f, longitud %.4f)%n",
					indice + 1, tienda.getNombre(), tienda.getLatitud(), tienda.getLongitud());
		}
	}

	/**
	 * Imprime los productos numerados por su nombre
	 * @param productos lista de productos que se muestra
	 * Lógica: recorre la lista y escribe cada producto con un índice empezando en 1
	 */
	private void mostrarProductos(List<Producto> productos) {
		salida.println("Productos disponibles:");
		for (int indice = 0; indice < productos.size(); indice++) {
			Producto producto = productos.get(indice);
			salida.printf(Locale.ROOT, "%d) %s%n", indice + 1, producto.getNombre());
		}
	}

	/**
	 * Lee un entero comprendido entre los límites indicados
	 * @param mensaje texto que se muestra antes de cada lectura
	 * @param minimo valor mínimo aceptado, inclusive
	 * @param maximo valor máximo aceptado, inclusive
	 * @return el entero válido o null si se agota la entrada
	 * Lógica: repite la lectura ante texto no numérico o valores fuera del rango,
	 * mostrando el rango esperado
	 */
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
				// Se repite el aviso de entrada inválida y se vuelve a pedir el dato
			}
			salida.printf(Locale.ROOT, "Introduce un número entre %d y %d.%n", minimo, maximo);
		}
		return null;
	}

	/**
	 * Lee una respuesta afirmativa o negativa
	 * @param mensaje texto que se muestra antes de cada lectura
	 * @return true para s/si/sí, false para n/no o fin de entrada
	 * Lógica: normaliza la respuesta y repite mientras no reconozca una opción
	 */
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

	/**
	 * Punto de entrada del programa de consola.
	 * @param argumentos de línea de comandos
	 * Lógica: crea los flujos UTF-8, construye la demo y ejecuta su menú
	 */
	public static void main(String[] args) {
		Scanner entrada = new Scanner(System.in, java.nio.charset.StandardCharsets.UTF_8);
		PrintStream salida = new PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8);
		crearDemo(entrada, salida).ejecutar();
	}

	/**
	 * Construye una aplicación completa con datos y servicios de demostración
	 * @param entrada flujo desde el que se leerán las respuestas
	 * @param salida flujo donde se mostrarán los mensajes
	 * @return aplicación configurada para ejecutarse
	 * Lógica: crea tiendas e inventarios de ejemplo, configura repositorio,
	 * distancia, tiempo y reposición, ensambla los servicios y devuelve la
	 * aplicación con su controlador y vista
	 */
	public static AplicacionConsola crearDemo(Scanner entrada, PrintStream salida) {
		List<Tienda> tiendas = crearTiendasDemo();
		RepositorioTiendas repositorio = () -> tiendas;
		CalculadorDistanciaGeografica calculadorDistancia = new CalculadorDistanciaGeografica();
		EstimadorTiempoViaje estimadorTiempo = (origen, destino) -> {
			double distanciaKm = calculadorDistancia.calcularKm(origen, destino);
			return (int) Math.ceil(distanciaKm * 2.0);
		};
		ProveedorReposicion proveedorReposicion = (tienda, producto) -> {
			if (tienda.getId().equals("T001")
					&& (producto.getCodigo().equals("P001")
							|| producto.getCodigo().equals("P002"))) {
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
		return new AplicacionConsola(
				controlador, new VistaLocalizadorStock(salida), repositorio, entrada, salida);
	}

	/**
	 * Genera las tiendas e inventarios usados por la configuración de demostración
	 * @return lista de tiendas de ejemplo
	 * Lógica: crea productos, asigna existencias a varios inventarios y construye
	 * cuatro tiendas con identificadores y coordenadas predefinidos
	 */
	private static List<Tienda> crearTiendasDemo() {
		Producto camiseta = new Producto("P001", "Camiseta básica", "Rojo", "Camiseta", "Unisex");
		Producto pantalon = new Producto("P002", "Pantalón de vestir", "Negro", "Pantalón", "Hombre");
		Producto chaquetaRoja = new Producto("P003", "Chaqueta roja", "Rojo", "Chaqueta", "Mujer");
		Producto chaquetaAzul = new Producto("P004", "Chaqueta azul", "Azul", "Chaqueta", "Mujer");
		Producto botas = new Producto("P005", "Botas de montaña", "Verde", "Botas", "Infantil");

		Inventario inventarioCentro = new Inventario();
		inventarioCentro.añadirProductos(camiseta, 1);
		inventarioCentro.añadirProductos(pantalon, 1);
		Inventario inventarioNorte = new Inventario();
		inventarioNorte.añadirProductos(camiseta, 5);
		Inventario inventarioSur = new Inventario();
		inventarioSur.añadirProductos(chaquetaAzul, 5);
		Inventario inventarioLejano = new Inventario();
		inventarioLejano.añadirProductos(chaquetaRoja, 2);
		inventarioLejano.añadirProductos(botas, 2);

		return List.of(
				new Tienda("T001", "Centro", 41.3870, 2.1700, inventarioCentro),
				new Tienda("T002", "Norte", 41.3970, 2.1700, inventarioNorte),
				new Tienda("T003", "Sur", 41.3770, 2.1700, inventarioSur),
				new Tienda("T004", "Far", 41.5500, 2.1700, inventarioLejano));
	}
}
