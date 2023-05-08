package org.ieschabas.application.views.main;

import java.io.IOException;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("admin-view")
public class AdminView extends AppLayout {

	public AdminView() throws NumberFormatException, IOException {
		DrawerToggle toggle = new DrawerToggle();

		H1 title = new H1("VideoClubApp");
		title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

		Tabs tabs = getTabs();
		
	

		addToDrawer(tabs);
		addToNavbar(toggle, title);
	}

	/**
	 * Este metodo crea el panel de navegacion
	 * 
	 * @return
	 */
	private Tabs getTabs() {
		Tabs tabs = new Tabs();
		tabs.add(createTabP(VaadinIcon.BOOK, "Películas"), createTabA(VaadinIcon.USER_HEART, "Actores"),
				createTabD(VaadinIcon.USER_HEART, "Directores"));
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
		// Demo has no routes
		link.setRoute(AdminPeliculasView.class);
		link.setTabIndex(-1);

		return new Tab(link);
	}

	/**
	 * Este metodo crea en la opcion Actor la ruta hacia el adminview de actor
	 * 
	 * @param viewIcon
	 * @param viewName
	 * @return
	 */
	private Tab createTabA(VaadinIcon viewIcon, String viewName) {
		Icon icon = viewIcon.create();
		icon.getStyle().set("box-sizing", "border-box").set("margin-inline-end", "var(--lumo-space-m)")
				.set("margin-inline-start", "var(--lumo-space-xs)").set("padding", "var(--lumo-space-xs)");

		RouterLink link = new RouterLink();
		link.add(icon, new Span(viewName));
		// Demo has no routes
		link.setRoute(AdminEquipoView.class);
		link.setTabIndex(-1);

		return new Tab(link);
	}

	/**
	 * Este metodo crea en la opcion Director la ruta hacia el adminview de director
	 * 
	 * @param viewIcon
	 * @param viewName
	 * @return
	 */
	private Tab createTabD(VaadinIcon viewIcon, String viewName) {
		Icon icon = viewIcon.create();
		icon.getStyle().set("box-sizing", "border-box").set("margin-inline-end", "var(--lumo-space-m)")
				.set("margin-inline-start", "var(--lumo-space-xs)").set("padding", "var(--lumo-space-xs)");

		RouterLink link = new RouterLink();
		link.add(icon, new Span(viewName));
		// Demo has no routes
		link.setRoute(AdminEquipoView.class);
		link.setTabIndex(-1);

		return new Tab(link);
	}

}
