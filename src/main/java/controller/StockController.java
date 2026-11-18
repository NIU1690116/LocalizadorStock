package controller;

import model.domain.Peticion;
import model.domain.RestriccionesBusqueda;
import model.domain.ResultadoBusqueda;
import model.domain.Tienda;
import model.service.GestionStock;

public class StockController {
	private final GestionStock gestionStock;

	public StockController(GestionStock gestionStock) {
		this.gestionStock = gestionStock;
	}

	public ResultadoBusqueda consultar(
			Peticion peticion,
			Tienda tienda,
			RestriccionesBusqueda restricciones) {
		return gestionStock.buscarAlternativas(peticion, tienda, restricciones);
	}
}
