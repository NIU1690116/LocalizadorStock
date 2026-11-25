package model.domain;

/**
 * Estados posibles para la resolución de una línea de petición.
 * RESUELTA: indica que se cubre en la tienda actual
 * ALTERNATIVA_PROPUESTA: que se encontró otra opción
 * SIN_SOLUCION: que no se encontró una forma de resolverla
 */
public enum EstadoResolucion {
	RESUELTA,
	ALTERNATIVA_PROPUESTA,
	SIN_SOLUCION
}
