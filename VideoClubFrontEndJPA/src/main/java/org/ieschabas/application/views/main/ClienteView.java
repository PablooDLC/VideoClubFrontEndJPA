package org.ieschabas.application.views.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import org.ieschabas.application.security.SecurityService;
import org.ieschabas.clases.Alquiler;
import org.ieschabas.clases.Pelicula;
import org.ieschabas.clases.Usuario;
import org.ieschabas.daos.AlquilerDao;
import org.ieschabas.daos.PeliculaDao;
import org.ieschabas.daos.UsuariosDao;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista de cliente que muestra un catálogo de películas y permite alquilar y ver detalles de las películas.
 * Requiere los roles "ROLE_USER" o "ROLE_ADMIN" para acceder.
 */
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
@Route("cliente")
public class ClienteView extends VerticalLayout {

    static Image imagen;
    List<Pelicula> peliculas = new ArrayList<Pelicula>();
    HorizontalLayout layout = new HorizontalLayout();
    Button alquilarBoton;
    Alquiler alquiler;
    Usuario cliente;
    UserDetails userDetails;
    SecurityService securityService;
    Button fichaBoton;
    RouterLink link;
    Button logoutButton;

    /**
     * Inicializa constructor de la clase
     *
     * @param securityService
     * @throws IOException
     */
    public ClienteView(SecurityService securityService) throws IOException {
        this.securityService = securityService;

        peliculas = PeliculaDao.obtenerPelicula();
        userDetails = securityService.getAuthenticatedUser();
        cliente = UsuariosDao.obtenerUsuarioEmail(userDetails.getUsername());

        iniciarLayout();

    }

    /**
     * Recoge los bytes de la imagen y la muestra
     *
     * @param pelicula
     * @return
     */
    public static Image cargarImagen(Pelicula pelicula) {

        imagen = new Image();
        imagen.setWidth("250px");
        byte[] imagenBytes = pelicula.getImagenByte();
        StreamResource resource = new StreamResource("imagen.png", () -> new ByteArrayInputStream(imagenBytes));
        imagen.setSrc(resource);
        imagen.getElement().setAttribute("alt", "Imagen");

        return imagen;
    }

    /**
     * Inicializa el catalogo y los botones y los agrega a la vista
     *
     * @throws IOException
     */
    private void iniciarLayout() throws IOException {

        H1 cabecera = new H1("Catalogo de peliculas");
        cabecera.getElement().getStyle().set("justify-content", "center").set("color", "blue").set("font-size", "40px");

        logoutButton = new Button("", event -> {
            UI.getCurrent().getPage().executeJs("location.href = '/login'");
        });
        logoutButton.getStyle().set("margin-left", "var(--lumo-space-s)");
        logoutButton.setIcon(VaadinIcon.SIGN_OUT.create());

        layout.getElement().getStyle().set("display", "flex").set("flex-wrap", "wrap").set("width", "100%");

        for (Pelicula pelicula : peliculas) {

            cargarImagen(pelicula);

            VerticalLayout contenedorP = new VerticalLayout();
            contenedorP.getElement().getStyle().set("width", "32%");
            contenedorP.add(imagen);
            contenedorP.add("Categoria: " + pelicula.getCategoria() + " ");
            contenedorP.add("Valoracion: " + pelicula.getValoracion());

            alquilarBoton = new Button("Alquilar");

            alquilarBoton.addClickListener(e -> {

                alquiler = new Alquiler(pelicula, cliente, LocalDate.now(), LocalDate.now().plusDays(3));

                AlquilerDao.guardarAlquiler(alquiler);

                Notification popup = Notification.show("Pelicula alquilada");
                popup.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            });

            link = new RouterLink();
            link.add(new Span("Ver ficha"));
            link.setRoute(FichaPeliculaView.class, pelicula.getTitulo());
            link.setTabIndex(-1);

            contenedorP.add(alquilarBoton);
            contenedorP.add(link);

            layout.add(contenedorP);
        }

        add(cabecera, logoutButton, layout);
    }
}
