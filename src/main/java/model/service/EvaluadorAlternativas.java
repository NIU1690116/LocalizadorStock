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
		if (alternativas == null) {
			throw new IllegalArgumentException("La lista de alternativas es obligatoria");
		}
		Alternativa mejorAlternativa = null;
	    double menorPuntuacion = Double.MAX_VALUE;

	    for (Alternativa alt : alternativas) {
	        // 1. Ignorar si es nula
	        if (alt == null) {
	            continue;
	        }
	        // 2. Comprobar si cumple las restricciones
	        if (!cumpleRestricciones(alt)) {
	        	continue;
	        }
	        // 3. Calcular su puntuación
	        double puntuacion = politica.calcularPuntuacion(alt);

	        // 4. Si es la primera válida, o tiene una puntuación menor (mejor)
	        if (mejorAlternativa == null || puntuacion < menorPuntuacion) {
	            mejorAlternativa = alt;
	            menorPuntuacion = puntuacion;
	        } 
	        // 5. Desempates si la puntuación es exactamente igual
	        else if (puntuacion == menorPuntuacion) {
	            
	            // Primer desempate: Menor tiempo estimado
	            if (alt.getTiempoEstimadoMinutos() < mejorAlternativa.getTiempoEstimadoMinutos()) {
	                mejorAlternativa = alt;
	            } 
	            // Segundo desempate: Menor distancia (si el tiempo también empata)
	            else if (alt.getTiempoEstimadoMinutos() == mejorAlternativa.getTiempoEstimadoMinutos()) {
	                if (alt.getDistanciaKm() < mejorAlternativa.getDistanciaKm()) {
	                    mejorAlternativa = alt;
	                }
	            }
	        }
	    }
	    
	    return mejorAlternativa;
	}
	
	private boolean cumpleRestricciones(Alternativa alternativa) {
		if (!restricciones.permiteDistancia(alternativa.getDistanciaKm())) {
			return false;
		}
		return !(alternativa instanceof AlternativaReposicion reposicion)
				|| restricciones.permiteEspera(reposicion.getTiempoEstimadoMinutos());
	}
}
