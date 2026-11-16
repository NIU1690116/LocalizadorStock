package model.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import model.distance.CalculadorDistancia;
import model.distance.EstimadorTiempoViaje;
import model.domain.AlternativaProductoSimilar;
import model.domain.LineaPeticion;
import model.domain.Producto;
import model.domain.Tienda;

public class BuscadorProductosSimilares {
	private static final int NUMERO_CARACTERISTICAS = 3;
	private final CalculadorDistancia calculadorDistancia;
	private final EstimadorTiempoViaje estimadorTiempoViaje;

	public BuscadorProductosSimilares(
			CalculadorDistancia calculadorDistancia,
			EstimadorTiempoViaje estimadorTiempoViaje) {
		if (calculadorDistancia == null) {
			throw new IllegalArgumentException("El calculador de distancia es obligatorio");
		}
		if (estimadorTiempoViaje == null) {
			throw new IllegalArgumentException("El estimador de tiempo es obligatorio");
		}
		this.calculadorDistancia = calculadorDistancia;
		this.estimadorTiempoViaje = estimadorTiempoViaje;
	}

	public List<AlternativaProductoSimilar> buscarSimilares(
			Producto solicitado,
			Tienda origen,
			List<Tienda> tiendas) {
		return buscarSimilares(solicitado, 1, origen, tiendas);
	}
	
	public List<AlternativaProductoSimilar> buscarSimilares(
			LineaPeticion linea,
			Tienda origen,
			List<Tienda> tiendas) {
		if (linea == null) {
	        throw new IllegalArgumentException("La línea de petición es obligatoria");
	    }
	    return buscarSimilares(linea.getProducto(), linea.getCantidad(), origen, tiendas);
	}
	
	private List<AlternativaProductoSimilar> buscarSimilares(
			Producto solicitado,
			int cantidad,
			Tienda origen,
			List<Tienda> tiendas) {
		if (solicitado == null) {
            throw new IllegalArgumentException("El producto solicitado es obligatorio");
        }
		if (origen == null) {
			throw new IllegalArgumentException("La tienda de origen es obligatoria");
		}
        if (tiendas == null) {
            throw new IllegalArgumentException("La lista de tiendas es obligatoria");
        }
        
        List<AlternativaProductoSimilar> alternativas = new ArrayList<>();
        for (Tienda tienda : tiendas) {
            if (tienda == null) {
                continue;
            }
            for (Producto candidato : tienda.getInventario().productosConStockSuficiente(cantidad)) {
                if (solicitado.equals(candidato)) {
                    continue;
                }
                double similitud = calcularSimilitud(solicitado, candidato);
                if (similitud > 0.0) {
	                double distanciaKm = calculadorDistancia.calcularKm(origen, tienda);
	                int tiempoEstimado = estimadorTiempoViaje.estimarMinutos(origen, tienda);
	                alternativas.add(new AlternativaProductoSimilar(
						candidato, tienda, similitud, tiempoEstimado, distanciaKm));
                }
            }
        }
        alternativas.sort(Comparator
                .comparingDouble(AlternativaProductoSimilar::gradoSimilitud).reversed()
                .thenComparing(a -> a.productoSimilar().getCodigo())
                .thenComparing(a -> a.tienda().getId()));
        return alternativas;
    } 
	
	public String normalizar(String valor) {
		if (valor == null || valor.isBlank()) {
	        return null;
	    }
		return valor.trim().toLowerCase(Locale.ROOT);
    } 
	
	public boolean coincide(String valorPrimero, String valorSegundo) {
		String primero = normalizar(valorPrimero);
	    String segundo = normalizar(valorSegundo);
	    return primero.equals(segundo);
	}
	
	public double calcularSimilitud(Producto primero, Producto segundo) {
		int coincidencias = 0;
	    coincidencias += coincide(primero.getFamilia(), segundo.getFamilia()) ? 1 : 0;
	    coincidencias += coincide(primero.getGenero(), segundo.getGenero()) ? 1 : 0;
	    coincidencias += coincide(primero.getColor(), segundo.getColor()) ? 1 : 0;
	    return coincidencias * 100.0 / NUMERO_CARACTERISTICAS;
	}
	
	
	

}
