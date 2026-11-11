package model.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Inventario {
	
	private final Map<Producto, Integer> stock;
	private final Map<Producto, Integer> stockMaximo;
	
	public Inventario() {
		this.stock = new HashMap<>();
		this.stockMaximo = new HashMap<>();
	}
	
	public void añadirProductos(Producto producto, int cantidad) {
		validarProducto(producto);
		if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad ha de ser positiva");
        }
		int stockActual = consultarStock(producto);
        long stockResultante = (long) stockActual + cantidad;
        Integer maximoPermitido = stockMaximo.get(producto);
        // Si se ha definido un máximo, también se aplica cuando vale cero
        if (maximoPermitido != null && stockResultante > maximoPermitido) {
            throw new IllegalArgumentException("La cantidad supera el stock máximo permitido");
        }
        if (stockResultante > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("La cantidad supera el máximo representable");
        }
        stock.merge(producto, cantidad, Integer::sum);
        
    }
	
	public void retirarStock(Producto producto, int cantidad) {
		validarProducto(producto);
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
		validarProducto(producto);
		return stock.getOrDefault(producto, 0);
	}
	
	public void setStockMaximo(Producto producto, int maximo) {
		validarProducto(producto);
		if (maximo < 0) {
            throw new IllegalArgumentException("El stock máximo no puede ser negativo");
        }
        if (consultarStock(producto) > maximo) {
            throw new IllegalArgumentException("El stock máximo no puede ser inferior al stock actual");
        }
        stockMaximo.put(producto, maximo);
	}
	
	public int consultarStockMaximo(Producto producto) {
		validarProducto(producto);
        return stockMaximo.getOrDefault(producto, 0);
    }

	
	public boolean hayStockSuficiente(Producto producto, int cantidad) {
		validarProducto(producto);
		if (cantidad < 0) {
			throw new IllegalArgumentException("La cantidad no podue ser negativa");
		    }

	    return consultarStock(producto) >= cantidad;
	}

	public List<Producto> productosConStockSuficiente(int cantidad) {
		return null;
	}

	private void validarProducto(Producto producto) {
		if (producto == null) {
			throw new IllegalArgumentException("El producto no puede ser null");
		}
	}
	
	
}
