package model.domain;

/**
 * Alternativa que propone recoger el producto en una tienda distinta de la actual
 * Almacena la tienda destino y las métricas de desplazamiento definidas por Alternativa
 */
public final class AlternativaOtraTienda extends Alternativa {
	private final Tienda tienda;

	public AlternativaOtraTienda(Tienda tienda, int tiempoEstimadoMinutos, double distanciaKm) {
		super(tiempoEstimadoMinutos, distanciaKm);
		if (tienda == null) {
			throw new IllegalArgumentException("La tienda destino es obligatoria");
		}
		this.tienda = tienda;
	}

	//Obtiene la tienda destino de la alternativa
	public Tienda getTienda() {
		return tienda;
	}
}
