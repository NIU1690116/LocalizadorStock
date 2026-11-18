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
		return null;
	}
	
	private List<Alternativa> buscarAlternativasDeStock(
			Peticion peticion, Tienda origen, LineaPeticion linea, int stockActual, RestriccionesBusqueda restricciones) {
		List<Alternativa> alternativas = new ArrayList<>();
		List<BuscadorTiendas.TiendaConDistancia> tiendas =
				buscadorTiendas.buscarTiendasConStock(origen, linea, restricciones);
		if (tiendas != null) {
			for (BuscadorTiendas.TiendaConDistancia candidata : tiendas) {
				if (candidata != null && candidata.tienda() != null) {
					alternativas.add(new AlternativaOtraTienda(
							candidata.tienda(), candidata.tiempoEstimadoMinutos(), candidata.distancia()));
				}
			}
		}

		int cantidadFaltante = linea.getCantidad() - stockActual;
		if (cantidadFaltante > 0) {
			Reposicion reposicion = servicioReposicion.consultarReposicion(origen, linea.getProducto());
			if (reposicion != null) {
				Peticion peticionFaltante = new Peticion(peticion.getId(), peticion.getFechaSolicitud());
				LineaPeticion lineaFaltante = new LineaPeticion(linea.getProducto(), cantidadFaltante);
				peticionFaltante.agregarLinea(lineaFaltante);
				if (validador.validarReposicion(
						peticionFaltante, lineaFaltante, reposicion, restricciones)) {
					int minutosEspera = Math.toIntExact(Duration.between(
							peticion.getFechaSolicitud(), reposicion.getFechaPrevista()).toMinutes());
					alternativas.add(new AlternativaReposicion(origen, reposicion, minutosEspera, 0.0));
				}
			}
		}
		return alternativas;
	}
	
	private List<AlternativaProductoSimilar> buscarSimilares(
			Tienda origen,
			LineaPeticion linea,
			RestriccionesBusqueda restricciones) {
return null;
	}
	
	
	
	
}
