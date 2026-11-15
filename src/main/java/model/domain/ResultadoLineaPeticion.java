package model.domain;

import java.util.List;
import java.util.Objects;

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
		if (linea == null) {
			throw new IllegalArgumentException("La línea de petición es obligatoria");
		}
		if (alternativas == null || alternativas.stream().anyMatch(Objects::isNull)) {
			throw new IllegalArgumentException("La lista de alternativas no puede ser nula ni contener nulos");
		}		if (mejorAlternativa != null && !alternativas.contains(mejorAlternativa)) {
			throw new IllegalArgumentException("La mejor alternativa debe estar en la lista de alternativas");
		}
		if (estado == EstadoResolucion.ALTERNATIVA_PROPUESTA && mejorAlternativa == null) {
			throw new IllegalArgumentException("Debe existir una mejor alternativa cuando se propone una opción");
		}

		this.linea = linea;
		this.alternativas = List.copyOf(alternativas);
		this.mejorAlternativa = mejorAlternativa;
		this.estado = estado;
	}


}
