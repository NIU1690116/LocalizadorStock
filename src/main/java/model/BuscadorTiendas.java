package model;

import java.util.List;

public class BuscadorTiendas {
	private final RepositorioTiendas repositorio;
    private final CalculadorDistancia calculadorDistancia;
    
    private void Invariante(){
    	if (repositorio == null || calculadorDistancia == null) {
            throw new IllegalStateException("Las dependencias del buscador no son válidas");
        }
	}
    
	public BuscadorTiendas(RepositorioTiendas repositorio,
			CalculadorDistancia calculadorDistancia) {
		this.repositorio = repositorio;
        this.calculadorDistancia = calculadorDistancia;
        
        //Postcondición
        Invariante();
	}
	
	public List<Tienda> buscarTiendasConStock(
            Tienda origen,
            LineaPeticion linea,
            RestriccionesBusqueda restricciones) {
		//Precondiciones: validar dependencias
		if(origen == null) {
			throw new IllegalArgumentException("La tienda origen no puede ser null");
		}
		if(linea == null) {
			throw new IllegalArgumentException("LineaPeticion no puede ser null");
		}
		if(restricciones == null) {
			throw new IllegalArgumentException("restricciones no puede ser null");
		}
		//Postcondición
		Invariante();
		return null;
    }

	
	
	

}
