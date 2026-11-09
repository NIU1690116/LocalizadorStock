package model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.AlternativaProductoSimilar;
import model.domain.Inventario;
import model.domain.LineaPeticion;
import model.domain.Producto;
import model.domain.Tienda;

class BuscadorProductosSimilaresTest {
	private BuscadorProductosSimilares buscador;
	private Producto solicitado;

	@BeforeEach
	void setUp() {
		buscador = new BuscadorProductosSimilares();
		solicitado = new Producto("P001", "Camiseta", "rojo", "camisetas", "mujer");
	}


	@Test
	void testArgumentosObligatoriosNulos() {

	}

}
