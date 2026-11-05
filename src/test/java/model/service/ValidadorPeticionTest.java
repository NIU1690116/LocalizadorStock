package model.service;

import model.domain.LineaPeticion;
import model.domain.Peticion;
import model.domain.Producto;
import model.domain.Reposicion;
import model.domain.RestriccionesBusqueda;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidadorPeticionTest {
	Producto producto;
	Producto producto_1;
	
	private final LocalDateTime solicitud = LocalDateTime.of(2026, 10, 22, 10, 0);
	
	ValidadorPeticion validador;
	RestriccionesBusqueda restricciones;
	
	@BeforeEach
	public void setUp() {
		producto = new Producto("P001", "Camisa", "Rojo", "Camisa", "Hombre");
		producto_1 = new Producto("P002", "Camisa", "Negra", "Camisa", "Hombre");
		validador = new ValidadorPeticion();
		restricciones = new RestriccionesBusqueda();
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
    
    @Test 
    void testReposicionValida() {
    	Peticion peticion = nuevaPeticion();
    	LineaPeticion linea = new LineaPeticion(producto, 3);
    	peticion.agregarLinea(linea);
    	Reposicion reposicion = new Reposicion(producto, 3, solicitud.plusMinutes(120));//en el limite de espera
    	
    	assertTrue(validador.validarReposicion(peticion, linea, reposicion, restricciones));
    	
    	Reposicion reposicion_1 = new Reposicion(producto, 1, solicitud.minusMinutes(1));//fecha anterior al plazo
        assertFalse(validador.validarReposicion(peticion, linea, reposicion_1, restricciones));
        
        Reposicion reposicion_2 = new Reposicion(producto, 2, solicitud.minusMinutes(121));//fecha posterior al plazo
        assertFalse(validador.validarReposicion(peticion, linea, reposicion_2, restricciones)); 
    }
    
    @Test
    void testArgumentosNulos() {//particiones equivalentes
    	Peticion peticion = nuevaPeticion();
        LineaPeticion linea = new LineaPeticion(producto, 1);
        peticion.agregarLinea(linea);
        Reposicion reposicion = new Reposicion(producto, 1, solicitud.minusMinutes(1));

        assertFalse(validador.validarReposicion(null, linea, reposicion, restricciones));
        assertFalse(validador.validarReposicion(peticion, null, reposicion, restricciones));
        assertFalse(validador.validarReposicion(peticion, linea, reposicion, null));
        assertFalse(validador.validarReposicion(peticion, linea, null, restricciones));
    }
    
    @Test
    void testValidarReposion() {
        Peticion peticion = nuevaPeticion();
        LineaPeticion linea = new LineaPeticion(producto, 3);
        peticion.agregarLinea(linea);
        
        //reposición de producto solicitado, cantidad sufiente
        Reposicion reposicion_1 =  new Reposicion(producto, 3, solicitud.plusMinutes(30));
        assertTrue(validador.validarReposicion(peticion, linea, reposicion_1, restricciones));

        //reposición de producto solicitado, pero cantidad insufiente
        Reposicion reposicion_2 =  new Reposicion(producto, 2, solicitud.plusMinutes(30));
        assertFalse(validador.validarReposicion(peticion, linea, reposicion_2, restricciones));
        
      //reposición de producto no solicitado
        Reposicion reposicion_3 = new Reposicion(producto_1, 3, solicitud.plusMinutes(30));
        assertFalse(validador.validarReposicion(peticion, linea, reposicion_3, restricciones));
        
    }
    
    private Peticion nuevaPeticion() {
        return new Peticion("R001", solicitud);
    }

}