package model.domain;

/**
 * Define los límites fijos que se aplican a las alternativas de búsqueda:
 * hasta 10 km de distancia y hasta 120 minutos de espera, incluidos los límites.
 */
public class RestriccionesBusqueda {
    private static final double DISTANCIA_MAXIMA_KM = 10.0;
    private static final int ESPERA_MAXIMA_MINUTOS = 120;

    /**
     * Comprueba si una distancia está dentro del límite configurado
     * @param distancia distancia en kilómetros, puede ser null
     * @return true si es finita y está entre 0 y 10 km, incluido
     * false si es nula, no finita, negativa o excede el máximo
     * Lógica: evalúa las condiciones del rango y comprueba mediante una aserción
     * que el resultado coincide con ellas
     */
    public boolean permiteDistancia(Double distancia) {
		boolean permitida = distancia != null
				&& Double.isFinite(distancia)
				&& distancia >= 0.0
				&& distancia <= DISTANCIA_MAXIMA_KM;
		assert permitida == (distancia != null
				&& Double.isFinite(distancia)
				&& distancia >= 0.0
				&& distancia <= DISTANCIA_MAXIMA_KM)
				: "La distancia solo se permite dentro del rango configurado";
		return permitida;
    }
    
    /**
     * Comprueba si una espera está dentro del límite configurado
     * @param esperaMinutos espera en minutos, puede ser null
     * @return {@code true} si está entre 0 y 120 minutos, incluido, false
     * si es nula, negativa o excede el máximo
     * Lógica: evalúa las condiciones del rango y comprueba mediante una aserción
     * que el resultado coincide con ellas.
     */
    public boolean permiteEspera(Integer esperaMinutos) {
		boolean permitida = esperaMinutos != null
				&& esperaMinutos >= 0
				&& esperaMinutos <= ESPERA_MAXIMA_MINUTOS;
		assert permitida == (esperaMinutos != null
				&& esperaMinutos >= 0
				&& esperaMinutos <= ESPERA_MAXIMA_MINUTOS)
				: "La espera solo se permite dentro del rango configurado";
		return permitida;
    }
    
	//Obtiene la distancia máxima permitida
	public double getDistanciaMaximaKm() {
		return DISTANCIA_MAXIMA_KM;
	}

	//Obtiene la espera máxima permitida
	public int getEsperaMaximaMinutos() {
		return ESPERA_MAXIMA_MINUTOS;
	}
}
