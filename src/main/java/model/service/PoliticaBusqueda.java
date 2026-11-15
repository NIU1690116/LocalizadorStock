package model.service;

import model.domain.Alternativa;

public class PoliticaBusqueda {
	private static final double PESO_TIEMPO = 0.70;
	private static final double PESO_DISTANCIA = 0.30;
	private static final double TIEMPO_REFERENCIA_MINUTOS = 120.0;
	private static final double DISTANCIA_REFERENCIA_KM = 10.0;

	public double calcularPuntuacion(Alternativa alternativa) {
		if (alternativa == null) {
			throw new IllegalArgumentException("La alternativa es obligatoria");
		}

		return 0.0;
	}
}
