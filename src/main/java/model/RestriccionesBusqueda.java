package main.java.model;

import java.time.LocalDateTime;

// Define los límites acordados para todas las búsquedas
public class RestriccionesBusqueda {
    private static final double DISTANCIA_MAXIMA_KM = 10.0;
    private static final int ESPERA_MAXIMA_MINUTOS = 120;

    //no neccesitamos que el constructor reciba valores, ya que los limites eran siempre los mismos
    public RestriccionesBusqueda() {
    }
    
    public boolean permiteDistancia(Double distancia) {
	    if (distancia == null || distancia > DISTANCIA_MAXIMA_KM || distancia <= 0) {
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
