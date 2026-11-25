package model.domain;

import java.util.Objects;

/**
 * Representa un producto del catálogo identificado por un código
 * El código es inmutable y determina la igualdad entre productos, nombre, color,
 * familia y género pueden consultarse y modificarse después de crear el objeto producto
 */
public class Producto {
	private final String codigo;
	private String nombre;
	private String color;
	private String familia;
	private String genero;
	
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
		assert invariant() : "El producto debe tener un código válido después de construirse";
	}

	/**
	 * Comprueba que el código identificador siga siendo válido.
	 * @return true si el código no es nulo ni está en blanco
	 * Lógica: comprueba el campo inmutable codigo
	 */
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

	// Obtiene la familia o categoría del producto.
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
	
	/**
	 * Compara este producto con otro usando el código identificador
	 * @param objeto objeto que se quiere comparar
	 * @return true si es la misma instancia o es un producto con
	 * el mismo código, false en caso contrario
	 * Lógica: comprueba primero identidad y tipo, y luego compara los códigos
	 */
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
	
	/**
	 * Calcula el código hash coherente con equals(Object)
	 * @return hash calculado a partir del código
	 */
	@Override
	public int hashCode() {
	    return Objects.hash(codigo);
	}

}
