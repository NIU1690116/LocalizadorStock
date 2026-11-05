package model;

public class CalculadorDistanciaGeografica implements CalculadorDistancia {

	@Override
	public double calcularKm(Tienda origen, Tienda destino) {
		if (origen == null || destino == null) {
            throw new IllegalArgumentException(
                    "Las tiendas no pueden ser null");
        }
		return 0;
	}
	

}
