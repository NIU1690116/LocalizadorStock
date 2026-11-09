package model.domain;

public record AlternativaProductoSimilar(
		Producto productoSimilar,
		Tienda tienda,
		double gradoSimilitud) {

	public AlternativaProductoSimilar {
		if (productoSimilar == null) {
			throw new IllegalArgumentException("El producto no puede ser");
		}
		if (tienda == null) {
			throw new IllegalArgumentException("La tienda no puede ser");
		}
		if (gradoSimilitud <= 0.0) {
			throw new IllegalArgumentException("El grado de similitud debe estar entre 0 y 100");
		}
	}
}
