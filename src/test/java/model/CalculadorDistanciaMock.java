package model;

import java.util.HashMap;
import java.util.Map;

public class CalculadorDistanciaMock implements CalculadorDistancia{
	private final Map<String, Double> distanciasPorTienda = new HashMap<>();
	
	@Override
	public double calcularKm(Tienda origen, Tienda destino) {
		return 0;
	}
	
	public void configurarDistancia(Tienda tienda, double distanciaKm) {
        distanciasPorTienda.put(tienda.getId(), distanciaKm);
    }

}
