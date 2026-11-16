package model.domain;

import java.util.List;
import java.util.Objects;

public final class ResultadoBusqueda {
	private final List<ResultadoLineaPeticion> resultados;

	public ResultadoBusqueda(List<ResultadoLineaPeticion> resultados) {
		if (resultados == null) {
			throw new IllegalArgumentException("La búsqueda debe contener resultados");
		}
		this.resultados = List.copyOf(resultados);
	}

	public List<ResultadoLineaPeticion> getResultados() {
		return resultados;
	}

}
