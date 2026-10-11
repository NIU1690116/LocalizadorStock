package main.java.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventario {
	
	private final Map<Producto, Integer> stock;
	
	public Inventario() {
		this.stock = new HashMap<>();
	}
	
	public void añadirProductos(Producto producto, int cantidad) {
		if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad ha de ser positiva");
        }
        stock.merge(producto, cantidad, Integer::sum);
    }
	
	public void retirarStock(Producto producto, int cantidad) {
		if (cantidad <= 0) {
	        throw new IllegalArgumentException("La quantitat ha de ser positiva");
		}
	    
	    if (!tieneStock(producto, cantidad)) {
	        throw new IllegalArgumentException("No hay suficiente stock disponible");
	    }
		int stockActual = consultarStock(producto);
	    stock.put(producto, stockActual - cantidad);
		
	}
	
	public int consultarStock(Producto producto) {
		return stock.getOrDefault(producto, 0);
	}
	
	public boolean tieneStock(Producto producto, int cantidad) {
		if (cantidad < 0) {
			throw new IllegalArgumentException("La cantidad no podue ser negativa");
		    }

	    return consultarStock(producto) >= cantidad;
	}
	
	
}
