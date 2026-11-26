package model.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa una petición de búsqueda con id, fecha de solicitud y
 * una lista de líneas. Las líneas se añaden y se exponen como una vista no modificable.
 */
public class Peticion {
	private final String id;
    private final LocalDateTime fechaSolicitud;
    private final List<LineaPeticion> lineas;
    
    public Peticion(String id, LocalDateTime fechaSolicitud) {
		// Precondiciones: identificador no vacío y fecha de solicitud obligatoria.
    	if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El id es obligatorio");
        }
        if (fechaSolicitud == null) {
            throw new IllegalArgumentException("La fecha de solicitud es obligatoria");
        }
    	this.id = id;
        this.fechaSolicitud = fechaSolicitud;
        this.lineas = new ArrayList<>();
		// Postcondición: la petición inicia sin líneas y cumple su invariante.
		assert invariant() : "La petición recién creada debe ser válida";
    }

	/**
	 * Comprueba la validez del identificador, fecha y líneas almacenadas
	 * @return true si los datos existen y cada línea tiene producto y cantidad positiva
	 * Lógica: verifica identificador y fecha y recorre las líneas para comprobar
	 * que no sean nulas y que cada una contenga producto y cantidad válida
	 */
	private boolean invariant() {
		return id != null && !id.trim().isEmpty()
				&& fechaSolicitud != null
				&& lineas != null
				&& lineas.stream().allMatch(linea -> linea != null
						&& linea.getProducto() != null
						&& linea.getCantidad() > 0);
    }

	public String getId() {
		return id;
	}

	// Obtiene la fecha y hora en que se registró la petición.
	public LocalDateTime getFechaSolicitud() {
		return fechaSolicitud;
	}

	//Obtiene las líneas como una vista que el llamador no puede modificar
	public List<LineaPeticion> getLineas() {
		// Postcondición: devuelve una vista de solo lectura de las líneas actuales.
		assert invariant();
		return Collections.unmodifiableList(lineas);
	}
	
	/**
	 * Añade una línea a la petición
	 * @param linea línea que se agregará
	 * @throws IllegalArgumentException si la línea es null
	 * Lógica: comprueba la invariante antes del cambio, rechaza una línea nula
	 * añade la línea y verifica mediante aserciones que se añadió una vez y que
	 * la petición sigue siendo válida
	 */
	public void agregarLinea(LineaPeticion linea) {
		// Precondición de estado: la petición debe cumplir su invariante antes del cambio.
		assert invariant() : "La petición debe ser válida antes de añadir una línea";
		// Precondición de entrada: la línea que se añade no puede ser nula.
        if (linea == null) {
            throw new IllegalArgumentException("La línea no puede ser null");
        }
		int numeroLineasAnterior = lineas.size();
        lineas.add(linea);
		// Postcondiciones: se añade exactamente la línea recibida y se conserva el invariante.
		assert lineas.size() == numeroLineasAnterior + 1
				&& lineas.get(numeroLineasAnterior) == linea
				: "Debe añadirse una única vez la línea recibida";
		assert invariant() : "Añadir una línea no debe romper el invariante de la petición";
    }
}
