package model.service;

import java.util.List;
import java.util.Objects;

import model.domain.Alternativa;
import model.domain.AlternativaReposicion;
import model.domain.RestriccionesBusqueda;

public class EvaluadorAlternativas {
	private final PoliticaBusqueda politica;
	private final RestriccionesBusqueda restricciones;

	public EvaluadorAlternativas(PoliticaBusqueda politica, RestriccionesBusqueda restricciones) {
		// Precondiciones: se necesitan una política y unas restricciones válidas.
		if (politica == null) {
			throw new IllegalArgumentException("La política de búsqueda es obligatoria");
		}
		if (restricciones == null) {
			throw new IllegalArgumentException("Las restricciones son obligatorias");
		}
		this.politica = politica;
		this.restricciones = restricciones;
		// Postcondición: el evaluador queda listo para aplicar ambas dependencias.
		assert invariant() : "El evaluador debe tener política y restricciones";
	}

	private boolean invariant() {
		return politica != null && restricciones != null;
	}
	
	public Alternativa seleccionarMejor(List<Alternativa> alternativas) {
		// Precondición: la lista debe existir; sus elementos nulos se ignoran.
		if (alternativas == null) {
			throw new IllegalArgumentException("La lista de alternativas es obligatoria");
		}
		assert invariant() : "El evaluador debe conservar sus dependencias";
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
	    // Postcondición: devuelve la candidata válida mejor puntuada, o null si no hay ninguna válida.
	    assert cumplePostcondicion(alternativas, mejorAlternativa)
		    : "La alternativa seleccionada debe ser la mejor candidata válida";
	    return mejorAlternativa;
	}

	private boolean cumplePostcondicion(List<Alternativa> alternativas, Alternativa seleccionada) {
		if (seleccionada == null) {
			return alternativas.stream()
					.filter(Objects::nonNull)
					.noneMatch(this::cumpleRestricciones);
		}
		if (!alternativas.contains(seleccionada) || !cumpleRestricciones(seleccionada)) {
			return false;
		}

		double puntuacionSeleccionada = politica.calcularPuntuacion(seleccionada);
		for (Alternativa candidata : alternativas) {
			if (candidata == null || !cumpleRestricciones(candidata)) {
				continue;
			}
			double puntuacionCandidata = politica.calcularPuntuacion(candidata);
			if (puntuacionCandidata < puntuacionSeleccionada) {
				return false;
			}
			if (puntuacionCandidata == puntuacionSeleccionada
					&& candidata.getTiempoEstimadoMinutos()
							< seleccionada.getTiempoEstimadoMinutos()) {
				return false;
			}
			if (puntuacionCandidata == puntuacionSeleccionada
					&& candidata.getTiempoEstimadoMinutos()
							== seleccionada.getTiempoEstimadoMinutos()
					&& candidata.getDistanciaKm() < seleccionada.getDistanciaKm()) {
				return false;
			}
		}
		return true;
	}
	
	private boolean cumpleRestricciones(Alternativa alternativa) {
		if (!restricciones.permiteDistancia(alternativa.getDistanciaKm())) {
			return false;
		}
		return !(alternativa instanceof AlternativaReposicion reposicion)
				|| restricciones.permiteEspera(reposicion.getTiempoEstimadoMinutos());
	}
}
