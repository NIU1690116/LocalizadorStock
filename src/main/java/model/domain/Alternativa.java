package model.domain;

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
		this.tiempoEstimadoMinutos = tiempoEstimadoMinutos;
		this.distanciaKm = distanciaKm;	
		// Postcondición: los valores recibidos quedan almacenados y cumplen el invariante
		assert invariant() : "La alternativa debe tener tiempo y distancia válidos";
	}

	private boolean invariant() {
		return tiempoEstimadoMinutos >= 0
				&& Double.isFinite(distanciaKm)
				&& distanciaKm >= 0.0;
	}
	
	public int getTiempoEstimadoMinutos() {
		return tiempoEstimadoMinutos;
	}

	public double getDistanciaKm() {
		return distanciaKm;
	}

}
