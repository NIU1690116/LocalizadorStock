package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventario {
	
	private final Map<Producto, Integer> stock;
	private final Map<Producto, Integer> stockMaximo;
	
	public Inventario() {
		this.stock = new HashMap<>();
		this.stockMaximo = new HashMap<>();
	}
	
	public void añadirProductos(Producto producto, int cantidad) {
		if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad ha de ser positiva");
        }
		int stockActual = consultarStock(producto);
        int maximoPermitido = consultarStockMaximo(producto);
        // Si hay un stock máximo definido comprobamos que no se supere
        if (maximoPermitido > 0 && (stockActual + cantidad) > maximoPermitido) {
            throw new IllegalArgumentException("La cantidad supera el stock máximo permitido");
        }// si no se supera lo podemos añadir
        stock.merge(producto, cantidad, Integer::sum);
        
    }
	
	public void retirarStock(Producto producto, int cantidad) {
		if (cantidad <= 0) {
	        throw new IllegalArgumentException("La quantitat ha de ser positiva");
		}
	    
	    if (!hayStockSuficiente(producto, cantidad)) {
	        throw new IllegalArgumentException("No hay suficiente stock disponible");
	    }
		int stockActual = consultarStock(producto);
	    stock.put(producto, stockActual - cantidad);
		
	}
	
	public int consultarStock(Producto producto) {
		return stock.getOrDefault(producto, 0);
	}
	
	public void setStockMaximo(Producto producto, int maximo) {
		if (maximo < 0) {
            throw new IllegalArgumentException("El stock máximo no puede ser negativo");
        }
        stockMaximo.put(producto, maximo);
	}
	
	public int consultarStockMaximo(Producto producto) {
        return stockMaximo.getOrDefault(producto, 0);
    }

	
	public boolean hayStockSuficiente(Producto producto, int cantidad) {
		if (cantidad < 0) {
			throw new IllegalArgumentException("La cantidad no podue ser negativa");
		    }

	    return consultarStock(producto) >= cantidad;
	}
	
	
}
