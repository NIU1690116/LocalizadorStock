package model.domain;

import java.util.List;
import java.util.Objects;

public final class ResultadoBusqueda {
	private final List<ResultadoLineaPeticion> resultados;

	public ResultadoBusqueda(List<ResultadoLineaPeticion> resultados) {
		// Precondiciones: debe recibirse una lista no nula, no vacía y sin resultados nulos.
		if (resultados == null || resultados.isEmpty()) {
			throw new IllegalArgumentException("La búsqueda debe contener resultados por línea");
		}
		if (resultados.stream().anyMatch(Objects::isNull)) {
			throw new IllegalArgumentException("La búsqueda no puede contener resultados nulos");
		}

		// Se conserva una copia inmutable para que el llamador no altere este resultado.
		this.resultados = List.copyOf(resultados);
		// Postcondición: el objeto contiene al menos un resultado válido.
		assert invariant() : "El resultado de búsqueda debe cumplir sus invariantes";
	}

	private boolean invariant() {
		return resultados != null
				&& !resultados.isEmpty()
				&& resultados.stream().noneMatch(Objects::isNull);
	}

	public List<ResultadoLineaPeticion> getResultados() {
		// Postcondición: devuelve la lista inmutable que representa esta búsqueda.
		assert invariant();
		return resultados;
	}

	public boolean estaResuelta() {
		// Precondición de estado: los resultados deben conservar el invariante de la clase.
		assert invariant();
		// Postcondición: true si todas las líneas tienen un estado distinto de SIN_SOLUCION.
		return resultados.stream()
				.allMatch(resultado -> resultado.getEstado() != EstadoResolucion.SIN_SOLUCION);
	}
}
