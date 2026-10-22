package main.java.model;

import java.time.LocalDateTime;

// Define las condiciones de búsqueda: distancia máxima de 10 km, espera máxima de reposición de 120 minutos y fecha límite
public class RestriccionesBusqueda {
    private final double distanciaMaximaKm;
    private final int esperaMaximaMinutos;
    private final LocalDateTime fechaLimite;

    public RestriccionesBusqueda(double distanciaMaximaKm, int esperaMaximaMinutos, LocalDateTime fechaLimite) {
        this.distanciaMaximaKm = distanciaMaximaKm;
        this.esperaMaximaMinutos = esperaMaximaMinutos;
        this.fechaLimite = fechaLimite;
    }


}
