package model.repository;

import model.domain.Producto;
import model.domain.Reposicion;
import model.domain.Tienda;

/**
 * consultar si se espera una reposición de un producto en una tienda
 * Desacopla los servicios que necesitan esta información de su origen concreto
 */
public interface ProveedorReposicion {
	Reposicion consultar(Tienda tienda, Producto producto);
}
