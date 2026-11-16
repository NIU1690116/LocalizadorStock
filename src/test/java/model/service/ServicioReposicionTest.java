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
	
	@Test
	void testConsultaReposicion() {
		Reposicion esperada = new Reposicion(producto, 4, LocalDateTime.of(2026, 11, 16, 12, 0));
		when(proveedor.consultar(tienda, producto)).thenReturn(esperada);

		Reposicion resultado = servicio.consultarReposicion(tienda, producto);

		assertSame(esperada, resultado);
		verify(proveedor).consultar(tienda, producto);
		
		//propagar reposición consultada sin alterar datos
		Reposicion reposicion = new Reposicion(producto, 2, LocalDateTime.of(2026, 11, 16, 13, 30));
		when(proveedor.consultar(tienda, producto)).thenReturn(reposicion);

		Reposicion resultado_2 = servicio.consultarReposicion(tienda, producto);

		assertEquals(producto, resultado.getProducto());
		assertEquals(2, resultado.getCantidad());
		assertEquals(LocalDateTime.of(2026, 11, 16, 13, 30), resultado_2.getFechaPrevista());
	}
	

}
