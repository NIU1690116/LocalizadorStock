package model.service;

import model.distance.CalculadorDistancia;
import model.distance.EstimadorTiempoViaje;
import model.domain.LineaPeticion;
import model.domain.RestriccionesBusqueda;
import model.domain.Tienda;
import model.repository.RepositorioTiendas;
import java.util.ArrayList;
import java.util.List;

public class BuscadorTiendas {
    private final RepositorioTiendas repositorio;
    private final CalculadorDistancia calculadorDistancia;
    private final EstimadorTiempoViaje estimadorTiempoViaje;

    public BuscadorTiendas(
            RepositorioTiendas repositorio,
            CalculadorDistancia calculadorDistancia,
            EstimadorTiempoViaje estimadorTiempoViaje) {
		// Precondiciones: las tres dependencias son obligatorias.
    	if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio no puede ser nulo");
        }
    	if (calculadorDistancia == null) {
            throw new IllegalArgumentException("El calculador no puede ser nulo");
        }
        if (estimadorTiempoViaje == null) {
            throw new IllegalArgumentException("El estimador de tiempo no puede ser nulo");
        }

        this.repositorio = repositorio;
        this.calculadorDistancia = calculadorDistancia;
        this.estimadorTiempoViaje = estimadorTiempoViaje;
		// Postcondición: el buscador queda con sus dependencias inicializadas.
		assert invariant() : "El buscador debe tener todas sus dependencias";
    }

	private boolean invariant() {
		return repositorio != null
				&& calculadorDistancia != null
				&& estimadorTiempoViaje != null;
    }

    public List<TiendaConDistancia> buscarTiendasConStock(
            Tienda origen,
            LineaPeticion linea,
            RestriccionesBusqueda restricciones) {
		// Precondiciones: origen, línea y restricciones son obligatorios
		assert invariant() : "El buscador debe conservar sus dependencias";
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
            // La tienda actual se evalúa antes de buscar alternativas
            if (origen.getId().equals(tienda.getId())) {
                continue;
            }
            double distanciaKm = calculadorDistancia.calcularKm(origen, tienda);

            boolean distanciaPermitida = restricciones.permiteDistancia(distanciaKm);
            boolean stockSuficiente = tienda.getInventario().hayStockSuficiente(
                    linea.getProducto(),
                    linea.getCantidad());

            if (distanciaPermitida && stockSuficiente) {
            	int tiempoEstimado = estimadorTiempoViaje.estimarMinutos(origen, tienda);
                candidatas.add(new TiendaConDistancia(tienda, distanciaKm, tiempoEstimado));
            }
        } 
        // Devolvemos las candidatas ordenadas de menor a mayor distancia.
        candidatas.sort((a, b) -> Double.compare(a.distancia(), b.distancia()));
		// Postcondición: todas las candidatas cumplen origen, stock y distancia, y están ordenadas.
		assert cumplePostcondicion(candidatas, origen, linea, restricciones)
				: "El resultado debe contener únicamente candidatas válidas y ordenadas";
        return candidatas;
    }

	private boolean cumplePostcondicion(
			List<TiendaConDistancia> resultado,
			Tienda origen,
			LineaPeticion linea,
			RestriccionesBusqueda restricciones) {
		if (resultado == null) {
			return false;
		}

		double distanciaAnterior = -1.0;
		for (TiendaConDistancia candidata : resultado) {
			if (candidata == null || candidata.tienda() == null) {
				return false;
			}
			if (origen.getId().equals(candidata.tienda().getId())
					|| !restricciones.permiteDistancia(candidata.distancia())
					|| !candidata.tienda().getInventario().hayStockSuficiente(
							linea.getProducto(), linea.getCantidad())
					|| candidata.distancia() < distanciaAnterior) {
				return false;
			}
			distanciaAnterior = candidata.distancia();
		}
		return true;
	}

    public record TiendaConDistancia(Tienda tienda, double distancia, int tiempoEstimadoMinutos) {}
}
