package model.repository;

import model.domain.Inventario;
import model.domain.Producto;
import model.domain.Tienda;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio mock que guarda tiendas en memoria
 * Facilita construir escenarios controlados sin una fuente de datos externa y
 * devuelve copias de la colección para que los consumidores no alteren su lista interna
 */
public class RepositorioTiendasMock implements RepositorioTiendas {
    private List<Tienda> tiendas = new ArrayList<>();
    
	/**
	 * Devuelve las tiendas preparadas para el escenario de prueba.
	 */
    @Override
    public List<Tienda> obtenerTiendas() {
        return new ArrayList<>(tiendas);
    } 
    
	/**
	 * Crea y registra una tienda de prueba con inventario vacío
	 */
    public Tienda crearTienda(String id, String nombre) {
        Tienda tienda = new Tienda(
                id, nombre, 41.0, 2.0, new Inventario());
        tiendas.add(tienda);
        return tienda;
    }
    
	/**
	 * Crea y registra una tienda de prueba cuyo inventario contiene el producto indicado
	 */
	public Tienda crearTiendaConStock(
            String id, String nombre, Producto producto, int cantidad) {
        Inventario inventario = new Inventario();
        inventario.añadirProductos(producto, cantidad);

        Tienda tienda = new Tienda(
                id, nombre, 41.0, 2.0, inventario);
        tiendas.add(tienda);
        return tienda;
    }
}
