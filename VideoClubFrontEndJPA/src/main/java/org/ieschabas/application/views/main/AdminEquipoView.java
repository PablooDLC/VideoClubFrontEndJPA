package org.ieschabas.application.views.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.ieschabas.clases.Actor;
import org.ieschabas.clases.Director;
import org.ieschabas.clases.Equipo;
import org.ieschabas.daos.EquipoDao;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista de administración para el listado y formulario del equipo.
 * Esta vista muestra un listado y formulario de actores y directores.
 * Solo los usuarios con el rol "ROLE_ADMIN" tienen acceso a esta vista.
 */
@Route(value = "listado-equipo", layout = AdminView.class)
@RolesAllowed("ROLE_ADMIN")
public class AdminEquipoView extends VerticalLayout implements HasUrlParameter<String> {

    List<Equipo> coleccionEquipo = new ArrayList<Equipo>();
    FormLayout formLayout = new FormLayout();
    Grid<Equipo> grid = new Grid<>(Equipo.class, false);
    Equipo equipoSeleccionado;
    String rol;

    /**
     * Constructor de la vista de administración de equipos.
     * Inicializa el Grid y el formulario, y los agrega al diseño.
     */
    public AdminEquipoView() {

        coleccionEquipo = EquipoDao.obtenerEquipo();

        iniciarGrid();
        iniciarFormulario();

        add(grid, formLayout);

    }

    /**
     * Inicializa el formulario para añadir equipos.
     */
    private void iniciarFormulario() {

        TextField nombreField = new TextField("Nombre");
        TextField apellidosField = new TextField("Apellidos");
        IntegerField añoField = new IntegerField("Año de nacimiento");
        TextField paisField = new TextField("Pais");

        formLayout.setResponsiveSteps(
                // Use one column by default
                new ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new ResponsiveStep("500px", 2));

        Button añadir = new Button("Añadir");
        añadir.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        añadir.addClickListener(e -> {

            Equipo equipo = new Equipo();
            if (nombreField != null && !nombreField.isEmpty() && rol != null && !rol.isEmpty()) {
                if (rol.equals("Actor")) {
                    equipo = new Actor(nombreField.getValue(), apellidosField.getValue(),
                            añoField.getValue(), paisField.getValue());
                } else if (rol.equals("Director")) {
                    equipo = new Director(nombreField.getValue(), apellidosField.getValue(),
                            añoField.getValue(), paisField.getValue());
                }
            }

            EquipoDao.guardarEquipo(equipo);

            Notification popup = Notification.show("Añadido correctamente");
            popup.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            nombreField.clear();
            apellidosField.clear();
            añoField.clear();
            paisField.clear();

            grid.getDataProvider().refreshAll();
            grid.setItems(coleccionEquipo);

            UI.getCurrent().getPage().reload();
        });

        formLayout.add(nombreField, apellidosField, añoField, paisField, añadir);

    }

    /**
     * Inicializa el grid para mostrar los equipos.
     */
    private void iniciarGrid() {
        grid.setItems(coleccionEquipo);
        grid.addColumn(Equipo::getId).setHeader("ID");
        grid.addColumn(Equipo::getNombre).setHeader("Nombre");
        grid.addColumn(Equipo::getApellidos).setHeader("Apellidos");
        grid.addColumn(Equipo::getAnyoNac).setHeader("Año de nacimiento");
        grid.addColumn(Equipo::getPais).setHeader("País");
        grid.addColumn(Equipo::getRol).setHeader("Rol");
        grid.addColumn(new ComponentRenderer<>(Button::new, (eliminarBoton, equipo) -> {

            eliminar(eliminarBoton, equipo);

        })).setHeader("Eliminar");

        grid.addColumn(new ComponentRenderer<>(Button::new, (editarBoton, equipo) -> {

            modificarForm(editarBoton, equipo);

        })).setHeader("Modificar");

    }

    /**
     * Modifica los datos del actor o director.
     *
     * @param editarBoton
     * @param equipo
     */
    private void modificarForm(Button editarBoton, Equipo equipo) {
        editarBoton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_TERTIARY);

        editarBoton.addClickListener(event -> {
            equipoSeleccionado = equipo;
            FormLayout formulario = new FormLayout();
            TextField nombreField = new TextField("Nombre");
            nombreField.setValue(equipo.getNombre());
            formulario.add(nombreField);
            TextField apellidosField = new TextField("Apellidos");
            apellidosField.setValue(equipo.getApellidos());
            formulario.add(apellidosField);
            IntegerField añoField = new IntegerField("Año de nacimiento");
            añoField.setValue(equipo.getAnyoNac());
            formulario.add(añoField);
            TextField paisField = new TextField("Pais");
            paisField.setValue(equipo.getPais());
            formulario.add(paisField);
            TextField rolField = new TextField("Rol");
            rolField.setValue(equipo.getRol());
            formulario.add(rolField);

            Dialog dialogo = new Dialog();
            dialogo.add(formulario);

            Button guardarBoton = new Button("Guardar");
            guardarBoton.addClickListener(event2 -> {

                equipoSeleccionado.setNombre(nombreField.getValue());
                equipoSeleccionado.setApellidos(apellidosField.getValue());
                equipoSeleccionado.setAnyoNac(añoField.getValue());
                equipoSeleccionado.setPais(paisField.getValue());
                equipoSeleccionado.setRol(rolField.getValue());
                equipoSeleccionado = equipo;

                EquipoDao.modificarEquipo(equipo);
                grid.getDataProvider().refreshAll();
                grid.setItems(coleccionEquipo);

                Notification popup = Notification.show("Modificado correctamente");
                popup.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                dialogo.close();
            });

            formulario.add(guardarBoton);

            editarBoton.setEnabled(true);

            dialogo.open();
        });

        editarBoton.setIcon(new Icon(VaadinIcon.EDIT));
    }

    /**
     * Elimina el actor o director seleccionado
     *
     * @param eliminarBoton
     * @param equipo
     */
    private void eliminar(Button eliminarBoton, Equipo equipo) {
        eliminarBoton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_TERTIARY);

        eliminarBoton.addClickListener(e -> {

            if (equipo != null) {
                EquipoDao.eliminarEquipo(equipo);
                UI.getCurrent().getPage().reload();

                Notification popup = Notification.show("Eliminado correctamente");
                popup.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        });

        eliminarBoton.setIcon(new Icon(VaadinIcon.TRASH));
    }

    /**
     * Establece un parámetro, comprueba su valor y actualiza la interfaz de usuario.
     *
     * @param event     the navigation event that caused the call to this method
     * @param parameter the resolved url parameter
     */
    @Override
    public void setParameter(BeforeEvent event, String parameter) {

        this.rol = parameter;
        this.removeAll();
        if (parameter.equalsIgnoreCase("Actor")) {
            coleccionEquipo = EquipoDao.obtenerActor(parameter);
        } else if (parameter.equalsIgnoreCase("Director")) {
            coleccionEquipo = EquipoDao.obtenerDirector(parameter);
        }

        grid.getDataProvider().refreshAll();
        grid.setItems(coleccionEquipo);
        add(grid, formLayout);

    }

}
