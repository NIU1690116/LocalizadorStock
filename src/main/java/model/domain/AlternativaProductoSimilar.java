package model.domain;

public final class AlternativaProductoSimilar extends Alternativa {
	private final Producto productoSimilar;
	private final Tienda tienda;
	private final double gradoSimilitud;

	public AlternativaProductoSimilar(
			Producto productoSimilar,
			Tienda tienda,
			double gradoSimilitud,
			int tiempoEstimadoMinutos,
			double distanciaKm) {
		super(tiempoEstimadoMinutos, distanciaKm);
		if (productoSimilar == null) {
			throw new IllegalArgumentException("El producto similar es obligatorio");
		}
		if (tienda == null) {
			throw new IllegalArgumentException("La tienda es obligatoria");
		}
		if (!Double.isFinite(gradoSimilitud) || gradoSimilitud <= 0.0 || gradoSimilitud > 100.0) {
			throw new IllegalArgumentException("El grado de similitud debe estar entre 0 y 100");
		}
		this.productoSimilar = productoSimilar;
		this.tienda = tienda;
		this.gradoSimilitud = gradoSimilitud;
	}

	public Producto productoSimilar() {
		return productoSimilar;
	}

	public Tienda tienda() {
		return tienda;
	}

	public double gradoSimilitud() {
		return gradoSimilitud;
	}
}
