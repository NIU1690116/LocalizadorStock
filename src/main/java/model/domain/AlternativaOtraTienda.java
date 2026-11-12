package model.domain;

public final class AlternativaOtraTienda extends Alternativa {
	private final Tienda tienda;

	public AlternativaOtraTienda(Tienda tienda, int tiempoEstimadoMinutos, double distanciaKm) {
		super(tiempoEstimadoMinutos, distanciaKm);
		if (tienda == null) {
			throw new IllegalArgumentException("La tienda destino es obligatoria");
		}
		this.tienda = tienda;
	}

	public Tienda getTienda() {
		return tienda;
	}
}
