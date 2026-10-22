package main.java.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Peticion {
	private final String id;
    private final LocalDateTime fechaSolicitud;
    private final List<LineaPeticion> lineas;
    
    public Peticion(String id, LocalDateTime fechaSolicitud) {
    	if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El id es obligatorio");
        }

        if (fechaSolicitud == null) {
            throw new IllegalArgumentException("La fecha de solicitud es obligatoria");
        }
        
    	this.id = id;
        this.fechaSolicitud = fechaSolicitud;
        this.lineas = new ArrayList<>();
    }
    

}
