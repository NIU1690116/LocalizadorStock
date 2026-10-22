package main.java.model;

public class Tienda {
	private final String id;
	private final String nombre;
	private final double latitud;
	private final double longitud;
	private final Inventario inventario;
	
	public Tienda(String id, String nombre, double latitud, double longitud, Inventario inventario) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.latitud = latitud;
		this.longitud = longitud;
		this.inventario = inventario;
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
	
	

}
