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
    
    public boolean permiteEspera(Integer EsperaMinutos) {
	if (EsperaMinutos == null || EsperaMinutos > ESPERA_MAXIMA_MINUTOS || EsperaMinutos < 0) {
    		return false;
    	}
    	return true;

    }
    
	public double getDistanciaMaximaKm() {
		return DISTANCIA_MAXIMA_KM;
	}

	public int getEsperaMaximaMinutos() {
		return ESPERA_MAXIMA_MINUTOS;
	}
	
	


}
