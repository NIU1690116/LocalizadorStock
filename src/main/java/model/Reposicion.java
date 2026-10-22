package main.java.model;

import java.time.LocalDateTime;

// Cantidad prevista de un producto y momento en que estará disponible
public class Reposicion {
    private final Producto producto;
    private final int cantidad;
    private final LocalDateTime fechaPrevista;

    public Reposicion(Producto producto, int cantidad, LocalDateTime fechaPrevista) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.fechaPrevista = fechaPrevista;
    }

    
}
