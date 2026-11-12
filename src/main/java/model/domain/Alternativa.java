package model.domain;

public abstract class Alternativa {
	private final int tiempoEstimadoMinutos;
	private final double distanciaKm;

	protected Alternativa(int tiempoEstimadoMinutos, double distanciaKm) {
		if (tiempoEstimadoMinutos < 0) {
			throw new IllegalArgumentException("El tiempo estimado no puede ser negativo");
		}
		if (distanciaKm < 0.0) {
			throw new IllegalArgumentException("La distancia debe ser no negativa");
		}
		this.tiempoEstimadoMinutos = tiempoEstimadoMinutos;
		this.distanciaKm = distanciaKm;
		
	}

}
