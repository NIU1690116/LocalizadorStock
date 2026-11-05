package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculadorDistanciaGeograficaTest {
	CalculadorDistanciaGeografica calculador;
	Tienda origen;
	Tienda destino;

	@BeforeEach
	void setUp() throws Exception {
		calculador = new CalculadorDistanciaGeografica();
		origen = tienda("O", 0.0, 0.0);
        destino = tienda("D", 0.0, 1.0);
	}

	@Test
	void testCalcularKmTiendaNulas() {
		//destino y origen no pueden ser null
		assertThrows(IllegalArgumentException.class, 
				() -> calculador.calcularKm(null, null));
		assertThrows(IllegalArgumentException.class, 
				() -> calculador.calcularKm(null, destino));
		assertThrows(IllegalArgumentException.class, 
				() -> calculador.calcularKm(origen, null));
	}
	
	@Test
	void testCalcularKm() {
		//misma ubicación -> distancia es igual a 0
		Tienda primera = tienda("T1", 41.0, 2.0);
        Tienda segunda = tienda("T2", 41.0, 2.0);

        assertEquals(0.0, calculador.calcularKm(primera, segunda), 0.000001);
	}
	
	private Tienda tienda(String id, double latitud, double longitud) {
        return new Tienda(
                id, id, latitud, longitud, new Inventario());
    }

}
