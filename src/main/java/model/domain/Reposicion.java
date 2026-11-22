package model.domain;

import java.time.LocalDateTime;

// Cantidad prevista de un producto y momento en que estará disponible
public class Reposicion {
    private final Producto producto;
    private final int cantidad;
    private final LocalDateTime fechaPrevista;

    public Reposicion(Producto producto, int cantidad, LocalDateTime fechaPrevista) {
		// Precondiciones (parte 1): el producto es obligatorio y la cantidad debe ser positiva.
    	if (producto == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de reposición debe ser positiva");
        }
		// Precondición (parte 2): la fecha prevista es obligatoria.
        if (fechaPrevista == null) {
            throw new IllegalArgumentException("La fecha prevista es obligatoria");
        }
        this.producto = producto;
        this.cantidad = cantidad;
        this.fechaPrevista = fechaPrevista;
		// Postcondición: se almacenan los argumentos y la reposición cumple su invariante.
		assert invariant() : "La reposición debe ser válida después de construirse";
    }

	private boolean invariant() {
		return producto != null && cantidad > 0 && fechaPrevista != null;
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
