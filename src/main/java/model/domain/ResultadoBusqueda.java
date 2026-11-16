package model.domain;

import java.util.List;
import java.util.Objects;

public final class ResultadoBusqueda {
	private final List<ResultadoLineaPeticion> resultados;

	public ResultadoBusqueda(List<ResultadoLineaPeticion> resultados) {
		if (resultados == null || resultados.isEmpty()) {
			throw new IllegalArgumentException("La búsqueda debe contener resultados por línea");
		}
		this.resultados = List.copyOf(resultados);
	}

	public List<ResultadoLineaPeticion> getResultados() {
		return resultados;
	}

	public boolean estaResuelta() {
		return resultados.stream()
				.allMatch(resultado -> resultado.getEstado() != EstadoResolucion.SIN_SOLUCION);
	}
}
