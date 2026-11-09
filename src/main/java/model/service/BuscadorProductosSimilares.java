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
	
	public List<AlternativaProductoSimilar> buscarSimilares(Producto solicitado, List<Tienda> tiendas) {
        return buscarSimilares(solicitado, 1, tiendas);
    } 
	
	public List<AlternativaProductoSimilar> buscarSimilares(LineaPeticion linea, List<Tienda> tiendas) {

        return null;

    } 

}
