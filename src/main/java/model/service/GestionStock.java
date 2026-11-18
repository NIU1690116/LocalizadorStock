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
		if (validador == null) {
			throw new IllegalArgumentException("Las dependencias de gestión de stock son obligatorias");
		}
		this.validador = validador;
		this.buscadorTiendas = buscadorTiendas;
		this.servicioReposicion = servicioReposicion;
		this.buscadorSimilares = buscadorSimilares;
		this.evaluador = evaluador;
		this.repositorioTiendas = repositorioTiendas;
	}

}
