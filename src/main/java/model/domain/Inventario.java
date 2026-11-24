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
		assert invariant() : "El inventario recién creado debe ser válido";
	}
	
	public void añadirProductos(Producto producto, int cantidad) {
		assert invariant() : "El inventario debe ser válido antes de añadir productos";
		// Precondiciones de entrada: producto no nulo y cantidad positiva.
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
		assert invariant() : "Añadir productos no debe romper el invariante del inventario";
    }
	
	public void retirarStock(Producto producto, int cantidad) {
		assert invariant() : "El inventario debe ser válido antes de retirar stock";
		// Precondiciones de entrada: producto no nulo, cantidad positiva y stock suficiente.
		validarProducto(producto);
		if (cantidad <= 0) {
	        throw new IllegalArgumentException("La quantitat ha de ser positiva");
		}
	    
	    if (!hayStockSuficiente(producto, cantidad)) {
	        throw new IllegalArgumentException("No hay suficiente stock disponible");
	    }
		int stockActual = consultarStock(producto);
	    stock.put(producto, stockActual - cantidad);
		assert invariant() : "Retirar stock no debe romper el invariante del inventario";
	}
	
	public int consultarStock(Producto producto) {
		// Precondición: el producto no puede ser null; puede no estar registrado.
		validarProducto(producto);
		// Postcondición: se devuelve el stock registrado, o cero si no está registrado.
		return stock.getOrDefault(producto, 0);
	}
	
	public void setStockMaximo(Producto producto, int maximo) {
		assert invariant() : "El inventario debe ser válido antes de cambiar el máximo";
		// Precondiciones de entrada: producto no nulo y máximo no negativo ni inferior al stock actual.
		validarProducto(producto);
		if (maximo < 0) {
            throw new IllegalArgumentException("El stock máximo no puede ser negativo");
        }
        if (consultarStock(producto) > maximo) {
            throw new IllegalArgumentException("El stock máximo no puede ser inferior al stock actual");
        }
        stockMaximo.put(producto, maximo);
		assert invariant() : "Cambiar el máximo no debe romper el invariante del inventario";
	}
	
	public int consultarStockMaximo(Producto producto) {
		// Precondición: el producto no puede ser null; puede no estar registrado.
		validarProducto(producto);
		// Postcondición: se devuelve el máximo registrado, o cero si no se configuró.
        return stockMaximo.getOrDefault(producto, 0);
    }

	
	public boolean hayStockSuficiente(Producto producto, int cantidad) {
		// Precondiciones: producto no nulo y cantidad no negativa.
		validarProducto(producto);
		if (cantidad < 0) {
			throw new IllegalArgumentException("La cantidad no podue ser negativa");
		    }

		// Postcondición: true exactamente cuando el stock registrado alcanza la cantidad.
	    return consultarStock(producto) >= cantidad;
	}

	public List<Producto> productosConStockSuficiente(int cantidad) {
		// Precondición: la cantidad consultada debe ser positiva.
		if (cantidad <= 0) {
			throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
		}
		// Postcondición: solo se devuelven productos cuyo stock alcanza la cantidad pedida.
		return stock.entrySet().stream()
				.filter(entrada -> entrada.getValue() >= cantidad)
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
	}

	private void validarProducto(Producto producto) {
		if (producto == null) {
			throw new IllegalArgumentException("El producto no puede ser null");
		}
	}

	private boolean invariant() {
		return estadoInternoValido(stock, stockMaximo);
	}

	// Visibilidad de paquete para probar la regla sin modificar los campos privados del inventario.
	static boolean estadoInternoValido(Map<Producto, Integer> stock,
			Map<Producto, Integer> stockMaximo) {
		if (stock == null || stockMaximo == null) {
			return false;
		}

		for (Map.Entry<Producto, Integer> entrada : stock.entrySet()) {
			Producto producto = entrada.getKey();
			Integer cantidad = entrada.getValue();
			if (producto == null || cantidad == null || cantidad < 0) {
				return false;
			}

			Integer maximo = stockMaximo.get(producto);
			if (maximo != null && cantidad > maximo) {
				return false;
			}
		}

		for (Map.Entry<Producto, Integer> entrada : stockMaximo.entrySet()) {
			Producto producto = entrada.getKey();
			Integer maximo = entrada.getValue();
			if (producto == null || maximo == null || maximo < 0) {
				return false;
			}
			
		}
		return true;
	}

}
