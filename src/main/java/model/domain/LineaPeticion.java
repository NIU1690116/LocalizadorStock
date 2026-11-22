package model.domain;

public class LineaPeticion {
	private final Producto producto;
    private final int cantidad;
    
    public LineaPeticion(Producto producto, int cantidad) {
		// Precondiciones: el producto es obligatorio y la cantidad debe ser positiva
    	if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    	 if (producto == null) {
    		 throw new IllegalArgumentException("El producto no puede ser null");
         }
		this.producto = producto;
		this.cantidad = cantidad;
		// Postcondición: se almacenan los argumentos y la línea cumple su invariante
		assert invariant() : "La línea de petición debe ser válida después de construirse";
    }

	private boolean invariant() {
		return producto != null && cantidad > 0;
    }

	public Producto getProducto() {
		// Postcondición: devuelve el producto asociado a esta línea
		assert invariant();
		return producto;
	}

	public int getCantidad() {
		// Postcondición: devuelve la cantidad positiva asociada a esta línea
		assert invariant();
		return cantidad;
	}
    


}
