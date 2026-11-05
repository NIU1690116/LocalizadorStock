package model;

public class CalculadorDistanciaGeografica implements CalculadorDistancia {

	private static final double RADIO_TERRESTRE_KM = 6371.0088;

	@Override
	public double calcularKm(Tienda origen, Tienda destino) {
		if (origen == null || destino == null) {
            throw new IllegalArgumentException(
                    "Las tiendas no pueden ser null");
        }
		
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
