package model.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mock de Inventario con un mapa de stock simplificado
 * Permite preparar cantidades en memoria para probar consumidores como
 * Tienda sin depender de las reglas adicionales del inventario real
 */
public class InventarioMock extends Inventario {
	private final Map<Producto, Integer> stockSimulado = new HashMap<>();

    /**
     * Añade unidades al stock simulado
     */
    @Override
    public void añadirProductos(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad inválida");
        }
        stockSimulado.put(producto, stockSimulado.getOrDefault(producto, 0) + cantidad);
    }

    /**
     * Consulta el stock simulado del producto
     */
    @Override
    public int consultarStock(Producto producto) {
        return stockSimulado.getOrDefault(producto, 0);
    }

    /**
     * Lista los productos cuyo stock simulado alcanza el mínimo indicado
     */
    @Override
    public List<Producto> productosConStockSuficiente(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad inválida");
        }
        return stockSimulado.entrySet().stream()
                .filter(entrada -> entrada.getValue() >= cantidad)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
