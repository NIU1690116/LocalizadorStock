package main.java.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Peticion {
	private final String id;
    private final LocalDateTime fechaSolicitud;
    private final List<LineaPeticion> lineas;
    
    public Peticion(String id, LocalDateTime fechaSolicitud) {
    	this.id = id;
        this.fechaSolicitud = fechaSolicitud;
        this.lineas = new ArrayList<>();
    }
    

}
