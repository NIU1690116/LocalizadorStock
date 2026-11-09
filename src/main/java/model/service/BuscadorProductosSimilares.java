package model.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import model.domain.AlternativaProductoSimilar;
import model.domain.LineaPeticion;
import model.domain.Producto;
import model.domain.Tienda;

public class BuscadorProductosSimilares {
	private static final int NUMERO_CARACTERISTICAS = 3;
	//method overloading
	// Asume que la cantidad pedida por defecto es 1 y delega
	public List<AlternativaProductoSimilar> buscarSimilares(Producto solicitado, List<Tienda> tiendas) {
        return buscarSimilares(solicitado, 1, tiendas);
    } 
	
	// Extrae el producto y la cantidad de la línea y da el trabajo a la versión privada
	public List<AlternativaProductoSimilar> buscarSimilares(LineaPeticion linea, List<Tienda> tiendas) { 
		if (linea == null) {
	        throw new IllegalArgumentException("La línea de petición es obligatoria");
	    }
	    return buscarSimilares(linea.getProducto(), linea.getCantidad(), tiendas);
	}
	
	//calculo real
	private List<AlternativaProductoSimilar> buscarSimilares(
			Producto solicitado, int cantidad, List<Tienda> tiendas) { 
		if (solicitado == null) {
            throw new IllegalArgumentException("El producto solicitado es obligatorio");
        }
        if (tiendas == null) {
            throw new IllegalArgumentException("La lista de tiendas es obligatoria");
        }
		return null;
    } 
	
	public String normalizar(String valor) {
		if (valor == null || valor.isBlank()) {
	        return null;
	    }
		return valor.trim().toLowerCase(Locale.ROOT);

    } 
	
	
	

}
