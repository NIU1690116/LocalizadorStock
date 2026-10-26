package model;

import java.util.List;

public class BuscadorTiendas {
	private final RepositorioTiendas repositorio;
    private final CalculadorDistancia calculadorDistancia;
    
	public BuscadorTiendas(RepositorioTiendas repositorio,
			CalculadorDistancia calculadorDistancia) {
		 // validar dependencias requeridas
		if(repositorio == null) {
			throw new IllegalArgumentException("El repositorio no puede ser null");
		}
		if(calculadorDistancia == null) {
			throw new IllegalArgumentException("El calculador no puede ser null");
		}
		this.repositorio = repositorio;
        this.calculadorDistancia = calculadorDistancia;
		
	}
	
	public List<Tienda> buscarTiendasConStock(
            Tienda origen,
            LineaPeticion linea,
            RestriccionesBusqueda restricciones) {
		return null;
        // filtrar por distancia y stock; ordenar por distancia
    }
	
	
	

}
