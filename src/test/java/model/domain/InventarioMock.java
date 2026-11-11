package model.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InventarioMock extends Inventario {
	private final Map<Producto, Integer> stockSimulado = new HashMap<>();

    @Override
    public void añadirProductos(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad inválida");
        }
        stockSimulado.put(producto, stockSimulado.getOrDefault(producto, 0) + cantidad);
    }

    @Override
    public int consultarStock(Producto producto) {
        return stockSimulado.getOrDefault(producto, 0);
    }

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
