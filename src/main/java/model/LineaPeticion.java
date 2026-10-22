package main.java.model;

public class LineaPeticion {
	private final Producto producto;
    private final int cantidad;
    
    public LineaPeticion(Producto producto, int cantidad) {
		this.producto = producto;
		this.cantidad = cantidad;
    }

}
