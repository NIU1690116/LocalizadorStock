package model.repository;

import model.domain.Producto;
import model.domain.Reposicion;
import model.domain.Tienda;

public interface ProveedorReposicion {
	Reposicion consultar(Tienda tienda, Producto producto);
}
