package model.domain;

/**
 * Alternativa que propone esperar una reposición de producto en una tienda
 * Incluye la tienda y los datos de la reposición, además del tiempo y distancia
 * heredados de Alternativa
 */
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

	//Obtiene la tienda donde se espera la reposición
	public Tienda getTienda() {
		return tienda;
	}

	//Obtiene los datos de la reposición prevista
	public Reposicion getReposicion() {
		return reposicion;
	}
}
