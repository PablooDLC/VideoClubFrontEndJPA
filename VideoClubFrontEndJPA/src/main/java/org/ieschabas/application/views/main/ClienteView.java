package org.ieschabas.application.views.main;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.apache.coyote.http11.Http11Processor;
import org.ieschabas.clases.Pelicula;
import org.ieschabas.daos.PeliculaDao;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RolesAllowed("ROLE_USER")
@Route("cliente")
public class ClienteView extends VerticalLayout {

	List<Pelicula> peliculas = new ArrayList<Pelicula>();
	HorizontalLayout layout = new HorizontalLayout();


	public ClienteView() {

		peliculas = PeliculaDao.obtenerPelicula();

		iniciarLayout();
		
	}

	private void iniciarLayout() {

		layout.getElement().getStyle().set("display", "flex").set("flex-wrap", "nowrap").set("width", "100%");

		for (Pelicula pelicula : peliculas) {
			VerticalLayout contenedorP = new VerticalLayout();
			Div divP = new Div();
			divP.add(pelicula.getTitulo());
			divP.add(pelicula.getValoracion().toString());
			contenedorP.getElement().getStyle().set("display", "flex");

			contenedorP.add(divP);
			/*contenedorP.add(pelicula.getTitulo());
			contenedorP.add(pelicula.getValoracion().toString());*/

			Button alquilar = new Button("Alquilar");



			layout.add(contenedorP);
		}

		add(new H1("Catalogo de peliculas"), layout);
	}
}
