package test.java.model;

import java.util.HashMap;
import java.util.Map;

import main.java.model.Inventario;
import main.java.model.Producto;

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

}
