package model;

import java.util.ArrayList;
import java.util.List;

public class BuscadorTiendas {
    private final RepositorioTiendas repositorio;
    private final CalculadorDistancia calculadorDistancia;

    public BuscadorTiendas(RepositorioTiendas repositorio, CalculadorDistancia calculadorDistancia) {
    	if (repositorio == null || calculadorDistancia == null) {
            throw new IllegalStateException("Las dependencias del buscador no son válidas");
        }
        this.repositorio = repositorio;
        this.calculadorDistancia = calculadorDistancia;
    }

    public List<TiendaConDistancia> buscarTiendasConStock(
            Tienda origen,
            LineaPeticion linea,
            RestriccionesBusqueda restricciones) {
        
        //precondiciones
        if (origen == null) {
            throw new IllegalArgumentException("La tienda origen no puede ser null");
        }
        if (linea == null) {
            throw new IllegalArgumentException("LineaPeticion no puede ser null");
        }
        if (restricciones == null) {
            throw new IllegalArgumentException("restricciones no puede ser null");
        }

        List<Tienda> tiendas = repositorio.obtenerTiendas();
        List<TiendaConDistancia> candidatas = new ArrayList<>();

        for (Tienda tienda : tiendas) {
            if (tienda == null) {
                continue;
            }
            double distanciaKm = calculadorDistancia.calcularKm(origen, tienda);

            boolean distanciaPermitida = restricciones.permiteDistancia(distanciaKm);
            boolean stockSuficiente = tienda.getInventario().hayStockSuficiente(
                    linea.getProducto(),
                    linea.getCantidad());

            if (distanciaPermitida && stockSuficiente) {
                candidatas.add(new TiendaConDistancia(tienda, distanciaKm));
            }
        } 
        // Devolvemos la lista completa de tiendas con sus respectivas distancias
        return candidatas;
    }

    public record TiendaConDistancia(Tienda tienda, double distancia) {}
}