package model.domain;

public final class AlternativaOtraTienda extends Alternativa {
	private final Tienda tienda;

	public AlternativaOtraTienda(Tienda tienda, int tiempoEstimadoMinutos, double distanciaKm) {
		super(tiempoEstimadoMinutos, distanciaKm);

		this.tienda = tienda;
	}


}
