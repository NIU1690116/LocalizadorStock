package model.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Gestiona el stock disponible y el máximo opcional de cada producto
 * Permite añadir y retirar unidades, consultar cantidades y listar productos
 * que alcanzan un mínimo
 */
public class Inventario {
	
	private final Map<Producto, Integer> stock;
	private final Map<Producto, Integer> stockMaximo;
	
	/**
	 * Crea un inventario sin productos ni máximos
	 * Devuelve: una instancia vacía de Inventario
	 * Lógica: inicializa los mapas de stock y stock máximo y comprueba mediante
	 * una aserción que el estado inicial sea válido.
	 */
	public Inventario() {
		this.stock = new HashMap<>();
		this.stockMaximo = new HashMap<>();
		assert invariant() : "El inventario recién creado debe ser válido";
	}
	
	/**
	 * Añade unidades al stock de un producto
	 * @param producto producto al que se añaden unidades
	 * @param cantidad unidades que se añadirán, debe ser positiva
	 * @throws IllegalArgumentException si el producto es nulo, la cantidad no es
	 * positiva, se excede el máximo configurado o el entero máximo 
	 * Lógica: valida los argumentos, calcula el stock resultante usando un tipo
	 * amplio para detectar desbordamiento, comprueba los límites y suma la cantidad
	 * Verifica mediante aserciones que se conserve la invariante.
	 */
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
	
	/**
	 * Retira unidades del stock de un producto
	 * @param producto producto del que se retira stock
	 * @param cantidad unidades que se retirarán; debe ser positiva
	 * @throws IllegalArgumentException si el producto es nulo, la cantidad no es
	 * positiva o el stock disponible es insuficiente
	 * Lógica: valida los argumentos, comprueba que haya unidades suficientes y
	 * actualiza el stock restando la cantidad, luego verifica la invariante mediante
	 * una aserción.
	 */
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
	
	/**
	 * Consulta las unidades disponibles de un producto
	 * @param producto producto que se consulta
	 * @return unidades registradas, o 0 si no está registrado
	 * @throws IllegalArgumentException si el producto es null
	 * Lógica: valida el producto y obtiene el valor del mapa, usando cero como valor
	 * predeterminado.
	 */
	public int consultarStock(Producto producto) {
		// Precondición: el producto no puede ser null; puede no estar registrado.
		validarProducto(producto);
		// Postcondición: se devuelve el stock registrado, o cero si no está registrado.
		return stock.getOrDefault(producto, 0);
	}
	
	/**
	 * Configura el máximo permitido para el stock de un producto
	 * @param producto producto cuyo máximo se configurará
	 * @param maximo cantidad máxima permitida, debe ser no negativa y no menor que el stock actual
	 * @throws IllegalArgumentException si el producto es nulo, el máximo es negativo
	 * o es inferior al stock actual
	 * Lógica: valida los argumentos y guarda el máximo; comprueba mediante aserción
	 * que el estado del inventario siga siendo válido
	 */
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
	
	//Consulta el máximo configurado para un producto.
	public int consultarStockMaximo(Producto producto) {
		// Precondición: el producto no puede ser null; puede no estar registrado.
		validarProducto(producto);
		// Postcondición: se devuelve el máximo registrado, o cero si no se configuró.
        return stockMaximo.getOrDefault(producto, 0);
    }

	
	/**
	 * Comprueba si hay al menos la cantidad indicada de un producto
	 * @param producto producto cuyo stock se consulta
	 * @param cantidad mínimo requerido, debe ser no negativo
	 * @return true si el stock alcanza la cantidad, false en caso contrario
	 * @throws IllegalArgumentException si el producto es nulo o la cantidad es negativa
	 * Lógica: valida los argumentos y compara el stock consultado con la cantidad
	 */
	public boolean hayStockSuficiente(Producto producto, int cantidad) {
		// Precondiciones: producto no nulo y cantidad no negativa.
		validarProducto(producto);
		if (cantidad < 0) {
			throw new IllegalArgumentException("La cantidad no podue ser negativa");
		    }
		// Postcondición: true exactamente cuando el stock registrado alcanza la cantidad.
	    return consultarStock(producto) >= cantidad;
	}

	/**
	 * Obtiene los productos cuyo stock alcanza la cantidad mínima indicada
	 * @param cantidad mínimo de unidades requeridas, debe ser positivo
	 * @return lista de productos que cumplen el mínimo
	 * @throws IllegalArgumentException si la cantidad es menor o igual que cero
	 * Lógica: filtra las entradas del mapa de stock por cantidad disponible y
	 * devuelve sus productos.
	 */
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

	/**
	 * Valida que se haya indicado un producto.
	 * @param producto producto que se comprobará
	 * @throws IllegalArgumentException si el producto es null
	 * Lógica: lanza una excepción cuando el argumento no existe.
	 */
	private void validarProducto(Producto producto) {
		if (producto == null) {
			throw new IllegalArgumentException("El producto no puede ser null");
		}
	}

	/**
	 * Comprueba la consistencia de los mapas almacenados en el inventario
	 * @return true si los mapas cumplen las reglas de stock y máximos,  delega la comprobación en
	 * estadoInternoValido(Map, Map) con los mapas de esta instancia
	 */
	private boolean invariant() {
		return estadoInternoValido(stock, stockMaximo);
	}

	/**
	 * Valida la consistencia de dos mapas de stock sin modificar ninguno
	 * @param stock mapa de cantidades disponibles
	 * @param stockMaximo mapa de máximos configurados
	 * @return true si ambos mapas existen, sus claves y valores son válidos,
	 * y ninguna cantidad supera su máximo, flase en caso contrario
	 * Lógica: rechaza mapas nulos, productos o cantidades nulos, valores negativos
	 * y stock superior al máximo, permite máximos definidos para productos sin stock
	 */
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
