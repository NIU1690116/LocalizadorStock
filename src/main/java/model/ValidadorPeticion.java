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
            if (linea == null) {
                return false;
            }
        }
        return true;
    }

}
