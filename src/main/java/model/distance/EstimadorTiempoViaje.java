package model.distance;

import model.domain.Tienda;

/**
 * Contrato para estimar el tiempo de desplazamiento entre dos tiendas
 * Desacopla los servicios que necesitan una duración estimada de la estrategia
 */
public interface EstimadorTiempoViaje {
	//Estima el tiempo de viaje entre una tienda de origen y una tienda destino
	int estimarMinutos(Tienda origen, Tienda destino);
}
