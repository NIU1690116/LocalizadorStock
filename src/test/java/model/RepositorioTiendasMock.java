package model;

import java.util.ArrayList;
import java.util.List;

public class RepositorioTiendasMock implements RepositorioTiendas {
    private List<Tienda> tiendas = new ArrayList<>();
    
    @Override
    public List<Tienda> obtenerTiendas() {
        return new ArrayList<>(tiendas);
    } 
    
    public Tienda crearTienda(String id, String nombre) {
        Tienda tienda = new Tienda(
                id, nombre, 41.0, 2.0, new Inventario());
        tiendas.add(tienda);
        return tienda;
    }
    
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
