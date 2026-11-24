package view;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.AlternativaOtraTienda;
import model.domain.Alternativa;
import model.domain.AlternativaProductoSimilar;
import model.domain.AlternativaReposicion;
import model.domain.EstadoResolucion;
import model.domain.Inventario;
import model.domain.LineaPeticion;
import model.domain.Producto;
import model.domain.Reposicion;
import model.domain.ResultadoBusqueda;
import model.domain.ResultadoLineaPeticion;
import model.domain.Tienda;

class VistaLocalizadorStockTest {
	private ByteArrayOutputStream contenido;
	private VistaLocalizadorStock vista;

	@BeforeEach
	void setUp() {
		contenido = new ByteArrayOutputStream();
		vista = new VistaLocalizadorStock(new PrintStream(contenido, true, StandardCharsets.UTF_8));
	}

	@Test
	void constructorPorDefecto() {
		// Statement coverage: ejecuta el constructor que delega en System.out.
		assertDoesNotThrow(() -> new VistaLocalizadorStock());
	}

	@Test
	void VistaLocalizadorStockTest() {
		//definición productos y tienda
		Producto camisa = producto("P001", "Camisa");
		Producto pantalon = producto("P002", "Pantalon");
		Producto abrigo = producto("P003", "Abrigo");
		Producto vestido = producto("P004", "Vestido");
		Producto chaqueta = producto("P005", "Chaqueta azul");
		Tienda centro = tienda("T001", "Centro");
		Tienda norte = tienda("T002", "Norte");
		Tienda sur = tienda("T003", "Sur");
		//definición de alternativas
		AlternativaOtraTienda otraTienda = new AlternativaOtraTienda(norte, 15, 2.5);
		AlternativaReposicion reposicion = new AlternativaReposicion(centro,
				new Reposicion(abrigo, 3, LocalDateTime.of(2026, 11, 18, 11, 0)),
				60, 0.0);
		AlternativaProductoSimilar similar = new AlternativaProductoSimilar(
				chaqueta, sur, 100.0, 20, 4.0);
		//Resultado de Búsqueda 
				ResultadoBusqueda resultado = new ResultadoBusqueda(List.of(
						// Caso: Se resuelve en la tienda actual (Stock suficiente)
						new ResultadoLineaPeticion(linea(camisa, 2), List.of(), null, EstadoResolucion.RESUELTA),
						// Caso: No tiene solución (Sin stock ni alternativas)
						new ResultadoLineaPeticion(linea(pantalon, 1), List.of(), null, EstadoResolucion.SIN_SOLUCION),
						// Caso: Alternativa propuesta recoger en otra tienda
						new ResultadoLineaPeticion(linea(vestido, 1), List.of(otraTienda), otraTienda,
								EstadoResolucion.ALTERNATIVA_PROPUESTA),
						// Caso: Alternativa propuesta de esperar reposición
						new ResultadoLineaPeticion(linea(abrigo, 3), List.of(reposicion), reposicion,
								EstadoResolucion.ALTERNATIVA_PROPUESTA),
						// Caso: Alternativa propuesta de producto similar
						new ResultadoLineaPeticion(linea(chaqueta, 1), List.of(similar), similar,
								EstadoResolucion.ALTERNATIVA_PROPUESTA)
				));
				vista.mostrarResultado(resultado);

				// Verificaciones por cada caso
				String texto = contenido.toString(StandardCharsets.UTF_8);
				// Caso: se resuelve en tienda actual
				assertTrue(texto.contains("Camisa x 2: resuelta en la tienda actual."));
				// Caso: no tiene solución
				assertTrue(texto.contains("Pantalon x 1: no se encontró una solución."));
				// Caso: alternativa de otra tienda
				assertTrue(texto.contains("recoger en Norte (15 min, 2.5 km)"));
				// Caso: alternativa de reposición
				assertTrue(texto.contains("esperar reposición en Centro (60 min, 3 unidades, 18/11/2026 11:00)"));
				// Caso: alternativa de producto similar
				assertTrue(texto.contains("producto similar: Chaqueta azul en Sur (100.0%, 20 min, 4.0 km)"));
}

	@Test
	void muestraAlternativaGenerica() {
		// Statement coverage: ejecuta el último return de describir para un subtipo 
		Alternativa generica = new Alternativa(7, 1.5) {};
		ResultadoBusqueda resultado = new ResultadoBusqueda(List.of(
				new ResultadoLineaPeticion(linea(producto("P006", "Producto"), 2),
						List.of(generica), generica, EstadoResolucion.ALTERNATIVA_PROPUESTA)));

		vista.mostrarResultado(resultado);

		assertTrue(contenido.toString(StandardCharsets.UTF_8)
				.contains("alternativa propuesta (7 min, 1.5 km)."));
	}

	private Producto producto(String codigo, String nombre) {
		return new Producto(codigo, nombre, "Azul", "Ropa", "Unisex");
	}

	private Tienda tienda(String id, String nombre) {
		return new Tienda(id, nombre, 41.0, 2.0, new Inventario());
	}

	private LineaPeticion linea(Producto producto, int cantidad) {
		return new LineaPeticion(producto, cantidad);
	}
}
