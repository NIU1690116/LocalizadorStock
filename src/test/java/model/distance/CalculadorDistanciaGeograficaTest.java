package model.distance;

import model.domain.Inventario;
import model.domain.Tienda;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de entradas y cálculo de CalculadorDistanciaGeografica
 * Combinan particiones de argumentos nulos con caja negra sobre distancias de
 * referencia, ubicación coincidente y simetría del cálculo
 */
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
		// Particiones inválidas, DbC: se rechazan origen y destino nulos, juntos o por separado
		assertThrows(IllegalArgumentException.class, 
				() -> calculador.calcularKm(null, null));
		assertThrows(IllegalArgumentException.class, 
				() -> calculador.calcularKm(null, destino));
		assertThrows(IllegalArgumentException.class, 
				() -> calculador.calcularKm(origen, null));
	}
	
	@Test
	void testCalcularKm() {
		// Límite inferior: dos tiendas con coordenadas iguales tienen distancia cero
		Tienda origen_1 = tienda("T1", 41.0, 2.0);
        Tienda destino_2 = tienda("T2", 41.0, 2.0);

        assertEquals(0.0, calculador.calcularKm(origen_1, destino_2), 0.000001);
        
        // Caso de referencia: un grado de longitud en el ecuador equivale aproximadamente a 111.195 km
        double distancia = calculador.calcularKm(origen, destino);

        assertEquals(111.195, distancia, 0.1);
        
        // Propiedad de simetría: intercambiar origen y destino no cambia la distancia
        Tienda primera = tienda("T1", 41.0, 2.0);
        Tienda segunda = tienda("T2", 40.0, 3.0);

        double primeraASegunda = calculador.calcularKm(primera, segunda);
        double segundaAPrimera = calculador.calcularKm(segunda, primera);

        assertEquals(primeraASegunda, segundaAPrimera, 0.000001);
	}
	
	/**
	 * Reduce la repetición al construir tiendas de prueba con coordenadas variables
	 * e inventario vacío, irrelevante para el cálculo geográfico
	 */
	private Tienda tienda(String id, double latitud, double longitud) {
        return new Tienda(
                id, id, latitud, longitud, new Inventario());
    }

}
