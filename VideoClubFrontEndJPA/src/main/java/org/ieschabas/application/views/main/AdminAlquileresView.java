package org.ieschabas.application.views.main;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.ieschabas.clases.Alquiler;
import org.ieschabas.clases.AlquilerId;
import org.ieschabas.daos.AlquilerDao;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;


/**
 * Vista de administración para el listado de alquileres.
 * Esta vista muestra un listado de alquileres.
 * Solo los usuarios con el rol "ROLE_ADMIN" tienen acceso a esta vista.
 */
@Route(value = "listado-alquileres", layout = AdminView.class)
@RolesAllowed("ROLE_ADMIN")
public class AdminAlquileresView extends VerticalLayout {

    List<Alquiler> coleccionAlquiler = new ArrayList<Alquiler>();
    Alquiler alquilerSeleccionado;
    Grid<Alquiler> grid = new Grid<>(Alquiler.class, false);
    FormLayout formLayout = new FormLayout();

    Alquiler alquiler;
    AlquilerId alquilerId;

    /**
     * Constructor de la vista AdminAlquileresView.
     * Se llama a la lista de alquileres, se crea el grid
     * y se añaden los componentes a la vista.
     */
    public AdminAlquileresView() {

        coleccionAlquiler = AlquilerDao.obtenerAlquiler();

        iniciarGrid();

        add(grid, formLayout);

    }

    /**
     * Configura un grid para mostrar los alquileres.
     * Se crean las columnas del grid y se asigna la lista de alquileres.
     */
    private void iniciarGrid() {

        grid.setItems(coleccionAlquiler);
        grid.addColumn(alquiler -> alquiler.getIdCompuesta().getIdCliente().getEmail()).setHeader("Cliente");
        grid.addColumn(alquiler -> alquiler.getIdCompuesta().getIdPelicula().getTitulo()).setHeader("Pelicula");
        grid.addColumn(alquiler -> alquiler.getIdCompuesta().getFechaAlquiler()).setHeader("Fecha de alquiler");
        grid.addColumn(Alquiler::getFechaRetorno).setHeader("Fecha de devolución");

    }
}
