package model.service;

import model.domain.Producto;
import model.domain.Reposicion;
import model.domain.Tienda;
import model.repository.ProveedorReposicion;

/**
 * Consulta las reposiciones disponibles mediante un proveedor
 * Valida los datos básicos de la consulta y deja en manos del proveedor la
 * búsqueda (desaclopar servicio del origen)
 */
public class ServicioReposicion {
	private final ProveedorReposicion proveedor;

	public ServicioReposicion(ProveedorReposicion proveedor) {
		if (proveedor == null) {
			throw new IllegalArgumentException("El proveedor de reposiciones es obligatorio");
		}
		this.proveedor = proveedor;
	}

	/**
	 * Consulta si hay una reposición disponible para un producto en una tienda.
	 * @param tienda tienda para la que se busca la reposición
	 * @param producto producto cuya reposición se consulta
	 * @return reposición dada, o null si el proveedor no encuentra ninguna
	 * @throws IllegalArgumentException si la tienda o el producto son null
	 * Lógica: valida ambos argumentos y delega la consulta en el proveedor, devuelve
	 * directamente el resultado recibido
	 */
	public Reposicion consultarReposicion(Tienda tienda, Producto producto) {
		if (tienda == null) {
			throw new IllegalArgumentException("La tienda es obligatoria");
		}
		if (producto == null) {
			throw new IllegalArgumentException("El producto es obligatorio");
		}
		return proveedor.consultar(tienda, producto);
	}
}
