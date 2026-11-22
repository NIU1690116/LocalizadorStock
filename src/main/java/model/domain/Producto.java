package model.domain;

import java.util.Objects;

// producto del catálogo
public class Producto {
	private final String codigo;
	private String nombre;
	private String color;
	private String familia;
	private String genero;
	
	//atributo codigo no puede ser nulo ni vacio
	public Producto(String codigo, String nombre, String color, String familia, String genero) {
		// Precondición: el código identificador es obligatorio y no puede estar en blanco.
		if (codigo == null || codigo.isBlank()) {
			throw new IllegalArgumentException("El código del producto es obligatorio");
		}
		this.codigo = codigo;
		this.nombre = nombre;
		this.color = color;
		this.familia = familia;
		this.genero = genero;
		// Postcondición: los atributos se inicializan con los argumentos recibidos
		// y el producto conserva un código válido.
		assert invariant() : "El producto debe tener un código válido después de construirse";
	}

	private boolean invariant() {
		return codigo != null && !codigo.isBlank();
	}


	public String getCodigo() {
		return codigo;
	}

	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	public String getFamilia() {
		return familia;
	}


	public void setFamilia(String familia) {
		this.familia = familia;
	}


	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}
	
	// El código identifica el producto y no debe cambiar mientras se use como clave.
	@Override
	public boolean equals(Object objeto) {
		if (this == objeto) {
			return true;
		}
		if (!(objeto instanceof Producto otro)) {
			return false;
		}
		return codigo.equals(otro.codigo);
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(codigo);
	}
	

}
