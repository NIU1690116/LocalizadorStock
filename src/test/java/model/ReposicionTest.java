package test.java.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import main.java.model.Producto;
import main.java.model.Reposicion;

class ReposicionTest {
    private Producto producto = new Producto("P1", "Camisa", "Rojo", "Camisa", "Unisex");
    private LocalDateTime prevista = LocalDateTime.of(2026, 10, 22, 11, 0);

    @Test
    void testCreaReposicionValida() {
       
    }

}
