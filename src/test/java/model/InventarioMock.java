package test.java.model;

import main.java.model.Inventario;
import main.java.model.Producto;

public class InventarioMock extends Inventario {
	private final int stockARetornar;

    public InventarioMock(int stockARetornar) {
        this.stockARetornar = stockARetornar;
    }

    @Override
    public int consultarStock(Producto producto) {
        return this.stockARetornar;
    }

}
