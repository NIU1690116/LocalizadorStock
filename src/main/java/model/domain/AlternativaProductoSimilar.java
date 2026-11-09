package model.domain;

public record AlternativaProductoSimilar(
		Producto productoSimilar,
		Tienda tienda,
		double gradoSimilitud) {


}
