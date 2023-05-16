package org.ieschabas.application.views.main;

import javax.annotation.security.RolesAllowed;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.apache.coyote.http11.Http11Processor;
import org.ieschabas.application.security.SecurityService;
import org.ieschabas.clases.*;
import org.ieschabas.daos.AlquilerDao;
import org.ieschabas.daos.PeliculaDao;
import org.ieschabas.daos.UsuariosDao;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RolesAllowed("ROLE_USER")
@Route("cliente")
public class ClienteView extends VerticalLayout {

	List<Pelicula> peliculas = new ArrayList<Pelicula>();
	HorizontalLayout layout = new HorizontalLayout();
	Button alquilarBoton;
	Alquiler alquiler;
	Usuario cliente;

	UserDetails userDetails;

	SecurityService securityService;


	public ClienteView(SecurityService securityService) {
		this.securityService = securityService;

		peliculas = PeliculaDao.obtenerPelicula();
		userDetails = securityService.getAuthenticatedUser();
		cliente = UsuariosDao.obtenerUsuarioEmail(userDetails.getUsername());

		iniciarLayout();
		
	}

	private void iniciarLayout() {

		H1 cabecera = new H1("Catalogo de peliculas");
		cabecera.getElement().getStyle().set("text-align", "center").set("color", "blue");
		layout.getElement().getStyle().set("display", "flex").set("flex-wrap", "wrap").set("width", "100%");

		for (Pelicula pelicula : peliculas) {
			VerticalLayout contenedorP = new VerticalLayout();
			contenedorP.getElement().getStyle().set("width","25%");
			contenedorP.add(pelicula.getTitulo());
			contenedorP.add(pelicula.getValoracion().toString());

			alquilarBoton = new Button("Alquilar");

			alquilarBoton.addClickListener(e -> {

				alquiler = new Alquiler(pelicula, cliente, LocalDate.now(), LocalDate.now().plusDays(3));

				AlquilerDao.guardarAlquiler(alquiler);

				Notification popup = Notification.show("Pelicula alquilada");
				popup.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

			});

			// Para que un cliente

			layout.add(contenedorP, alquilarBoton);
		}

		add(cabecera, layout);
	}
}
