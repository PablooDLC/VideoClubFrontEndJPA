package org.ieschabas.application.views.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.ieschabas.clases.Equipo;
import org.ieschabas.clases.Pelicula;
import org.ieschabas.daos.EquipoDao;
import org.ieschabas.daos.PeliculaDao;
import org.ieschabas.enums.Categoria;
import org.ieschabas.enums.Formato;
import org.ieschabas.enums.Valoracion;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class FormularioPeliculaView extends Div {

	List<Pelicula> coleccionPeliculas = new ArrayList<Pelicula>();
	Grid<Pelicula> gridP;
	String actor = "Actor";
	String director = "Director";

	public FormularioPeliculaView() {

		coleccionPeliculas = PeliculaDao.ObtenerPelicula();

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
		List<Equipo> actoresL = EquipoDao.ObtenerActor(actor);
		relActor.setItems(actoresL);
		relActor.setItemLabelGenerator(Equipo::getNombre);
		TextArea actoresSelec = new TextArea("Actores seleccionados");
		actoresSelec.setReadOnly(true);

		relActor.addValueChangeListener(e -> {
			String actoresSelecText = e.getValue().stream().map(Equipo::getNombre).collect(Collectors.joining(", "));

			actoresSelec.setValue(actoresSelecText);

		});
		MultiSelectComboBox<Equipo> relDirector = new MultiSelectComboBox<>("Directores");
		List<Equipo> directoresL = EquipoDao.ObtenerDirector(director);
		relDirector.setItems(directoresL);
		relDirector.setItemLabelGenerator(Equipo::getNombre);
		TextArea directoresSelec = new TextArea("Directores seleccionados");
		directoresSelec.setReadOnly(true);

		relDirector.addValueChangeListener(e -> {
			String directoresSelecText = e.getValue().stream().map(Equipo::getNombre).collect(Collectors.joining(", "));

			directoresSelec.setValue(directoresSelecText);

		});

		FormLayout formLayout = new FormLayout();
		formLayout.add(tituloField, descripcionField, anyoPublicacionField, duracionField, categoriaField, formatoField,
				valoracionField, relActor, relDirector);
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

			gridP.getDataProvider().refreshAll();
			gridP.setItems(coleccionPeliculas);

		});

		add(formLayout, añadir);

	}

}
