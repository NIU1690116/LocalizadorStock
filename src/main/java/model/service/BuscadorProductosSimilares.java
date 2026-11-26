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

/**
 * Encuentra productos distintos del solicitado que tienen stock en las tiendas
 * y son similares. Calcula la similitud mediante familia,género y color, 
 * añade las medidas de desplazamiento y ordena los resultados
 * por similitud y criterios estables de producto y tienda.
 */
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

	/**
	 * Busca productos similares a uno solicitado
	 * @param solicitado producto que se quiere sustituir
	 * @param origen tienda desde la que se calculan distancia y tiempo
	 * @param tiendas tiendas cuyos inventarios se revisan
	 * @return alternativas con similitud positiva, ordenadas por similitud descendente
	 * puede ser una lista vacía
	 * Lógica: delega la búsqueda al método interno con cantidad requerida igual a uno
	 */
	public List<AlternativaProductoSimilar> buscarSimilares(
			Producto solicitado,
			Tienda origen,
			List<Tienda> tiendas) {
		return buscarSimilares(solicitado, 1, origen, tiendas);
	}
	
	/**
	 * Busca productos similares que cubran la cantidad indicada en una línea.ç
	 * @param linea línea con el producto y la cantidad solicitada
	 * @param origen tienda desde la que se calculan distancia y tiempo
	 * @param tiendas tiendas cuyos inventarios se revisan
	 * @return alternativas con similitud positiva, ordenadas por similitud descendente
	 * puede ser una lista vacía
	 * @throws IllegalArgumentException si la línea es null
	 * Lógica: extrae producto y cantidad de la línea y delega la búsqueda al método interno
	 */
	public List<AlternativaProductoSimilar> buscarSimilares(
			LineaPeticion linea,
			Tienda origen,
			List<Tienda> tiendas) {
		if (linea == null) {
	        throw new IllegalArgumentException("La línea de petición es obligatoria");
	    }
	    return buscarSimilares(linea.getProducto(), linea.getCantidad(), origen, tiendas);
	}
	
	/**
	 * Recorre los inventarios para generar alternativas de productos similares
	 * @param solicitado producto que se quiere sustituir
	 * @param cantidad unidades mínimas que el producto candidato debe tener en stock
	 * @param origen tienda desde la que se calculan distancia y tiempo
	 * @param tiendas tiendas candidatas
	 * @return alternativas con similitud mayor que cero, ordenadas por similitud
	 * descendente, código de producto y luego identificador de tienda
	 * @throws IllegalArgumentException si el producto, el origen o la lista son null
	 * Lógica: omite tiendas nulas, productos iguales al solicitado y productos sin
	 * stock suficiente, calcula similitud para cada candidato y, si es positiva,
	 * calcula distancia y tiempo y añade una alternativa. Al final ordena la lista
	 */
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
	
	/**
	 * Normaliza un texto para poder compararlo sin distinguir mayúsculas ni espacios externos
	 * @param valor texto que se normalizará
	 * @return texto recortado y en minúsculas, o null si es nulo o está en blanco
	 * Lógica: devuelve null para entradas sin contenido, en otro caso aplica
	 * trim() y minúsculas con Locale#ROOT
	 */
	public String normalizar(String valor) {
		if (valor == null || valor.isBlank()) {
	        return null;
	    }
		return valor.trim().toLowerCase(Locale.ROOT);
    } 
	
	/**
	 * Compara dos textos después de normalizarlos
	 * @param valorPrimero primer texto que se compara
	 * @param valorSegundo segundo texto que se compara
	 * @return true si los textos normalizados son iguales
	 * Lógica: normaliza ambos valores y compara el primero con el segundo usando
	 * equals(Object)
	 */
	public boolean coincide(String valorPrimero, String valorSegundo) {
		String primero = normalizar(valorPrimero);
	    String segundo = normalizar(valorSegundo);
	    return primero.equals(segundo);
	}
	
	/**
	 * Calcula el porcentaje de características similares entre dos productos
	 * @param primero primer producto de la comparación
	 * @param segundo segundo producto de la comparación
	 * @return porcentaje entre 0 y 100 basado en familia, género y color
	 * Lógica: suma una coincidencia por cada una de esas tres características iguales
	 * tras normalizarlas y divide el total entre tres para obtener el porcentaje.
	 */
	public double calcularSimilitud(Producto primero, Producto segundo) {
		int coincidencias = 0;
	    coincidencias += coincide(primero.getFamilia(), segundo.getFamilia()) ? 1 : 0;
	    coincidencias += coincide(primero.getGenero(), segundo.getGenero()) ? 1 : 0;
	    coincidencias += coincide(primero.getColor(), segundo.getColor()) ? 1 : 0;
	    return coincidencias * 100.0 / NUMERO_CARACTERISTICAS;
	}

}
