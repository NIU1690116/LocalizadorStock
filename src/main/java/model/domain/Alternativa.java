package model.domain;

/**
 * Clase abstracta para una alternativa de resolución de stock
 * Todas las alternativas comparten un tiempo estimado y una distancia, que
 * deben ser no negativos y finitos, las subclases aportan los datos específicos
 * de cada tipo de opción
 */
public abstract class Alternativa {
	private final int tiempoEstimadoMinutos;
	private final double distanciaKm;

	protected Alternativa(int tiempoEstimadoMinutos, double distanciaKm) {
		// Precondiciones: el tiempo no puede ser negativo y la distancia debe ser
		// finita y no negativa
		if (tiempoEstimadoMinutos < 0) {
			throw new IllegalArgumentException("El tiempo estimado no puede ser negativo");
		}
		if (!Double.isFinite(distanciaKm) || distanciaKm < 0.0) {
			throw new IllegalArgumentException("La distancia debe ser finita y no negativa");
		}
		// Postcondición: los valores recibidos quedan almacenados y cumplen el invariante
		this.tiempoEstimadoMinutos = tiempoEstimadoMinutos;
		this.distanciaKm = distanciaKm;	
		assert invariant() : "La alternativa debe tener tiempo y distancia válidos";
	}

	// Comprueba que el tiempo y la distancia almacenados sean válidos
	private boolean invariant() {
		return tiempoEstimadoMinutos >= 0
				&& Double.isFinite(distanciaKm)
				&& distanciaKm >= 0.0;
	}
	
	//Obtiene el tiempo estimado de la alternativa
	public int getTiempoEstimadoMinutos() {
		assert invariant();
		return tiempoEstimadoMinutos;
	}

	//Obtiene la distancia estimada de la alternativa
	public double getDistanciaKm() {
		assert invariant();
		return distanciaKm;
	}

}
