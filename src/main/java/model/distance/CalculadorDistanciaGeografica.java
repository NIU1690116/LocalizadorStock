package model.distance;

import model.domain.Tienda;

/**
 * Calcula una distancia geográfica aproximada entre dos tiendas con sus
 * coordenadas, paara calcularlo proyecta las diferencias de latitud y longitud
 * localmente y aplica una aproximación plana para desplazamientos cortos
 */
public class CalculadorDistanciaGeografica implements CalculadorDistancia {

	private static final double RADIO_TERRESTRE_KM = 6371.0088;

	/**
	 * Calcula la distancia aproximada entre dos tiendas en kilómetros
	 * @param origen tienda de origen con latitud y longitud válidas
	 * @param destino tienda destino con latitud y longitud válidas
	 * @return distancia aproximada en kilómetros
	 * @throws IllegalArgumentException si alguna tienda es null
	 * Lógica: convierte latitudes y diferencias de coordenadas a radianes, ajusta
	 * la diferencia longitudinal por el coseno de la latitud media y aplica Pitágoras,
	 * multiplicando el resultado angular por el radio medio terrestre
	 */
	@Override
	public double calcularKm(Tienda origen, Tienda destino) {
		if (origen == null || destino == null) {
            throw new IllegalArgumentException(
                    "Las tiendas no pueden ser null");
        }
		//Para una búsqueda local con un máximo de 10 km, hemos usado una aproximación plana lo que implica
		// convertir las diferencias de latitud y longitud a radianes y calcular la distancia con Pitágoras
		double latitudOrigen = Math.toRadians(origen.getLatitud());
	    double latitudDestino = Math.toRadians(destino.getLatitud());

	    double diferenciaLatitud = latitudDestino - latitudOrigen;
	    double diferenciaLongitud = Math.toRadians(destino.getLongitud() - origen.getLongitud());

	    double latitudMedia = (latitudOrigen + latitudDestino) / 2.0;
	    double diferenciaEsteOeste = diferenciaLongitud * Math.cos(latitudMedia);

	    return RADIO_TERRESTRE_KM * Math.sqrt(
	            diferenciaLatitud * diferenciaLatitud + 
	            diferenciaEsteOeste * diferenciaEsteOeste);
	}

}
