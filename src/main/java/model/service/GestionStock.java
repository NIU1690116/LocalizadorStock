package model.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import model.domain.Alternativa;
import model.domain.AlternativaOtraTienda;
import model.domain.AlternativaProductoSimilar;
import model.domain.AlternativaReposicion;
import model.domain.EstadoResolucion;
import model.domain.LineaPeticion;
import model.domain.Peticion;
import model.domain.Reposicion;
import model.domain.RestriccionesBusqueda;
import model.domain.ResultadoBusqueda;
import model.domain.ResultadoLineaPeticion;
import model.domain.Tienda;
import model.repository.RepositorioTiendas;

/**
 * Coordina la resolución de cada línea de una petición de stock
 * Comprueba la petición, usa el stock de la tienda de origen, cuando no es suficiente
 * reúne alternativas de otras tiendas o de reposición, si no encuentra ninguna,
 * busca productos similares. Finalmente devuelve un resultado por línea petición
 */
public class GestionStock {
	private final ValidadorPeticion validador;
	private final BuscadorTiendas buscadorTiendas;
	private final ServicioReposicion servicioReposicion;
	private final BuscadorProductosSimilares buscadorSimilares;
	private final EvaluadorAlternativas evaluador;
	private final RepositorioTiendas repositorioTiendas;

	public GestionStock(
			ValidadorPeticion validador,
			BuscadorTiendas buscadorTiendas,
			ServicioReposicion servicioReposicion,
			BuscadorProductosSimilares buscadorSimilares,
			EvaluadorAlternativas evaluador,
			RepositorioTiendas repositorioTiendas) {
		if (validador == null || buscadorTiendas == null || servicioReposicion == null
				|| buscadorSimilares == null || evaluador == null || repositorioTiendas == null) {
			throw new IllegalArgumentException("Las dependencias de gestión de stock son obligatorias");
		}
		this.validador = validador;
		this.buscadorTiendas = buscadorTiendas;
		this.servicioReposicion = servicioReposicion;
		this.buscadorSimilares = buscadorSimilares;
		this.evaluador = evaluador;
		this.repositorioTiendas = repositorioTiendas;
	}

	/**
	 * Resuelve todas las líneas de una petición desde una tienda de origen
	 * @param peticion petición con productos y cantidades solicitados
	 * @param origen tienda en la que se consulta primero el stock
	 * @param restricciones límites que deben respetar las alternativas
	 * @return resultado que contiene la resolución de cada línea de la petición
	 * @throws IllegalArgumentException si algún argumento es null o la
	 * petición no supera la validación
	 * Lógica: valida los argumentos y la petición, después resuelve cada línea y
	 * agrupa sus resultados en un ResultadoBusqueda
	 */
	public ResultadoBusqueda buscarAlternativas(Peticion peticion,
			Tienda origen,RestriccionesBusqueda restricciones) {
		if (peticion == null || origen == null || restricciones == null) {
	        throw new IllegalArgumentException("La petición, la tienda y las restricciones son obligatorias");
	    }
		if (!validador.validarPeticion(peticion)) {
	        throw new IllegalArgumentException("La petición no es válida");
	    }
		
		List<ResultadoLineaPeticion> resultados = new ArrayList<>();
	    for (LineaPeticion linea : peticion.getLineas()) {
	        resultados.add(resolverLinea(peticion, origen, linea, restricciones));
	    }
	    return new ResultadoBusqueda(resultados);
	}
	
	/**
	 * Determina cómo resolver una línea de petición concreta
	 * @param peticion petición completa a la que pertenece la línea
	 * @param origen tienda cuyo stock se consulta inicialmente
	 * @param linea línea de producto y cantidad que se quiere resolver
	 * @param restricciones límites aplicables a las alternativas
	 * @return resultado de la línea, marcado como resuelto, con alternativa o sin solución
	 * Lógica: si el stock de origen es suficiente, la marca resuelta, si no busca alternativas
	 * de stock y selecciona la mejor. Si no las hay, busca productos similares, si
	 * tampoco encuentra, marca la línea sin solución
	 */
	private ResultadoLineaPeticion resolverLinea(
			Peticion peticion,
			Tienda origen,
			LineaPeticion linea,
			RestriccionesBusqueda restricciones) {
		int stockActual = origen.consultarStock(linea.getProducto());
		if (stockActual >= linea.getCantidad()) {
			return new ResultadoLineaPeticion(linea, List.of(), null, EstadoResolucion.RESUELTA);
		}

		List<Alternativa> alternativas = buscarAlternativasDeStock(
				peticion, origen, linea, stockActual, restricciones);
		Alternativa mejor = alternativas.isEmpty() ? null : evaluador.seleccionarMejor(alternativas);
		if (mejor != null && alternativas.contains(mejor)) {
			return new ResultadoLineaPeticion(
					linea, alternativas, mejor, EstadoResolucion.ALTERNATIVA_PROPUESTA);
		}

		List<AlternativaProductoSimilar> similares = buscarSimilares(origen, linea, restricciones);
		if (!similares.isEmpty()) {
			List<Alternativa> alternativasSimilares = new ArrayList<>(similares);
			return new ResultadoLineaPeticion(
					linea, alternativasSimilares, similares.get(0),
					EstadoResolucion.ALTERNATIVA_PROPUESTA);
		}
		return new ResultadoLineaPeticion(linea, List.of(), null, EstadoResolucion.SIN_SOLUCION);
	}
	
	/**
	 * Reúne alternativas por disponibilidad en otra tienda o por reposición
	 * @param peticion petición original, usada para conservar su identificador y fecha
	 * @param origen tienda cuyo stock se consultó y donde se consulta la reposición
	 * @param linea línea que se intenta cubrir
	 * @param stockActual unidades disponibles en la tienda de origen
	 * @param restricciones límites aplicables a las tiendas y a la reposición
	 * @return alternativas válidas encontradas, puede ser una lista vacía
	 * Lógica: convierte las tiendas candidatas con stock en alternativas, calcula
	 * las unidades faltantes, si hay, consulta y valida una reposición para esa
	 * cantidad antes de agregarla
	 */
	private List<Alternativa> buscarAlternativasDeStock(
			Peticion peticion, Tienda origen, LineaPeticion linea, int stockActual,
			RestriccionesBusqueda restricciones) {
		List<Alternativa> alternativas = new ArrayList<>();
		List<BuscadorTiendas.TiendaConDistancia> tiendas =
				buscadorTiendas.buscarTiendasConStock(origen, linea, restricciones);
		if (tiendas != null) {
			for (BuscadorTiendas.TiendaConDistancia candidata : tiendas) {
				if (candidata != null && candidata.tienda() != null) {
					alternativas.add(new AlternativaOtraTienda(
							candidata.tienda(), candidata.tiempoEstimadoMinutos(),
							candidata.distancia()));
				}
			}
		}

		int cantidadFaltante = linea.getCantidad() - stockActual;
		if (cantidadFaltante > 0) {
			Reposicion reposicion = servicioReposicion.consultarReposicion(origen, linea.getProducto());
			if (reposicion != null) {
				Peticion peticionFaltante = new Peticion(
						peticion.getId(), peticion.getFechaSolicitud());
				LineaPeticion lineaFaltante = new LineaPeticion(linea.getProducto(), cantidadFaltante);
				peticionFaltante.agregarLinea(lineaFaltante);
				if (validador.validarReposicion(
						peticionFaltante, lineaFaltante, reposicion, restricciones)) {
					int minutosEspera = Math.toIntExact(Duration.between(
							peticion.getFechaSolicitud(),
							reposicion.getFechaPrevista()).toMinutes());
					alternativas.add(new AlternativaReposicion(
							origen, reposicion, minutosEspera, 0.0));
				}
			}
		}
		return alternativas;
	}
	
	/**
	 * Busca productos similares y descarta los que incumplen la distancia máxima
	 * @param origen tienda desde la que se evalúan los productos similares
	 * @param linea línea cuyo producto se quiere sustituir por uno similar
	 * @param restricciones restricciones usadas para filtrar por distancia
	 * @return alternativas similares válidas, o lista vacía si no hay tiendas o resultados
	 * Lógica: obtiene las tiendas del repositorio, llama al buscador de similares y
	 * elimina resultados nulos y alternativas cuya distancia no sea valida
	 */
	private List<AlternativaProductoSimilar> buscarSimilares(Tienda origen,
			LineaPeticion linea, RestriccionesBusqueda restricciones) {
		List<Tienda> tiendas = repositorioTiendas.obtenerTiendas();
		if (tiendas == null || tiendas.isEmpty()) {
			return List.of();
		}
		List<AlternativaProductoSimilar> encontradas =
				buscadorSimilares.buscarSimilares(linea, origen, tiendas);
		if (encontradas == null) {
			return List.of();
		}
		return encontradas.stream()
				.filter(alternativa -> alternativa != null)
				.filter(alternativa -> restricciones.permiteDistancia(alternativa.getDistanciaKm()))
				.toList();
	}
	
}
