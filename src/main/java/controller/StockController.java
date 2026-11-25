package controller;

import model.domain.Peticion;
import model.domain.RestriccionesBusqueda;
import model.domain.ResultadoBusqueda;
import model.domain.Tienda;
import model.service.GestionStock;

/**
 * Recibe las consultas de la aplicación y las delega al servicio de gestión
 * de stock. Actúa como medio entre la vista y la lógica de búsqueda
 */
public class StockController {
	private final GestionStock gestionStock;

	// Crea el controlador asociado al servicio de gestión de stock
	public StockController(GestionStock gestionStock) {
		this.gestionStock = gestionStock;
	}

	/**
	 * Busca alternativas para una petición desde una tienda y con restricciones
	 * @param peticion productos y cantidades solicitados
	 * @param tienda tienda desde la que se inicia la búsqueda
	 * @param restricciones condiciones que limitan las alternativas validas
	 * @return resultado de búsqueda producido por GestionStock
	 * Lógica: pasa los tres argumentos al servicio y devuelve el
	 * resultado que este genera
	 */
	public ResultadoBusqueda consultar(
			Peticion peticion,
			Tienda tienda,
			RestriccionesBusqueda restricciones) {
		return gestionStock.buscarAlternativas(peticion, tienda, restricciones);
	}
}
