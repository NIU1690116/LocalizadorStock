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

		this.linea = linea;
		this.alternativas = List.copyOf(alternativas);
		this.mejorAlternativa = mejorAlternativa;
		this.estado = estado;
	}


}
