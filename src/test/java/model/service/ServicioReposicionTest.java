package model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.Inventario;
import model.domain.Producto;
import model.domain.Reposicion;
import model.domain.Tienda;
import model.repository.ProveedorReposicion;

class ServicioReposicionTest {
	private ProveedorReposicion proveedor;
	private ServicioReposicion servicio;
	private Tienda tienda;
	private Producto producto;

	@BeforeEach
	void setUp() {
		proveedor = org.mockito.Mockito.mock(ProveedorReposicion.class);
		servicio = new ServicioReposicion(proveedor);
		tienda = new Tienda("T001", "Centro", 41.0, 2.0, new Inventario());
		producto = new Producto("P001", "Camisa", "Rojo", "Camisa", "Unisex");
	}

	@Test
	void testDependenciaYArgumentosNulos() {
		assertThrows(IllegalArgumentException.class, () -> new ServicioReposicion(null));
		assertThrows(IllegalArgumentException.class, () -> servicio.consultarReposicion(null, producto));
		assertThrows(IllegalArgumentException.class, () -> servicio.consultarReposicion(tienda, null));
	}

}
