package org.ieschabas.application.views.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.ieschabas.application.security.SecurityService;
import org.ieschabas.clases.Alquiler;
import org.ieschabas.clases.Equipo;
import org.ieschabas.clases.Pelicula;
import org.ieschabas.clases.Usuario;
import org.ieschabas.daos.AlquilerDao;
import org.ieschabas.daos.PeliculaDao;
import org.ieschabas.daos.UsuariosDao;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista que muestra la ficha de una película y permite alquilarla.
 * Requiere los roles "ROLE_USER" y "ROLE_ADMIN" para acceder.
 */
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
@Route("ficha")
public class FichaPeliculaView extends HorizontalLayout implements HasUrlParameter<String> {

    Pelicula pelicula = new Pelicula();
    HorizontalLayout layout = new HorizontalLayout();
    ClienteView clienteView;
    Image imagen;
    Button volverButton;
    Button alquilarBoton;
    VerticalLayout vl;
    Alquiler alquiler;
    Usuario cliente;
    List<Pelicula> peliculas = new ArrayList<Pelicula>();
    UserDetails userDetails;
    SecurityService securityService;

    HorizontalLayout ficha;

    /**
     * Inicializa el constructor de la clase
     *
     * @param securityService
     */
    public FichaPeliculaView(SecurityService securityService) {

        this.securityService = securityService;

        peliculas = PeliculaDao.obtenerPelicula();
        userDetails = securityService.getAuthenticatedUser();
        cliente = UsuariosDao.obtenerUsuarioEmail(userDetails.getUsername());

    }

    /**
     * Establece el parametro de la ruta de la pelicula seleccionada
     *
     * @param event          the navigation event that caused the call to this method
     * @param peliculaTitulo the resolved url parameter
     */
    @Override
    public void setParameter(BeforeEvent event, String peliculaTitulo) {
        ficha = new HorizontalLayout();
        ficha.getStyle().set("width", "100%").set("display", "flex").set("flex-wrap", "wrap").set("justify-content", "center").set("align-items", "center");
        pelicula = PeliculaDao.obtenerPeliculaPorTitulo(peliculaTitulo);

        imagen = new Image();


        divImagen();
        divTituloDescripcionAnyo();
        divDuracionCategFormVal();
        divEquipo();
        divBotones();

        this.getElement().getStyle().set("width", "100%");

        add(ficha);
    }

    /**
     * Bloque de botones para alquilar y volver al catalogo en la ficha
     */
    private void divBotones() {

        VerticalLayout vl = new VerticalLayout();
        vl.getStyle().set("justify-content", "center").set("align-items", "center").set("width", "50%");
        vl.getStyle().set("padding", "10px");
        vl.getStyle().set("border", "1px solid gray");
        vl.getStyle().set("border-radius", "2px");
        vl.getStyle().set("margin-left", "20px");
        vl.getStyle().set("margin-right", "5px");

        volverButton = new Button("", e -> {
            UI.getCurrent().getPage().executeJs("location.href = '/cliente'");
        });
        volverButton.setIcon(VaadinIcon.SIGN_OUT.create());

        alquilarBoton = new Button("Alquilar");

        alquilarBoton.addClickListener(e -> {

            alquiler = new Alquiler(pelicula, cliente, LocalDate.now(), LocalDate.now().plusDays(3));

            AlquilerDao.guardarAlquiler(alquiler);

            Notification popup = Notification.show("Pelicula alquilada");
            popup.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        });

        vl.add(volverButton);
        vl.add(alquilarBoton);

        ficha.add(vl);

    }

    /**
     * Bloque que contiene los actores y directores de cada pelicula
     */
    private void divEquipo() {

        VerticalLayout vl = new VerticalLayout();
        Div actores = new Div();
        actores.getStyle().set("width", "100%");
        Div directores = new Div();
        directores.getStyle().set("width", "100%");


        StringBuilder actoresStringBuilder = new StringBuilder();
        for (Equipo actor : pelicula.getActores()) {
            String nombreCompleto = actor.getNombre() + " " + actor.getApellidos();
            actoresStringBuilder.append(nombreCompleto).append(", ");
        }
        if (actoresStringBuilder.length() > 0) {
            actoresStringBuilder.delete(actoresStringBuilder.length() - 2, actoresStringBuilder.length()); // Eliminar la última coma y espacio
        }
        actores.add("Actores: " + actoresStringBuilder + " ");
        vl.add(actores);

        StringBuilder directoresStringBuilder = new StringBuilder();
        for (Equipo director : pelicula.getDirectores()) {
            String nombreCompleto = director.getNombre() + " " + director.getApellidos();
            directoresStringBuilder.append(nombreCompleto).append(", ");
        }
        if (directoresStringBuilder.length() > 0) {
            directoresStringBuilder.delete(directoresStringBuilder.length() - 2, directoresStringBuilder.length()); // Eliminar la última coma y espacio
        }
        directores.add("Directores: " + directoresStringBuilder + " ");
        vl.add(directores);

        vl.getStyle().set("width", "50%");
        vl.getStyle().set("padding", "20px");
        vl.getStyle().set("border", "1px solid gray");
        vl.getStyle().set("border-radius", "5px");
        vl.getStyle().set("margin-left", "20px");
        vl.getStyle().set("margin-right", "20px");

        ficha.add(vl);

    }

    /**
     * Bloque que contiene la duracion, categoria, formato y la valoracion de la pelicula
     */
    private void divDuracionCategFormVal() {

        VerticalLayout vl = new VerticalLayout();
        vl.getStyle().set("width", "50%");
        vl.add("Duracion: " + pelicula.getDuracion() + " ");
        vl.add("Categoria: " + pelicula.getCategoria() + " ");
        vl.add("Formato: " + pelicula.getFormato() + " ");
        vl.add("Valoracion: " + pelicula.getValoracion());

        ficha.add(vl);

    }

    /**
     * Bloque que contiene el titulo, descripcion y año de publicacion
     */
    private void divTituloDescripcionAnyo() {

        HorizontalLayout vl = new HorizontalLayout();

        vl.add(pelicula.getTitulo() + " ");
        vl.add(pelicula.getDescripcion() + " ");
        vl.add(String.valueOf(pelicula.getAñopublicacion()));

        vl.getStyle().set("font-size", "24px").set("width", "35%");

        ficha.add(vl);

    }

    /**
     * Bloque que contiene la imagen de la pelicula
     */
    private void divImagen() {

        Div vlI = new Div();
        imagen = ClienteView.cargarImagen(pelicula);
        imagen.setHeight("700px");
        imagen.setWidth("500px");
        vlI.add(imagen);
        vlI.getStyle().set("padding", "30px").set("width", "35%");

        ficha.add(vlI);

    }

}
