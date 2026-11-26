package model.service;

import model.domain.Alternativa;

/**
 * Define el criterio numérico para comparar alternativas de búsqueda
 * Calcula una puntuación de desfavorabilidad combinando el tiempo estimado y
 * la distancia, con mayor peso para el tiempo, una puntuación menor representa
 * una alternativa más conveniente
 */
public class PoliticaBusqueda {
	private static final double PESO_TIEMPO = 0.70;
	private static final double PESO_DISTANCIA = 0.30;
	private static final double TIEMPO_REFERENCIA_MINUTOS = 120.0;
	private static final double DISTANCIA_REFERENCIA_KM = 10.0;

	/**
	 * Calcula la desfavorabilidad de una alternativa según su tiempo y distancia.
	 * @param alternativa alternativa cuyos datos se puntúan
	 * @return suma ponderada del tiempo y la distancia normalizados cuanto mayor
	 * el valor, menos conveniente es la alternativa
	 * @throws IllegalArgumentException si la alternativa es null
	 * Lógica: divide el tiempo estimado entre 120 minutos y la distancia entre
	 * 10 kilómetros, multiplica esos valores por sus pesos 0,70 y 0,30 y suma
	 * los resultados.
	 */
	public double calcularPuntuacion(Alternativa alternativa) {
		if (alternativa == null) {
			throw new IllegalArgumentException("La alternativa es obligatoria");
		}

		double tiempoNormalizado = alternativa.getTiempoEstimadoMinutos() / TIEMPO_REFERENCIA_MINUTOS;
		double distanciaNormalizada = alternativa.getDistanciaKm() / DISTANCIA_REFERENCIA_KM;
		return PESO_TIEMPO * tiempoNormalizado + PESO_DISTANCIA * distanciaNormalizada;
	}
}
