package main.java.model;

import java.time.LocalDateTime;

// Validaciones de peticiones y de sus reposiciones candidatas
public class ValidadorPeticion {

    // Comprueba que la petición existe y contiene líneas válidas.
    public boolean validarPeticion(Peticion peticion) {
        if (peticion == null || peticion.getLineas() == null || peticion.getLineas().isEmpty()) {
            return false;
        }
       
        for (LineaPeticion linea : peticion.getLineas()) {
            if (linea == null || linea.getProducto() == null || linea.getCantidad() <= 0) {
                return false;
            }
        }
        return true;
    }
    
    public boolean validarReposicion(Peticion peticion, LineaPeticion linea, Reposicion reposicion, RestriccionesBusqueda restricciones) {
    	if (!validarPeticion(peticion) || linea == null || reposicion == null || restricciones == null || !peticion.getLineas().contains(linea)) {
            return false;
        }
    	
    	LocalDateTime fechaSolicitud = peticion.getFechaSolicitud();
        LocalDateTime fechaPrevista = reposicion.getFechaPrevista();
        LocalDateTime fechaLimite = fechaSolicitud.plusMinutes(restricciones.getEsperaMaximaMinutos());
        if (fechaPrevista.isBefore(fechaSolicitud) || fechaPrevista.isAfter(fechaLimite)) {
            return false;
        }

        return true;
    }
    
    

}
