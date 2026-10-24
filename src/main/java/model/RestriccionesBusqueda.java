package main.java.model;

import java.time.LocalDateTime;

// Define las condiciones de búsqueda como la distancia máxima de 10 km, espera máxima de reposición de 120 minutos y fecha límite
public class RestriccionesBusqueda {
    private final double distanciaMaximaKm;
    private final int esperaMaximaMinutos;
    private final LocalDateTime fechaLimite;

    public RestriccionesBusqueda(double distanciaMaximaKm, int esperaMaximaMinutos, LocalDateTime fechaLimite) {
        this.distanciaMaximaKm = distanciaMaximaKm;
        this.esperaMaximaMinutos = esperaMaximaMinutos;
        this.fechaLimite = fechaLimite;
    }
    
    public boolean permiteDistancia(Double distancia) {
    	if(distancia > distanciaMaximaKm || distancia <= 0) {
    		return false;
    	}
    	return true;
    }
    
	public double getDistanciaMaximaKm() {
		return distanciaMaximaKm;
	}

	public int getEsperaMaximaMinutos() {
		return esperaMaximaMinutos;
	}

	public LocalDateTime getFechaLimite() {
		return fechaLimite;
	}


}
