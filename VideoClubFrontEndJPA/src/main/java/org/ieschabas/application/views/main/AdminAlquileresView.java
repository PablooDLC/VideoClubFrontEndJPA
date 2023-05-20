package org.ieschabas.application.views.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.select.Select;
import org.ieschabas.clases.*;
import org.ieschabas.daos.AlquilerDao;
import org.ieschabas.daos.EquipoDao;
import org.ieschabas.daos.PeliculaDao;
import org.ieschabas.daos.UsuariosDao;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;

@Route(value = "listado-alquileres", layout = AdminView.class)
@RolesAllowed("ROLE_ADMIN")
public class AdminAlquileresView extends VerticalLayout {

    List<Alquiler> coleccionAlquiler = new ArrayList<Alquiler>();
    Alquiler alquilerSeleccionado;
    Grid<Alquiler> grid = new Grid<>(Alquiler.class, false);
    FormLayout formLayout = new FormLayout();

    Alquiler alquiler;
    AlquilerId alquilerId;

    public AdminAlquileresView() {

        coleccionAlquiler = AlquilerDao.obtenerAlquiler();

        iniciarGrid();

        add(grid, formLayout);

    }

    private void iniciarGrid() {

        grid.setItems(coleccionAlquiler);
        grid.addColumn(alquiler -> alquiler.getIdCompuesta().getIdCliente().getEmail()).setHeader("Cliente");
        grid.addColumn(alquiler -> alquiler.getIdCompuesta().getIdPelicula().getTitulo()).setHeader("Pelicula");
        grid.addColumn(alquiler -> alquiler.getIdCompuesta().getFechaAlquiler()).setHeader("Fecha de alquiler");
        grid.addColumn(Alquiler::getFechaRetorno).setHeader("Fecha de devolución");

        grid.addColumn(new ComponentRenderer<>(Button::new, (eliminarBoton, alquiler) -> {

            eliminar(eliminarBoton, alquiler);

        })).setHeader("Eliminar");

        grid.addColumn(new ComponentRenderer<>(Button::new, (editarBoton, alquiler) -> {

            modificarForm(editarBoton, alquiler);

        })).setHeader("Modificar");

    }

    private void modificarForm(Button editarBoton, Alquiler alquiler) {

        editarBoton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_TERTIARY);

        editarBoton.addClickListener(event -> {
            alquilerSeleccionado = alquiler;
            FormLayout formulario = new FormLayout();
            DatePicker fechaRetornoField = new DatePicker();
            fechaRetornoField.setValue(alquiler.getFechaRetorno());
            formulario.add(fechaRetornoField);

            Dialog dialogo = new Dialog();
            dialogo.add(formulario);

            Button guardarBoton = new Button("Guardar");
            guardarBoton.addClickListener(event2 -> {

                alquilerSeleccionado.setFechaRetorno(fechaRetornoField.getValue());
                alquilerSeleccionado = alquiler;

                AlquilerDao.modificarPelicula(alquiler);
                grid.getDataProvider().refreshAll();
                grid.setItems(coleccionAlquiler);

                Notification popup = Notification.show("Alquiler modificado correctamente");
                popup.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                dialogo.close();
            });

            formulario.add(guardarBoton);

            editarBoton.setEnabled(true);

            dialogo.open();
        });

        editarBoton.setIcon(new Icon(VaadinIcon.EDIT));
    }

    private void eliminar(Button eliminarBoton, Alquiler alquiler) {

        eliminarBoton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_TERTIARY);

        eliminarBoton.addClickListener(e -> {

            if (alquiler != null) {
                AlquilerDao.eliminarAlquiler(alquiler);
                UI.getCurrent().getPage().reload();

                Notification popup = Notification.show("Alquiler eliminado correctamente");
                popup.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        });

        eliminarBoton.setIcon(new Icon(VaadinIcon.TRASH));

    }
}
