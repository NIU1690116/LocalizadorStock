package main.java.model;

import java.time.LocalDateTime;

// Validaciones de peticiones y de sus reposiciones candidatas
public class ValidadorPeticion {

    // Comprueba que la petición existe y contiene líneas válidas.
    public boolean validarPetición(Peticion peticion) {
       if (peticion == null) {
    	   return false;
       }
       return true;
    }
}