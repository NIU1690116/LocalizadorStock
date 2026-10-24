package test.java.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.model.LineaPeticion;
import main.java.model.Peticion;
import main.java.model.Producto;
import main.java.model.Reposicion;
import main.java.model.RestriccionesBusqueda;
import main.java.model.ValidadorPeticion;

class ValidadorPeticionTest {
	Producto producto = new Producto("P001", "Camisa", "Rojo", "Camisa", "Hombre");
	private final LocalDateTime solicitud = LocalDateTime.of(2026, 10, 22, 10, 0);
	
	ValidadorPeticion validador;
	
	@BeforeEach
	public void setUp() {
		validador = new ValidadorPeticion();
	}

    @Test
    void testValidaPeticionLineasValidas() {
        Peticion peticion = nuevaPeticion();
        peticion.agregarLinea(new LineaPeticion(producto, 2)); 
        assertTrue(validador.validarPeticion(peticion));//linea valida
        
        assertFalse(validador.validarPeticion(null));//petició null
        assertFalse(validador.validarPeticion(nuevaPeticion()));//petició vacia
        
        Peticion conLineaNula = nuevaPeticion();
        conLineaNula.getLineas().add(null);
        assertFalse(validador.validarPeticion(conLineaNula));//petició con linea a null
    }
    
    private Peticion nuevaPeticion() {
        return new Peticion("R001", solicitud);
    }

}