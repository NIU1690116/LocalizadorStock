package model.domain;

public final class AlternativaReposicion extends Alternativa {
	private final Tienda tienda;
	private final Reposicion reposicion;

	public AlternativaReposicion(
			Tienda tienda,
			Reposicion reposicion,
			int tiempoEstimadoMinutos,
			double distanciaKm) {
		super(tiempoEstimadoMinutos, distanciaKm);
		if (tienda == null) {
			throw new IllegalArgumentException("La tienda es obligatoria");
		}
		if (reposicion == null) {
			throw new IllegalArgumentException("La reposición es obligatoria");
		}
		this.tienda = tienda;
		this.reposicion = reposicion;
	}


}
