package model.service;

import model.domain.Producto;
import model.domain.Reposicion;
import model.domain.Tienda;
import model.repository.ProveedorReposicion;

public class ServicioReposicion {
	private final ProveedorReposicion proveedor;

	public ServicioReposicion(ProveedorReposicion proveedor) {
		if (proveedor == null) {
			throw new IllegalArgumentException("El proveedor de reposiciones es obligatorio");
		}
		this.proveedor = proveedor;
	}

}
