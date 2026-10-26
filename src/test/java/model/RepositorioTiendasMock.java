<<<<<<< HEAD
package test.java.model;

import java.util.List;

import main.java.model.RepositorioTiendas;
import main.java.model.Tienda;

public class RepositorioTiendasMock implements RepositorioTiendas {
	List<Tienda> obtenerTiendas();
=======
package model;

import java.util.List;
import java.util.ArrayList;

public class RepositorioTiendasMock implements RepositorioTiendas {
    private final List<Tienda> tiendas = new ArrayList<>();

    @Override
    public List<Tienda> obtenerTiendas() {
        return new ArrayList<>(tiendas);
    }

    public void agregarTienda(Tienda tienda) {
        if (tienda == null) {
            throw new IllegalArgumentException("La tienda no puede ser null");
        }
        tiendas.add(tienda);
    }
>>>>>>> 1ffe5f9695c990272a0450b5a61117523034bce9
}
