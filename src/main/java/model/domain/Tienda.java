package model.domain;

public class Tienda {
	private final String id;
	private final String nombre;
	private final double latitud;
	private final double longitud;
	private final Inventario inventario;
	
	public Tienda(String id, String nombre, double latitud, double longitud, Inventario inventario) {
		// Precondiciones: identidad y nombre no vacíos, coordenadas finitas y en rango
		// e inventario obligatorio.
		if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El id es obligatorio");
        }
		if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
		if (!Double.isFinite(latitud) || latitud < -90 || latitud > 90) {
            throw new IllegalArgumentException("Latitud no válida");
        }
		if (!Double.isFinite(longitud) || longitud < -180 || longitud > 180) {
            throw new IllegalArgumentException("Longitud no válida");
        }
		
		if (inventario == null) {
            throw new IllegalArgumentException(
                "El inventario no puede ser null"
            );
        }
		
		this.id = id;
		this.nombre = nombre;
		this.latitud = latitud;
		this.longitud = longitud;
		this.inventario = inventario;
		// Postcondición: la tienda se crea con todos sus datos válidos
		assert invariant() : "La tienda debe cumplir sus invariantes después de construirse";
	}

	private boolean invariant() {
		return id != null && !id.trim().isEmpty()
				&& nombre != null && !nombre.trim().isEmpty()
				&& Double.isFinite(latitud) && latitud >= -90 && latitud <= 90
				&& Double.isFinite(longitud) && longitud >= -180 && longitud <= 180
				&& inventario != null;
	}
	
    // getters and setters
	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public double getLatitud() {
		return latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public Inventario getInventario() {
		return inventario;
	}
	
	public int consultarStock(Producto producto) {
		// Postcondición: devuelve el stock que ese inventario registra para el producto
        return inventario.consultarStock(producto);
    }	

}
