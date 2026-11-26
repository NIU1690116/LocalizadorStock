package model.distance;

import model.domain.Tienda;
import java.util.HashMap;
import java.util.Map;

/**
 * Mock CalculadorDistancia que permite fijar distancias
 * por tienda destino. Evita depender de cálculos geográficos reales y hace que
 * las pruebas de servicios controlen de forma determinista los valores usados
 */
public class CalculadorDistanciaMock implements CalculadorDistancia{
	private final Map<String, Double> distanciasPorTienda = new HashMap<>();
	
	/**
	 * Devuelve la distancia configurada para el destino o cero si no se configuro
	 */
	@Override
	public double calcularKm(Tienda origen, Tienda destino) {
		// Buscamos la distancia configurada para esta tienda en el mapa
        // Si no se ha configurado ninguna devolvemos 0.0 por defecto
        return distanciasPorTienda.getOrDefault(destino.getId(), 0.0);
	}
	
	/**
	 * Registra una distancia para que las pruebas la reciban al consultar el destino.
	 */
	public void configurarDistancia(Tienda tienda, double distanciaKm) {
        distanciasPorTienda.put(tienda.getId(), distanciaKm);
    }

}
