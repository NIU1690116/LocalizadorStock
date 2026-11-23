package model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.Alternativa;
import model.domain.AlternativaOtraTienda;
import model.domain.AlternativaReposicion;
import model.domain.Inventario;
import model.domain.Producto;
import model.domain.Reposicion;
import model.domain.RestriccionesBusqueda;
import model.domain.Tienda;

class EvaluadorAlternativasTest {
	private EvaluadorAlternativas evaluador;
	private Tienda tienda;
	private Reposicion reposicion;

	@BeforeEach
	void setUp() {
		evaluador = new EvaluadorAlternativas(new PoliticaBusqueda(), new RestriccionesBusqueda());
		tienda = new Tienda("T001", "Tienda Centro", 41.0, 2.0, new Inventario());
		Producto producto = new Producto("P001", "Camiseta", "rojo", "camisetas", "mujer");
		reposicion = new Reposicion(producto, 2, LocalDateTime.of(2026, 11, 15, 10, 0));
	}
	
	@Test
	void testPoliticaYRestriccionesValidas() {
		assertThrows(IllegalArgumentException.class,
				() -> new EvaluadorAlternativas(null, new RestriccionesBusqueda()));
		assertThrows(IllegalArgumentException.class,
				() -> new EvaluadorAlternativas(new PoliticaBusqueda(), null));
	}
	
	@Test
	void tesListaAlternativaInvalida() {
		// Loop testing: cero iteraciones con la lista vacía
		assertThrows(IllegalArgumentException.class,//lista nula
				() -> evaluador.seleccionarMejor(null));
		assertNull(evaluador.seleccionarMejor(List.of()));//null si no hay alternativas(lista vacia)
	}
	
	@Test
	void testCumpleRestricciones() {
		// Loop testing: una iteración y varias alternativas en la lista
		//desplazamiento tiene un peso del 30% y el de espera de 70%
		//el dezplazamiento tiene un porcentaje inferior de desfavorilidad
		Alternativa espera = new AlternativaReposicion(tienda, reposicion, 120, 0.0);
		assertEquals(espera, evaluador.seleccionarMejor(List.of(espera)));
		Alternativa desplazamiento = new AlternativaOtraTienda(tienda, 10, 2.0);
		assertEquals(desplazamiento, evaluador.seleccionarMejor(List.of(espera, desplazamiento)));
		
		Alternativa lejos = new AlternativaOtraTienda(tienda, 5, 10.1);
		Alternativa esperaExcesiva = new AlternativaReposicion(tienda, reposicion, 121, 0.0);
		assertNull(evaluador.seleccionarMejor(List.of(lejos, esperaExcesiva)));
		
		Alternativa menosTiempo = new AlternativaOtraTienda(tienda, 0, 7.0);
		Alternativa menosDistancia = new AlternativaOtraTienda(tienda, 36, 0.0);
		assertEquals(menosTiempo, evaluador.seleccionarMejor(List.of(menosDistancia, menosTiempo)));
	}

	@Test
	void testSeleccionarMejorAlternativa() {
		Alternativa mejor = new AlternativaOtraTienda(tienda, 5, 1.0);//menor porcentaje penalización
		Alternativa peor = new AlternativaOtraTienda(tienda, 15, 2.0);//mayor porcentaje de penalización
		Alternativa fueraDeRango = new AlternativaOtraTienda(tienda, 1, 10.1);
		List<Alternativa> candidatas = new ArrayList<>();
		candidatas.add(null);
		candidatas.add(fueraDeRango);
		candidatas.add(peor);
		candidatas.add(mejor);

		assertEquals(mejor, evaluador.seleccionarMejor(candidatas));
		assertNull(evaluador.seleccionarMejor(List.of(fueraDeRango)));
	}

	@Test
	void testDesempataPorMenorTiempoCuandoLaPuntuacionEsIgual() {
		// Las dos alternativas puntúan exactamente igual; debe ganar la de menor tiempo.
		Alternativa mayorTiempo = new AlternativaOtraTienda(tienda, 108, 0.0);
		Alternativa menorTiempo = new AlternativaOtraTienda(tienda, 72, 7.0);
		PoliticaBusqueda politica = new PoliticaBusqueda();
		assertEquals(politica.calcularPuntuacion(mayorTiempo), politica.calcularPuntuacion(menorTiempo), 0.0);

		assertEquals(menorTiempo, evaluador.seleccionarMejor(List.of(mayorTiempo, menorTiempo)));
	}

	@Test
	void estDesempataPorMenorDistanciaCuandoTiempoYPuntuacionEmpatan() {
		// A 120 minutos, estas distancias adyacentes producen la misma puntuación double.
		Alternativa mayorDistancia = new AlternativaOtraTienda(tienda, 120, Math.nextUp(1.0));
		Alternativa menorDistancia = new AlternativaOtraTienda(tienda, 120, 1.0);
		PoliticaBusqueda politica = new PoliticaBusqueda();
		assertEquals(politica.calcularPuntuacion(mayorDistancia), politica.calcularPuntuacion(menorDistancia), 0.0);

		assertEquals(menorDistancia, evaluador.seleccionarMejor(List.of(mayorDistancia, menorDistancia)));
	}

	@Test
	void tetConservaLaPrimeraAlternativaSiElTiempoDelEmpateNoMejora() {
		// Cubre el empate de puntuación con mayor tiempo y el empate exacto sin mejora de distancia.
		Alternativa menorTiempo = new AlternativaOtraTienda(tienda, 72, 7.0);
		Alternativa mayorTiempo = new AlternativaOtraTienda(tienda, 108, 0.0);
		Alternativa duplicada = new AlternativaOtraTienda(tienda, 72, 7.0);

		assertEquals(menorTiempo, evaluador.seleccionarMejor(List.of(menorTiempo, mayorTiempo)));
		assertEquals(menorTiempo, evaluador.seleccionarMejor(List.of(menorTiempo, duplicada)));
	}

	@Test
	void postcondicionDetectaUnaSeleccionQueNoEsLaMejor() throws ReflectiveOperationException {
		// Statement coverage: explora los retornos false de la postcondición con candidatos deliberadamente incorrectos
		Method postcondicion = EvaluadorAlternativas.class.getDeclaredMethod(
				"cumplePostcondicion", List.class, Alternativa.class);
		postcondicion.setAccessible(true);

		Alternativa seleccionNoIncluida = new AlternativaOtraTienda(tienda, 5, 1.0);
		Alternativa candidata = new AlternativaOtraTienda(tienda, 10, 2.0);
		assertFalse((boolean) postcondicion.invoke(evaluador, List.of(candidata), seleccionNoIncluida));

		Alternativa peor = new AlternativaOtraTienda(tienda, 15, 2.0);
		Alternativa mejor = new AlternativaOtraTienda(tienda, 5, 1.0);
		assertFalse((boolean) postcondicion.invoke(evaluador, List.of(peor, mejor), peor));

		Alternativa másTarde = new AlternativaOtraTienda(tienda, 108, 0.0);
		Alternativa másPronto = new AlternativaOtraTienda(tienda, 72, 7.0);
		assertFalse((boolean) postcondicion.invoke(evaluador, List.of(másTarde, másPronto), másTarde));

		Alternativa másLejos = new AlternativaOtraTienda(tienda, 120, Math.nextUp(1.0));
		Alternativa másCerca = new AlternativaOtraTienda(tienda, 120, 1.0);
		assertFalse((boolean) postcondicion.invoke(evaluador, List.of(másLejos, másCerca), másLejos));
	}

}
