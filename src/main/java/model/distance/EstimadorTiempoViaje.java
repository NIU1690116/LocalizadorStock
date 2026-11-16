package model.distance;

import model.domain.Tienda;

public interface EstimadorTiempoViaje {
	int estimarMinutos(Tienda origen, Tienda destino);
}
