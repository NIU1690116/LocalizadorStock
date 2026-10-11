package main.java.model;

import java.util.Objects;

public class Producto {
	private String codigo;
	private String nombre;
	private String color;
	private String familia;
	private String genero;
	
	
	public Producto(String codigo, String nombre, String color, String familia, String genero) {
		if (codigo == null || codigo.isBlank()) {
			throw new IllegalArgumentException("El código del producto es obligatorio");
		}
		this.codigo = codigo;
		this.nombre = nombre;
		this.color = color;
		this.familia = familia;
		this.genero = genero;
	}


	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
	
	public int hashCode() {
	    return Objects.hash(codigo);
	}
	

}
