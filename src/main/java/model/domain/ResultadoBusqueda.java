package model.domain;

import java.util.List;
import java.util.Objects;

/**
 * Representa el resultado completo de una búsqueda, con un resultado por línea
 * Requiere al menos un resultado no nulo y conserva la lista como una copia
 * inmutable para proteger el estado de la búsqueda
 */
public final class ResultadoBusqueda {
	private final List<ResultadoLineaPeticion> resultados;

	public ResultadoBusqueda(List<ResultadoLineaPeticion> resultados) {
		// Precondiciones: debe recibirse una lista no nula, no vacía y sin resultados nulos
		if (resultados == null || resultados.isEmpty()) {
			throw new IllegalArgumentException("La búsqueda debe contener resultados por línea");
		}
		if (resultados.stream().anyMatch(Objects::isNull)) {
			throw new IllegalArgumentException("La búsqueda no puede contener resultados nulos");
		}

		// Se conserva una copia inmutable para que el llamador no altere este resultado
		this.resultados = List.copyOf(resultados);
		// Postcondición: el objeto contiene al menos un resultado válido.
		assert invariant() : "El resultado de búsqueda debe cumplir sus invariantes";
	}

	/**
	 * Comprueba que la lista de resultados sea válida
	 * @return truesi la lista existe, no está vacía y no contiene nulos
	 * Lógica: comprueba esas condiciones sobre la lista almacenada
	 */
	private boolean invariant() {
		return resultados != null
				&& !resultados.isEmpty()
				&& resultados.stream().noneMatch(Objects::isNull);
	}

	/**
	 * Obtiene los resultados de las líneas de la búsqueda
	 * @return lista inmutable de resultados por línea
	 * Lógica: comprueba mediante una aserción la invariante
	 * y devuelve la lista almacenada.
	 */
	public List<ResultadoLineaPeticion> getResultados() {
		// Postcondición: devuelve la lista inmutable que representa esta búsqueda.
		assert invariant();
		return resultados;
	}

	/**
	 * Indica si todas las líneas tienen una resolución o una alternativa propuesta
	 * @return true si ninguna línea tiene estado SIN_SOLUCION
	 * false si al menos una línea no tiene solución
	 * Lógica: comprueba mediante una aserción la invariante y verifica que el estado
	 * de cada resultado sea distinto de SIN_SOLUCION
	 */
	public boolean estaResuelta() {
		// Precondición de estado: los resultados deben conservar el invariante de la clase.
		assert invariant();
		// Postcondición: true si todas las líneas tienen un estado distinto de SIN_SOLUCION.
		return resultados.stream()
				.allMatch(resultado -> resultado.getEstado() != EstadoResolucion.SIN_SOLUCION);
	}
}
