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

public class VistaLocalizadorStock {
	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private final PrintStream salida;

	public VistaLocalizadorStock() {
		this(System.out);
	}

	public VistaLocalizadorStock(PrintStream salida) {
		this.salida = salida;
	}

	public void mostrarResultado(ResultadoBusqueda resultado) {
		salida.println("Resultado de búsqueda:");
		for (ResultadoLineaPeticion resultadoLinea : resultado.getResultados()) {
			mostrarLinea(resultadoLinea);
		}
	}
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
