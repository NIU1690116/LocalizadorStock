package model;

public class CalculadorDistanciaGeografica implements CalculadorDistancia {

	private static final double RADIO_TERRESTRE_KM = 6371.0088;

	@Override
	public double calcularKm(Tienda origen, Tienda destino) {
		if (origen == null || destino == null) {
            throw new IllegalArgumentException(
                    "Las tiendas no pueden ser null");
        }
		//Para una búsqueda local con un máximo de 10 km, usaremos una aproximación plana lo que implica
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
