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


}
