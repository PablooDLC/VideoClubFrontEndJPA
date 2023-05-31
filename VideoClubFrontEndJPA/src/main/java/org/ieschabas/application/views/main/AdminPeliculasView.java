package org.ieschabas.application.views.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.apache.commons.io.IOUtils;
import org.ieschabas.clases.Equipo;
import org.ieschabas.clases.Pelicula;
import org.ieschabas.daos.EquipoDao;
import org.ieschabas.daos.PeliculaDao;
import org.ieschabas.enums.Categoria;
import org.ieschabas.enums.Formato;
import org.ieschabas.enums.Valoracion;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Vista de administración para el listado y formulario de las peliculas.
 * Esta vista muestra un listado y formulario de las peliculas.
 * Solo los usuarios con el rol "ROLE_ADMIN" tienen acceso a esta vista.
 */
@Route(value = "listado-peliculas", layout = AdminView.class)
@RolesAllowed("ROLE_ADMIN")
public class AdminPeliculasView extends VerticalLayout {

    List<Pelicula> coleccionPeliculas = new ArrayList<Pelicula>();
    Pelicula peliculaSeleccionada;
    String actor = "Actor";
    String director = "Director";
    Grid<Pelicula> grid = new Grid<>(Pelicula.class, false);
    FormLayout formLayout = new FormLayout();
    Upload imagenUpload;
    MultiFileMemoryBuffer multiFileMemoryBuffer;
    byte[] imagenBytes;

    /**
     * Constructor de la vista de administración de las peliculas.
     * Inicializa el Grid y el formulario, y los agrega al diseño.
     */
    public AdminPeliculasView() {

        coleccionPeliculas = PeliculaDao.obtenerPelicula();

        iniciarGrid();
        iniciarFormulario();

        add(grid, formLayout);

    }

    /**
     * Inicializa el formulario para añadir peliculas
     */
    private void iniciarFormulario() {

        TextField tituloField = new TextField("Título");
        TextField descripcionField = new TextField("Descripcion");
        IntegerField anyoPublicacionField = new IntegerField("Año de publicacion");
        TextField duracionField = new TextField("Duracion");
        Select<Categoria> categoriaField = new Select<>();
        categoriaField.setLabel("Categoria");
        categoriaField.setItems(Categoria.values());
        Select<Formato> formatoField = new Select<>();
        formatoField.setLabel("Formato");
        formatoField.setItems(Formato.values());
        Select<Valoracion> valoracionField = new Select<>();
        valoracionField.setLabel("Valoracion");
        valoracionField.setItems(Valoracion.values());
        MultiSelectComboBox<Equipo> relActor = new MultiSelectComboBox<>("Actores");
        List<Equipo> actoresL = EquipoDao.obtenerActor(actor);
        relActor.setItems(actoresL);
        relActor.setItemLabelGenerator(Equipo::getNombre);
        TextArea actoresSelec = new TextArea("Actores seleccionados");
        actoresSelec.setReadOnly(true);

        relActor.addValueChangeListener(e -> {
            String actoresSelecText = e.getValue().stream().map(Equipo::getNombre).collect(Collectors.joining(", "));

            actoresSelec.setValue(actoresSelecText);

        });
        MultiSelectComboBox<Equipo> relDirector = new MultiSelectComboBox<>("Directores");
        List<Equipo> directoresL = EquipoDao.obtenerDirector(director);
        relDirector.setItems(directoresL);
        relDirector.setItemLabelGenerator(Equipo::getNombre);
        TextArea directoresSelec = new TextArea("Directores seleccionados");
        directoresSelec.setReadOnly(true);

        relDirector.addValueChangeListener(e -> {
            String directoresSelecText = e.getValue().stream().map(Equipo::getNombre).collect(Collectors.joining(", "));

            directoresSelec.setValue(directoresSelecText);

        });

        multiFileMemoryBuffer = new MultiFileMemoryBuffer();
        imagenUpload = new Upload(multiFileMemoryBuffer);
        imagenUpload.setUploadButton(new Button("Subir portada"));
        imagenUpload.setAcceptedFileTypes("image/jpg, image/png");

        imagenUpload.addSucceededListener(e -> {
            try {
                InputStream imagenInputStream = multiFileMemoryBuffer.getInputStream(e.getFileName());
                imagenBytes = IOUtils.toByteArray(imagenInputStream);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });

        formLayout.setResponsiveSteps(
                // Use one column by default
                new ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new ResponsiveStep("500px", 2));

        Button añadir = new Button("Añadir");
        añadir.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        añadir.addClickListener(e -> {

            Pelicula pelicula = new Pelicula(tituloField.getValue(), descripcionField.getValue(),
                    anyoPublicacionField.getValue(), duracionField.getValue(), categoriaField.getValue(),
                    formatoField.getValue(), valoracionField.getValue());

            Set<Equipo> actoresSeleccionados = relActor.getSelectedItems();
            pelicula.setActores(actoresSeleccionados);

            Set<Equipo> directoresSeleccionados = relDirector.getSelectedItems();
            pelicula.setDirectores(directoresSeleccionados);

            pelicula.setImagenByte(imagenBytes);

            PeliculaDao.guardarPelicula(pelicula);

            Notification popup = Notification.show("Pelicula añadida correctamente");
            popup.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            tituloField.clear();
            descripcionField.clear();
            anyoPublicacionField.clear();
            duracionField.clear();
            categoriaField.clear();
            formatoField.clear();
            valoracionField.clear();
            relActor.clear();
            relDirector.clear();
            imagenUpload.clearFileList();

            grid.getDataProvider().refreshAll();
            grid.setItems(coleccionPeliculas);

            UI.getCurrent().getPage().reload();

        });

        formLayout.add(tituloField, descripcionField, anyoPublicacionField, duracionField, categoriaField, formatoField,
                valoracionField, relActor, relDirector, imagenUpload, añadir);
    }

    /**
     * Inicializa el grid para listar las peliculas
     */
    private void iniciarGrid() {

        grid.setItems(coleccionPeliculas);
        grid.addColumn(Pelicula::getId).setHeader("ID");
        grid.addColumn(Pelicula::getTitulo).setHeader("Título");
        grid.addColumn(Pelicula::getDescripcion).setHeader("Descripción");
        grid.addColumn(Pelicula::getAñopublicacion).setHeader("Año de publicacion");
        grid.addColumn(Pelicula::getDuracion).setHeader("Duración");
        grid.addColumn(Pelicula::getFormato).setHeader("Formato");
        grid.addColumn(Pelicula::getCategoria).setHeader("Categoria");
        grid.addColumn(Pelicula::getValoracion).setHeader("Valoración");

        grid.addColumn(new ComponentRenderer<>(Button::new, (eliminarBoton, pelicula) -> {

            eliminar(eliminarBoton, pelicula);

        })).setHeader("Eliminar");

        grid.addColumn(new ComponentRenderer<>(Button::new, (editarBoton, pelicula) -> {

            modificarForm(editarBoton, pelicula);

        })).setHeader("Modificar");
    }

    /**
     * Elimina la pelicula seleccionada
     *
     * @param eliminarBoton
     * @param pelicula
     */
    private void eliminar(Button eliminarBoton, Pelicula pelicula) {

        eliminarBoton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_TERTIARY);

        eliminarBoton.addClickListener(e -> {

            if (pelicula != null) {
                PeliculaDao.eliminarPelicula(pelicula);
                UI.getCurrent().getPage().reload();

                Notification popup = Notification.show("Pelicula eliminada correctamente");
                popup.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }
        });

        eliminarBoton.setIcon(new Icon(VaadinIcon.TRASH));

    }

    /**
     * Modifica la pelicula modificada
     *
     * @param editarBoton
     * @param pelicula
     */
    private void modificarForm(Button editarBoton, Pelicula pelicula) {

        editarBoton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_TERTIARY);

        List<Equipo> listaActores = EquipoDao.obtenerActor(actor);
        List<Equipo> listaDirectores = EquipoDao.obtenerDirector(director);

        editarBoton.addClickListener(event -> {
            peliculaSeleccionada = pelicula;
            FormLayout formulario = new FormLayout();
            TextField tituloField = new TextField("Título");
            tituloField.setValue(pelicula.getTitulo());
            formulario.add(tituloField);
            TextField descripcionField = new TextField("Descripcion");
            descripcionField.setValue(pelicula.getDescripcion());
            formulario.add(descripcionField);
            IntegerField añoField = new IntegerField("Año de publicacion");
            añoField.setValue(pelicula.getAñopublicacion());
            formulario.add(añoField);
            TextField duracionField = new TextField("Duracion");
            duracionField.setValue(pelicula.getDuracion());
            formulario.add(duracionField);
            Select<Categoria> categoriaField = new Select<>();
            categoriaField.setLabel("Categoria");
            categoriaField.setItems(Categoria.values());
            categoriaField.setValue(pelicula.getCategoria());
            formulario.add(categoriaField);
            Select<Formato> formatoField = new Select<>();
            formatoField.setLabel("Formato");
            formatoField.setItems(Formato.values());
            formatoField.setValue(pelicula.getFormato());
            formulario.add(formatoField);
            Select<Valoracion> valoracionField = new Select<>();
            valoracionField.setLabel("Valoracion");
            valoracionField.setItems(Valoracion.values());
            valoracionField.setValue(pelicula.getValoracion());
            formulario.add(valoracionField);

            MultiSelectComboBox<Equipo> relActor = new MultiSelectComboBox<>("Actores");
            relActor.setItems(listaActores);
            relActor.setValue(pelicula.getActores());
            formulario.add(relActor);

            MultiSelectComboBox<Equipo> relDirector = new MultiSelectComboBox<>("Directores");
            relDirector.setItems(listaDirectores);
            relDirector.setValue(pelicula.getDirectores());
            formulario.add(relDirector);


            Dialog dialogo = new Dialog();
            dialogo.add(formulario);

            Button guardarBoton = new Button("Guardar");
            guardarBoton.addClickListener(event2 -> {

                peliculaSeleccionada.setTitulo(tituloField.getValue());
                peliculaSeleccionada.setDescripcion(descripcionField.getValue());
                peliculaSeleccionada.setAñopublicacion(añoField.getValue());
                peliculaSeleccionada.setDuracion(duracionField.getValue());
                peliculaSeleccionada.setCategoria(categoriaField.getValue());
                peliculaSeleccionada.setFormato(formatoField.getValue());
                peliculaSeleccionada.setValoracion(valoracionField.getValue());
                Set<Equipo> actoresSeleccionados = relActor.getSelectedItems();
                peliculaSeleccionada.setActores(actoresSeleccionados);
                Set<Equipo> directoresSeleccionados = relDirector.getSelectedItems();
                peliculaSeleccionada.setDirectores(directoresSeleccionados);
                peliculaSeleccionada = pelicula;

                PeliculaDao.modificarPelicula(pelicula);
                grid.getDataProvider().refreshAll();
                grid.setItems(coleccionPeliculas);

                Notification popup = Notification.show("Pelicula modificada correctamente");
                popup.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                dialogo.close();
            });

            formulario.add(guardarBoton);

            editarBoton.setEnabled(true);

            dialogo.open();
        });

        editarBoton.setIcon(new Icon(VaadinIcon.EDIT));

    }

}
