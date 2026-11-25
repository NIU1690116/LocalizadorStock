package model.distance;

import model.domain.Tienda;

/**
 * Contrato para calcular la distancia entre dos tiendas
 * Permite intercambiar la estrategia de cálculo sin acoplarla a los servicios
 * que necesitan conocer kilómetros entre ubicaciones
 */
public interface CalculadorDistancia {
	// Calcula la distancia entre una tienda de origen y una tienda destino
	double calcularKm(Tienda origen, Tienda destino);
	
}
