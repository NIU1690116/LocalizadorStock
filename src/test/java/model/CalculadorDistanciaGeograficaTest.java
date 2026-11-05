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
	void testCalcularKm() {
		//destino y origen no pueden ser null
		assertThrows(IllegalArgumentException.class, 
				() -> calculador.calcularKm(null, null));
		
	}
	
	private Tienda tienda(String id, double latitud, double longitud) {
        return new Tienda(
                id, id, latitud, longitud, new Inventario());
    }

}
