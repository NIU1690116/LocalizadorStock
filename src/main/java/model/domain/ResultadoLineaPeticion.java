package model.domain;

import java.util.List;
import java.util.Objects;

/**
 * Agrupa una línea de petición con sus alternativas, la mejor opción propuesta
 * y el estado de resolución, guarda una copia inmutable de la lista para que
 * el resultado no cambie si se modifica la lista original
 */
public final class ResultadoLineaPeticion {
	private final LineaPeticion linea;
	private final List<Alternativa> alternativas;
	private final Alternativa mejorAlternativa;
	private final EstadoResolucion estado;

	public ResultadoLineaPeticion(
			LineaPeticion linea,
			List<Alternativa> alternativas,
			Alternativa mejorAlternativa,
			EstadoResolucion estado) {
		// Precondiciones: línea, lista de alternativas y estado obligatorios
		if (linea == null) {
			throw new IllegalArgumentException("La línea de petición es obligatoria");
		}
		if (alternativas == null || alternativas.stream().anyMatch(Objects::isNull)) {
			throw new IllegalArgumentException(
					"La lista de alternativas no puede ser nula ni contener nulos");
		}
		if (estado == null) {
			throw new IllegalArgumentException("El estado de resolución es obligatorio");
		}
		// Precondiciones: la mejor opción pertenece a la lista
		// si se propone una alternativa, debe existir una mejor alternativa
		if (mejorAlternativa != null && !alternativas.contains(mejorAlternativa)) {
			throw new IllegalArgumentException(
					"La mejor alternativa debe estar en la lista de alternativas");
		}
		if (estado == EstadoResolucion.ALTERNATIVA_PROPUESTA && mejorAlternativa == null) {
			throw new IllegalArgumentException(
					"Debe existir una mejor alternativa cuando se propone una opción");
		}
		this.linea = linea;
		this.alternativas = List.copyOf(alternativas);
		this.mejorAlternativa = mejorAlternativa;
		this.estado = estado;
		// Postcondición
		assert invariant() : "El resultado de línea debe cumplir sus invariantes";
	}

	/**
	 * Comprueba las condiciones que deben mantenerse en el resultado construido.
	 * @return true si los datos son válidos y coherentes, false en caso contrario
	 * Lógica: verifica que haya línea, lista sin nulos y estado, que la mejor alternativa
	 * sea nula o pertenezca a la lista, y que un estado de alternativa propuesta tenga
	 * una mejor alternativa asociada
	 */
	private boolean invariant() {
		return linea != null
				&& alternativas != null
				&& alternativas.stream().noneMatch(Objects::isNull)
				&& (mejorAlternativa == null || alternativas.contains(mejorAlternativa))
				&& estado != null
				&& (estado != EstadoResolucion.ALTERNATIVA_PROPUESTA || mejorAlternativa != null);
	}
	
	//Obtiene la línea de petición evaluada.
	public LineaPeticion getLinea() {
		return linea;
	}

	//Obtiene las alternativas disponibles para la línea.
	public List<Alternativa> getAlternativas() {
		return alternativas;
	}

	//Obtiene la alternativa seleccionada como mejor
	public Alternativa getMejorAlternativa() {
		return mejorAlternativa;
	}

	//Obtiene el estado de resolución de la línea.
	public EstadoResolucion getEstado() {
		return estado;
	}


}
