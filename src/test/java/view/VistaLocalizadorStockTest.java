package view;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.AlternativaOtraTienda;
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
	void VistaLocalizadorStockTest() {
		
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
