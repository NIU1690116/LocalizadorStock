package model.service;

import model.domain.Alternativa;

public class PoliticaBusqueda {
	private static final double PESO_TIEMPO = 0.70;
	private static final double PESO_DISTANCIA = 0.30;
	private static final double TIEMPO_REFERENCIA_MINUTOS = 120.0;
	private static final double DISTANCIA_REFERENCIA_KM = 10.0;

	//medidor de "desfavorabilidad": cuanto más lejos y más tarde, más penalizado (mayor porcentaje)
	public double calcularPuntuacion(Alternativa alternativa) {
		if (alternativa == null) {
			throw new IllegalArgumentException("La alternativa es obligatoria");
		}

		double tiempoNormalizado = alternativa.getTiempoEstimadoMinutos() / TIEMPO_REFERENCIA_MINUTOS;
		double distanciaNormalizada = alternativa.getDistanciaKm() / DISTANCIA_REFERENCIA_KM;
		return PESO_TIEMPO * tiempoNormalizado + PESO_DISTANCIA * distanciaNormalizada;
	}
}
