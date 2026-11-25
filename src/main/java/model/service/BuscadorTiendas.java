package model.service;

import model.distance.CalculadorDistancia;
import model.distance.EstimadorTiempoViaje;
import model.domain.LineaPeticion;
import model.domain.RestriccionesBusqueda;
import model.domain.Tienda;
import model.repository.RepositorioTiendas;
import java.util.ArrayList;
import java.util.List;

/**
 * Busca tiendas distintas de la tienda de origen que tengan stock suficiente
 * para una línea de petición y estén dentro de la distancia permitida
 * Calcula distancia y tiempo estimado para cada candidata y devuelve la lista
 * ordenada de menor a mayor distancia
 */
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

	/**
	 * Comprueba que todas las dependencias del buscador estén inicializadas.
	 * @return true si el repositorio y ambos calculadores no son nulos
	 * Lógica: verifica que cada referencia obligatoria tenga un valor
	 */
	private boolean invariant() {
		return repositorio != null
				&& calculadorDistancia != null
				&& estimadorTiempoViaje != null;
    }

	/**
	 * Busca tiendas que puedan cubrir la línea solicitada dentro de la distancia máxima
	 * @param origen tienda desde la que comienza la búsqueda
	 * @param linea producto y cantidad que deben estar disponibles
	 * @param restricciones límites que determinan si una distancia es admisible
	 * @return candidatas con distancia y tiempo estimado, ordenadas por distancia
	 * @throws IllegalArgumentException si el origen, la línea o las restricciones son null
	 * Lógica: obtiene las tiendas del repositorio, no incluye entradas nulas y la tienda
	 * de origen y filtra por distancia permitida y stock suficiente. Calcula el
	 * tiempo estimado de cada candidata, ordena la lista por distancia y comprueba
	 * mediante una aserción que el resultado cumpla las condiciones esperadas
	 */
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

	/**
	 * Verifica que la lista de candidatas sea válida y esté ordenada por distancia
	 * @param resultado candidatas que se comprobarán
	 * @param origen tienda que debe quedar excluida del resultado
	 * @param linea línea cuyo stock debe estar disponible en cada candidata
	 * @param restricciones límites de distancia que debe cumplir cada candidata
	 * @return true si todas las entradas son válidas y están en orden no decreciente
	 * Lógica: recorre el resultado y comprueba que no haya candidatas nulas, que no
	 * coincidan con el origen, que cumplan distancia y stock, y que la distancia no
	 * disminuya respecto a la candidata anterior
	 */
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

    /**
     * Datos de una tienda candidata junto con sus medidas de desplazamiento.
     * @param tienda tienda que cumple los criterios de búsqueda
     * @param distancia distancia desde el origen, en kilómetros
     * @param tiempoEstimadoMinutos tiempo estimado desde el origen, en minutos
     * Accesores generados: {@code tienda()}, {@code distancia()} y
     * {@code tiempoEstimadoMinutos()} devuelven respectivamente los valores
     * almacenados en esos componentes.
     */
    public record TiendaConDistancia(Tienda tienda, double distancia, int tiempoEstimadoMinutos) {}
}
