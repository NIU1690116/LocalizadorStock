package model.domain;

import java.util.List;
import java.util.Objects;

public final class ResultadoBusqueda {
	private final List<ResultadoLineaPeticion> resultados;

	public ResultadoBusqueda(List<ResultadoLineaPeticion> resultados) {
		this.resultados = List.copyOf(resultados);
	}

	public List<ResultadoLineaPeticion> getResultados() {
		return resultados;
	}


}
