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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Pruebas de validación de peticiones y reposiciones
 * Usa particiones de equivalencia para argumentos nulos, peticiones vacías y
 * líneas válidas o inválidas, además de pruebas de límite para la espera y la
 * cantidad
 */
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

    /**
     * Comprueba las particiones de petición nula, vacía y con una línea válida
     * Tipo: particiones de equivalencia, cubre además el bucle con cero y una
     * iteración y la aceptación de una petición válida --> caja negra
     */
    @Test
    void testValidaPeticionUnaLinea() {
        // Loop testing: caso de cero iteraciones (petición vacía) y una iteración (una línea válida)
        Peticion peticion = nuevaPeticion();
        peticion.agregarLinea(new LineaPeticion(producto, 2)); 
        assertTrue(validador.validarPeticion(peticion));//linea valida
        
        assertFalse(validador.validarPeticion(null));//petició null
        assertFalse(validador.validarPeticion(nuevaPeticion()));//petició vacia
    }
    
    /**
     * Comprueba la validación de una petición con varias líneas válidas.
     * Tipo: caja blanca, prueba de bucle con varias iteraciones, y caja negra
     * sobre la aceptación de la petición.
     */
    @Test
    void testValidaPeticionDosLineas() {
        // Loop testing: el bucle realiza varias iteraciones para validar todas las líneas
        Peticion peticion = nuevaPeticion();
        peticion.agregarLinea(new LineaPeticion(producto, 2)); 
        peticion.agregarLinea(new LineaPeticion(producto_1, 1));

        assertTrue(validador.validarPeticion(peticion));
    }

	/**
	 * Comprueba que una petición cuya lista contiene una línea nula se rechaza.
	 * Tipo: caja blanca, cobertura de la rama de rechazo de línea inválida, usa
	 * un mock para construir el estado normal de Peticion impide crear
	 */
	@Test
	void rechazaUnaLineaNulaDentroDeLaPeticion() {
		// Statement coverage: alcanza el return false interno al detectar una línea null
		Peticion peticionConLineaNula = mock(Peticion.class);
		when(peticionConLineaNula.getLineas()).thenReturn(java.util.Arrays.asList((LineaPeticion) null));

		assertFalse(validador.validarPeticion(peticionConLineaNula));
	}
    
    /**
     * Comprueba una reposición válida justo en el máximo de espera y el rechazo
     * de fechas anteriores a la solicitud.
     * Tipo: prueba de límite en 120 minutos y partición válida/no válida de fechas
     * Los dos casos rechazados están antes de la solicitud; este test no cubre
     * una fecha posterior al máximo permitido.
     */
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
    
    /**
     * Comprueba que se rechaza la reposición cuando falta cualquiera de los
     * argumentos obligatorios.
     * Tipo: particiones de equivalencia para cada argumento nulo, prueba de
     * (DbC) observada desde el resultado 
     */
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
    
    /**
     * Comprueba que la reposición debe corresponder al producto solicitado y
     * cubrir toda la cantidad requerida.
     * Tipo: particiones de equivalencia para producto coincidente/no coincidente
     * y cantidad suficiente/insuficiente.
     */
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
    
    //dependencias
    private Peticion nuevaPeticion() {
        return new Peticion("R001", solicitud);
    }

}
