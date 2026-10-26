package model;

import java.util.HashMap;
import java.util.Map;

public class CalculadorDistanciaMock implements CalculadorDistancia{
	private final Map<String, Double> distanciasPorTienda = new HashMap<>();
	
	@Override
	public double calcularKm(Tienda origen, Tienda destino) {
		// Buscamos la distancia configurada para esta tienda en el mapa
        // Si no se ha configurado ninguna devolvemos 0.0 por defecto
        return distanciasPorTienda.getOrDefault(destino.getId(), 0.0);
	}
	
	public void configurarDistancia(Tienda tienda, double distanciaKm) {
        distanciasPorTienda.put(tienda.getId(), distanciaKm);
    }

}
