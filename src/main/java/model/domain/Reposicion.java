package model.domain;

import java.time.LocalDateTime;

/**
 * Representa una reposición prevista de un producto, con su cantidad y fecha
 * estimada de llegada, sus datos son no son modificables después de la creación
 */
public class Reposicion {
    private final Producto producto;
    private final int cantidad;
    private final LocalDateTime fechaPrevista;

    public Reposicion(Producto producto, int cantidad, LocalDateTime fechaPrevista) {
		// Precondiciones : el producto es obligatorio y la cantidad debe ser positiva
    	if (producto == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de reposición debe ser positiva");
        }
		// Precondición: la fecha prevista es obligatoria
        if (fechaPrevista == null) {
            throw new IllegalArgumentException("La fecha prevista es obligatoria");
        }
        this.producto = producto;
        this.cantidad = cantidad;
        this.fechaPrevista = fechaPrevista;
		// Postcondición: se almacenan los argumentos y la reposición cumple su invariante.
		assert invariant() : "La reposición debe ser válida después de construirse";
    }

	/**
	 * Comprueba que el producto, la cantidad y la fecha almacenados sean válidos
	 * @return true si el producto y la fecha existen y la cantidad es positiva
	 * Lógica: verifica las condiciones de validez sobre los campos de la instancia
	 */
	private boolean invariant() {
		return producto != null && cantidad > 0 && fechaPrevista != null;
    }

	//Obtiene el producto que se repondrá
	public Producto getProducto() {
		return producto;
	}

	//Obtiene la cantidad de unidades previstas
	public int getCantidad() {
		return cantidad;
	}

	//Obtiene la fecha y hora previstas para la reposición
	public LocalDateTime getFechaPrevista() {
		return fechaPrevista;
	} 
}
