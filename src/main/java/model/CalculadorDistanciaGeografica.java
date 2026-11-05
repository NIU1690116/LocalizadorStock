package model;

public class CalculadorDistanciaGeografica implements CalculadorDistancia {

	private static final double RADIO_TERRESTRE_KM = 6371.0088;

	@Override
	public double calcularKm(Tienda origen, Tienda destino) {
		if (origen == null || destino == null) {
            throw new IllegalArgumentException(
                    "Las tiendas no pueden ser null");
        }
		
		// Si están en la misma latitud y longitud la diferencia es 0
	    if (origen.getLatitud() == destino.getLatitud() && 
	        origen.getLongitud() == destino.getLongitud()) {
	        return 0.0;
	    }
	    return 0.0;
	}
	
	

}
