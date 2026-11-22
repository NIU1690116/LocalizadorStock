package model.domain;

// Define los límites acordados para todas las búsquedas
public class RestriccionesBusqueda {
    private static final double DISTANCIA_MAXIMA_KM = 10.0;
    private static final int ESPERA_MAXIMA_MINUTOS = 120;

    //no neccesitamos que el constructor reciba valores, ya que los limites eran siempre los mismos

    /**
     * Precondición: ninguna; los valores nulos o no válidos se rechazan devolviendo false.
     * Postcondición: devuelve true exactamente si la distancia es finita y está entre
     * cero y la distancia máxima, ambos límites incluidos.
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
     * Precondición: ninguna; los valores nulos o no válidos se rechazan devolviendo false.
     * Postcondición: devuelve true exactamente si la espera está entre cero y el máximo,
     * ambos límites incluidos.
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
    
	public double getDistanciaMaximaKm() {
		return DISTANCIA_MAXIMA_KM;
	}

	public int getEsperaMaximaMinutos() {
		return ESPERA_MAXIMA_MINUTOS;
	}
	
	


}
