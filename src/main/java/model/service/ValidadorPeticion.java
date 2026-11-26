package model.service;

import model.domain.LineaPeticion;
import model.domain.Peticion;
import model.domain.Reposicion;
import model.domain.RestriccionesBusqueda;
import java.time.LocalDateTime;

/**
 * Comprueba que las peticiones y las reposiciones candidatas cumplen las
 * condiciones necesarias para participar en una búsqueda. Valida tanto la
 * estructura y cantidades solicitadas como la fecha, el producto y la cantidad
 * ofrecida por una reposición frente a las restricciones de espera
 */
public class ValidadorPeticion {

    /**
     * Comprueba que una petición contiene al menos una línea válida
     * @param peticion petición que se quiere validar
     * @return true si existe, contiene líneas y cada una tiene producto
     * y cantidad positiva, false en caso contrario
     * Lógica: rechaza peticiones nulas, sin líneas o con alguna línea nula, sin
     * producto o con cantidad menor o igual que cero, si supera las comprobaciones
     * la considera válida.
     */
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

    /**
     * Comprueba si una reposición satisface una línea de la petición y la espera permitida.
     * @param peticion petición a la que pertenece la línea
     * @param linea línea que se pretende cubrir con la reposición
     * @param reposicion reposición candidata que se evalúa
     * @param restricciones límites aplicables, incluida la espera máxima
     * @return true si la reposición es válida para esa línea y restricciones,
     * false si falla alguna comprobación.
     * Lógica: primero valida la petición y la pertenencia de la línea, después
     * verifica que la fecha prevista esté entre la solicitud y el límite máximo,
     * que coincida el código del producto y que haya unidades suficientes, por
     * último, convierte la espera a minutos y consulta la restricción.
     */
    public boolean validarReposicion(Peticion peticion, LineaPeticion linea, Reposicion reposicion,
            RestriccionesBusqueda restricciones) {
        if (!validarPeticion(peticion) || linea == null || reposicion == null || restricciones == null
                || !peticion.getLineas().contains(linea)) {
            return false;
        }
    	
    	LocalDateTime fechaSolicitud = peticion.getFechaSolicitud();
        LocalDateTime fechaPrevista = reposicion.getFechaPrevista();
        LocalDateTime fechaLimite = fechaSolicitud.plusMinutes(restricciones.getEsperaMaximaMinutos());
        if (fechaPrevista.isBefore(fechaSolicitud) || fechaPrevista.isAfter(fechaLimite)) {
            return false;
        }
        
        if (reposicion.getProducto() == null
                || !reposicion.getProducto().getCodigo().equals(linea.getProducto().getCodigo())
                || reposicion.getCantidad() < linea.getCantidad()) {
            return false;
        }

        //Calculamos los minutos de diferencia enter las fechas
        long minutosEspera = java.time.Duration.between(fechaSolicitud, fechaPrevista).toMinutes();

        //Convertimos el valor a int
        int minutosInt = (int) minutosEspera;

        //Devolvemos el resultado de la validación
        return restricciones.permiteEspera(minutosInt);
    }
    
    

}
