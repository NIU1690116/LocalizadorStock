package main.java.model;

import java.time.LocalDateTime;

// Cantidad prevista de un producto y momento en que estará disponible
public class Reposicion {
    private final Producto producto;
    private final int cantidad;
    private final LocalDateTime fechaPrevista;

    public Reposicion(Producto producto, int cantidad, LocalDateTime fechaPrevista) {
    	if (producto == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de reposición debe ser positiva");
        }
        if (fechaPrevista == null) {
            throw new IllegalArgumentException("La fecha prevista es obligatoria");
        }
        this.producto = producto;
        this.cantidad = cantidad;
        this.fechaPrevista = fechaPrevista;
    }

	public Producto getProducto() {
		return producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public LocalDateTime getFechaPrevista() {
		return fechaPrevista;
	}

    
}
