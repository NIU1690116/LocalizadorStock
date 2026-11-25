package model.domain;

/**
 * Representa una tienda con id, nombre, ubicación geográfica e inventario.
 * Valida sus datos al construirse y ofrece acceso a ellos y al stock de sus productos
 */
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

	/**
	 * Comprueba que la identidad, la ubicación y el inventario sean válidos
	 * @return true si todos los campos cumplen sus condiciones, false en caso contrario
	 * Lógica: verifica que los textos no sean nulos ni vacíos, que las coordenadas
	 * sean finitas y estén dentro de sus rangos geográficos y que exista inventario
	 */
	private boolean invariant() {
		return id != null && !id.trim().isEmpty()
				&& nombre != null && !nombre.trim().isEmpty()
				&& Double.isFinite(latitud) && latitud >= -90 && latitud <= 90
				&& Double.isFinite(longitud) && longitud >= -180 && longitud <= 180
				&& inventario != null;
	}
	
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

	/**
	 * Obtiene el inventario asociado a la tienda.
	 * @return inventario de la tienda
	 * Lógica: devuelve la referencia al inventario almacenado
	 */
	public Inventario getInventario() {
		return inventario;
	}
	
	/**
	 * Consulta cuántas unidades de un producto hay en esta tienda
	 * @param producto producto cuyo stock se consulta
	 * @return número de unidades que informa el inventario
	 * Lógica: delega la consulta en el inventario asociado y devuelve su resultado
	 */
	public int consultarStock(Producto producto) {
        return inventario.consultarStock(producto);
    }	

}
