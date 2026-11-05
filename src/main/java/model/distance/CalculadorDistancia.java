package model.distance;

import model.domain.Tienda;
public interface CalculadorDistancia {
	double calcularKm(Tienda origen, Tienda destino);
	
}
