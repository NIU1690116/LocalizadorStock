package model.service;

import java.util.List;
import java.util.Objects;

import model.domain.Alternativa;
import model.domain.AlternativaReposicion;
import model.domain.RestriccionesBusqueda;

/**
 * Filtra y ordena alternativas de stock según política de puntuación y sus
 * restricciones. Evalúa la distancia de todas las alternativas y la espera de
 * las reposiciones y entonces selecciona la de menor puntuación y aplica desempates por
 * tiempo y distancia
 */
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

	/**
	 * Comprueba que las dependencias obligatorias del evaluador estén inicializadas.
	 * @return true si hay política y restricciones,  false caso contrario
	 * Lógica: comprueba que ambas referencias no sean nulas
	 */
	private boolean invariant() {
		return politica != null && restricciones != null;
	}
	
	/**
	 * Selecciona la alternativa valida con menor puntuación
	 * @param alternativas alternativas candidata, los elementos nulos se ignoran
	 * @return mejor alternativa válida, o null si ninguna cumple las restricciones
	 * @throws IllegalArgumentException si la lista es null}ç
	 * Lógica: filtra candidatas por restricciones, calcula sus puntuaciones y conserva
	 * la menor. Si hay empate, gana menor tiempo y luego menor distancia, al final
	 * comprueba mediante una aserción que la selección cumple la postcondición
	 */
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
	    // Postcondición: devuelve la candidata válida mejor puntuada, o null si no hay ninguna válida
	    assert cumplePostcondicion(alternativas, mejorAlternativa)
		    : "La alternativa seleccionada debe ser la mejor candidata válida";
	    return mejorAlternativa;
	}

	/**
	 * Comprueba que la selección pertenece a la lista y es la mejor candidata admisible
	 * @param alternativas lista original de candidatas
	 * @param seleccionada alternativa elegida, o {@code null} si no se eligió ninguna
	 * @return true si no existe una candidata admisible mejor, false en caso contrario
	 * Lógica: si no hay selección verifica que ninguna alternativa no nula cumpla las
	 * restricciones. Si hay selección, comprueba que está en la lista, es admisible y
	 * que ninguna candidata la supera por puntuación o por criterios de desempate
	 */
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
	
	/**
	 * Comprueba las restricciones aplicables a una alternativa
	 * @param alternativa alternativa que se evalúa, debe ser no nula
	 * @return true si respeta la distancia máxima y si es reposición la espera máxima
	 * Lógica: rechaza primero las alternativas que exceden la distancia, para una
	 * reposición, comprueba tambien el tiempo de espera
	 */
	private boolean cumpleRestricciones(Alternativa alternativa) {
		if (!restricciones.permiteDistancia(alternativa.getDistanciaKm())) {
			return false;
		}
		return !(alternativa instanceof AlternativaReposicion reposicion)
				|| restricciones.permiteEspera(reposicion.getTiempoEstimadoMinutos());
	}
}
