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
	Producto producto_1 = new Producto("P002", "Camisa", "Negra", "Camisa", "Hombre");
	
	private final LocalDateTime solicitud = LocalDateTime.of(2026, 10, 22, 10, 0);
	
	ValidadorPeticion validador;
	RestriccionesBusqueda restricciones;
	
	
	@BeforeEach
	public void setUp() {
		validador = new ValidadorPeticion();
	}

    @Test
    void testValidaPeticionUnaLinea() {
        Peticion peticion = nuevaPeticion();
        peticion.agregarLinea(new LineaPeticion(producto, 2)); 
        assertTrue(validador.validarPeticion(peticion));//linea valida
        
        assertFalse(validador.validarPeticion(null));//petició null
        assertFalse(validador.validarPeticion(nuevaPeticion()));//petició vacia
        
        Peticion conLineaNula = nuevaPeticion();
        //conLineaNula.agregarLinea(null); --> linea no puede ser null
        conLineaNula.getLineas().add(null);
        assertFalse(validador.validarPeticion(conLineaNula));//petició con linea a null
    }
    
    
    @Test
    void testValidaPeticionDosLineas() {
        Peticion peticion = nuevaPeticion();
        peticion.agregarLinea(new LineaPeticion(producto, 2)); 
        
        peticion.getLineas().add(null);
        assertFalse(validador.validarPeticion(peticion));//linea valida y linea a null        
    }
    
    private Peticion nuevaPeticion() {
        return new Peticion("R001", solicitud);
    }
    
    @Test 
    void testReposicionValida() {
    	Peticion peticion = nuevaPeticion();
    	LineaPeticion linea = new LineaPeticion(producto, 3);
    	peticion.agregarLinea(linea);
    	Reposicion reposicion = new Reposicion(producto, 3, solicitud.plusMinutes(120));
    	
    	assertTrue(validador.validarReposicion(peticion, linea, reposicion, restricciones);
    	
    }

}