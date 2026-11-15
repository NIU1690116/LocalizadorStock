package model.service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import model.domain.Alternativa;
import model.domain.AlternativaReposicion;
import model.domain.RestriccionesBusqueda;

public class EvaluadorAlternativas {
	private final PoliticaBusqueda politica;
	private final RestriccionesBusqueda restricciones;

	public EvaluadorAlternativas(PoliticaBusqueda politica, RestriccionesBusqueda restricciones) {
		if (politica == null) {
			throw new IllegalArgumentException("La política de búsqueda es obligatoria");
		}
		if (restricciones == null) {
			throw new IllegalArgumentException("Las restricciones son obligatorias");
		}
		this.politica = politica;
		this.restricciones = restricciones;
	}
	
	public Alternativa seleccionarMejor(List<Alternativa> alternativas) {
		return null;
		}
	}
}
