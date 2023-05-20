package org.ieschabas.application.views.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.apache.commons.io.IOUtils;
import org.ieschabas.application.security.SecurityService;
import org.ieschabas.clases.Cliente;
import org.ieschabas.clases.Pelicula;
import org.ieschabas.daos.PeliculaDao;

import javax.annotation.security.RolesAllowed;

import static org.apache.log4j.xml.DOMConfigurator.setParameter;

@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
@Route("ficha")
public class FichaPeliculaView extends VerticalLayout implements HasUrlParameter<String> {

    Pelicula pelicula = new Pelicula();
    HorizontalLayout layout = new HorizontalLayout();
    ClienteView clienteView;
    Image imagen;
    Button volverButton;

    public FichaPeliculaView() {



    }



    @Override
    public void setParameter(BeforeEvent event, String peliculaTitulo) {
        pelicula = PeliculaDao.obtenerPeliculaPorTitulo(peliculaTitulo);
        volverButton = new Button("", e -> {
            UI.getCurrent().getPage().executeJs("location.href = '/cliente'");
        });
        volverButton.getStyle().set("margin-left", "var(--lumo-space-s)");
        volverButton.setIcon(VaadinIcon.SIGN_OUT.create());

        imagen = new Image();

        imagen = ClienteView.cargarImagen(pelicula);

        layout.add(pelicula.getTitulo());
        layout.add(pelicula.getDescripcion());
        layout.add(imagen);
        layout.add(String.valueOf(pelicula.getAñopublicacion()));
        layout.add(pelicula.getDuracion());
        layout.add(String.valueOf(pelicula.getCategoria()));
        layout.add(String.valueOf(pelicula.getFormato()));
        layout.add(String.valueOf(pelicula.getValoracion()));

        add(volverButton, layout);
    }
}
