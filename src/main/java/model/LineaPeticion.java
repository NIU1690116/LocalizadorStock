package model;

public class LineaPeticion {
	private final Producto producto;
    private final int cantidad;
    
    public LineaPeticion(Producto producto, int cantidad) {
    	if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    	 if (producto == null) {
    		 throw new IllegalArgumentException("El producto no puede ser null");
         }

		this.producto = producto;
		this.cantidad = cantidad;
    }

	public Producto getProducto() {
		return producto;
	}

	public int getCantidad() {
		return cantidad;
	}
    


}
