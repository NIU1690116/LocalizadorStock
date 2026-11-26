package model.repository;

import model.domain.Tienda;
import java.util.List;

/**
 * Contrato para obtener las tiendas disponibles en la app
 * Permite que los servicios consulten las tiendas sin depender de donde
 * se almacenan los datos.
 */
public interface RepositorioTiendas {
	List<Tienda> obtenerTiendas();
}
