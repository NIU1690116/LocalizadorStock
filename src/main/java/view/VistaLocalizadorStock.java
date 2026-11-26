package view;

import java.io.PrintStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import model.domain.Alternativa;
import model.domain.AlternativaOtraTienda;
import model.domain.AlternativaProductoSimilar;
import model.domain.AlternativaReposicion;
import model.domain.EstadoResolucion;
import model.domain.ResultadoBusqueda;
import model.domain.ResultadoLineaPeticion;

/**
 * Presenta por consola el resultado de una búsqueda de stock
 * Recorre cada línea de la petición, indica su estado de resolución y, cuando
 * existe una alternativa, la describe según su tipo con tienda, tiempo,
 * distancia
 */
public class VistaLocalizadorStock {
	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private final PrintStream salida;

	/**
	 * Crea la vista usando la salida estándar del proceso
	 * Devuelve: una instancia de VistaLocalizadorStock
	 * Lógica: delega la inicialización en VistaLocalizadorStock(PrintStream)
	 */
	public VistaLocalizadorStock() {
		this(System.out);
	}

	/**
	 * Crea la vista asociada al flujo de salida indicado
	 * @param salida flujo donde se escribirán los resultados
	 * Devuelve: una instancia de VistaLocalizadorStock
	 * Lógica: guarda el flujo para utilizarlo al imprimir la información
	 */
	public VistaLocalizadorStock(PrintStream salida) {
		this.salida = salida;
	}

	/**
	 * Muestra el encabezado y cada línea incluida en un resultado de búsqueda.
	 * @param resultado resultado con las líneas y estados que se presentarán
	 * Lógica: recorre los resultados y delega la impresión de cada uno en
	 * mostrarLinea(ResultadoLineaPeticion)
	 */
	public void mostrarResultado(ResultadoBusqueda resultado) {
		salida.println("Resultado de búsqueda:");
		for (ResultadoLineaPeticion resultadoLinea : resultado.getResultados()) {
			mostrarLinea(resultadoLinea);
		}
	}
	/**
	 * Imprime el producto, la cantidad solicitada y el estado de una línea.
	 * @param resultadoLinea línea de petición junto con su estado y posible alternativa
	 * Lógica: si está resuelta, informa que se encuentra en la tienda actual; si no
	 * tiene solución, lo indica en otro caso, describe su mejor alternativa
	 */
	private void mostrarLinea(ResultadoLineaPeticion resultadoLinea) {
		String producto = resultadoLinea.getLinea().getProducto().getNombre();
		int cantidad = resultadoLinea.getLinea().getCantidad();
		salida.print(producto + " x " + cantidad + ": ");

		EstadoResolucion estado = resultadoLinea.getEstado();
		if (estado == EstadoResolucion.RESUELTA) {
			salida.println("resuelta en la tienda actual.");
		} else if (estado == EstadoResolucion.SIN_SOLUCION) {
			salida.println("no se encontró una solución.");
		} else {
			salida.println(describir(resultadoLinea.getMejorAlternativa()));
		}
	}

	/**
	 * Genera el texto que resume una alternativa de stock
	 * @param alternativa alternativa que se quiere presentar
	 * @return descripción con los datos disponibles según el tipo de alternativa
	 * Lógica: identifica si es de otra tienda, de reposición o de producto similar,
	 * incluye los datos propios de ese tipo y utiliza una descripción genérica si
	 * no coincide con ninguno de ellos
	 */
	private String describir(Alternativa alternativa) {
		if (alternativa instanceof AlternativaOtraTienda otraTienda) {
			return String.format(Locale.ROOT, "recoger en %s (%d min, %.1f km).",
					otraTienda.getTienda().getNombre(),
					otraTienda.getTiempoEstimadoMinutos(),
					otraTienda.getDistanciaKm());
		}
		if (alternativa instanceof AlternativaReposicion reposicion) {
			return String.format(Locale.ROOT, "esperar reposición en %s (%d min, %d unidades, %s).",
					reposicion.getTienda().getNombre(),
					reposicion.getTiempoEstimadoMinutos(),
					reposicion.getReposicion().getCantidad(),
					reposicion.getReposicion().getFechaPrevista().format(FORMATO_FECHA));
		}
		if (alternativa instanceof AlternativaProductoSimilar similar) {
			return String.format(Locale.ROOT, "producto similar: %s en %s (%.1f%%, %d min, %.1f km).",
					similar.productoSimilar().getNombre(),
					similar.tienda().getNombre(),
					similar.gradoSimilitud(),
					similar.getTiempoEstimadoMinutos(),
					similar.getDistanciaKm());
		}
		return String.format(Locale.ROOT, "alternativa propuesta (%d min, %.1f km).",
				alternativa.getTiempoEstimadoMinutos(), alternativa.getDistanciaKm());
	}


}
