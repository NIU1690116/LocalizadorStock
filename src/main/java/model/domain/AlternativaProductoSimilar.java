package model.domain;

public record AlternativaProductoSimilar(
		Producto productoSimilar,
		Tienda tienda,
		double gradoSimilitud) {

	public AlternativaProductoSimilar {
		if (productoSimilar == null) {
			throw new IllegalArgumentException("El producto no puede ser");
		}
	}
}
