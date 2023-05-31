package org.ieschabas.application.views.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;

/**
 * Vista de admin que muestra un panel de navegación y contenido de administración.
 * Requiere el rol "ROLE_ADMIN" para acceder.
 */
@Route("admin-view")
@RolesAllowed("ROLE_ADMIN")
public class AdminView extends AppLayout {

    /**
     * Inicializa el constructor de la clase
     *
     * @throws NumberFormatException
     * @throws IOException
     */
    public AdminView() throws NumberFormatException, IOException {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("VideoClubApp");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        Tabs tabs = getTabs();

        AdminPeliculasView adminPeliculasView = new AdminPeliculasView();

        Button logoutButton;
        logoutButton = new Button("", event -> {
            UI.getCurrent().getPage().executeJavaScript("location.href = '/login'");
        });
        logoutButton.getStyle().set("margin-left", "var(--lumo-space-s)");
        logoutButton.setIcon(VaadinIcon.SIGN_OUT.create());

        setContent(adminPeliculasView);
        addToDrawer(tabs);
        addToNavbar(toggle, title, logoutButton);
    }

    /**
     * Este metodo crea el panel de navegacion
     *
     * @return
     */
    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(createTabP(VaadinIcon.BOOK, "Películas"), createTabD(VaadinIcon.USER_HEART, "Actores", "Actor"), createTabD(VaadinIcon.USER_HEART, "Directores", "Director"), createTabAlquiler(VaadinIcon.BOOK, "Alquileres"));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    /**
     * Este metodo crea en la opcion Pelicula la ruta hacia el adminview de pelicula
     *
     * @param viewIcon
     * @param viewName
     * @return
     */
    private Tab createTabP(VaadinIcon viewIcon, String viewName) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box").set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)").set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setRoute(AdminPeliculasView.class);
        link.setTabIndex(-1);

        return new Tab(link);
    }

    /**
     * Este metodo crea en la opcion Equipo la ruta hacia el adminview de Equipo
     *
     * @param viewIcon
     * @param viewName
     * @return
     */
    private Tab createTabD(VaadinIcon viewIcon, String viewName, String rol) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box").set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)").set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        // Demo has no routes
        link.setRoute(AdminEquipoView.class, rol);
        link.setTabIndex(-1);

        return new Tab(link);
    }

    /**
     * Este metodo crea en la opcion Alquiler la ruta hacia el adminview de Alquiler
     *
     * @param viewIcon
     * @param viewName
     * @return
     */
    private Tab createTabAlquiler(VaadinIcon viewIcon, String viewName) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box").set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)").set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        // Demo has no routes
        link.setRoute(AdminAlquileresView.class);
        link.setTabIndex(-1);

        return new Tab(link);
    }

}
