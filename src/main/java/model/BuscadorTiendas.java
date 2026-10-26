<<<<<<< HEAD
package main.java.model;
=======
package model;
>>>>>>> 1ffe5f9695c990272a0450b5a61117523034bce9

import java.util.List;

public class BuscadorTiendas {
	public BuscadorTiendas(RepositorioTiendas respositorio,
			CalculadorDistancia calculadorDistancia) {
		 // validar dependencias requeridas
	}
	
	public List<Tienda> buscarTiendasConStock(
            Tienda origen,
            LineaPeticion linea,
            RestriccionesBusqueda restricciones) {
		return null;
        // filtrar por distancia y stock; ordenar por distancia
    }
	
	
	

}
