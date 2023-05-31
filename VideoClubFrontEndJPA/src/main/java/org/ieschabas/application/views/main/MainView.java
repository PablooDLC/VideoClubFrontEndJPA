package org.ieschabas.application.views.main;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.security.PermitAll;

/**
 * Esta vista redirige segun el rol que obtenga el login
 * Pueden acceder todos los usuarios
 */
@PermitAll
@Route("/")
public class MainView extends AppLayout implements BeforeEnterObserver {

    LoginForm loginForm = new LoginForm();

    /**
     * Inicializa el constructor de la clase
     */
    public MainView() {
        super();
        //PropertyConfigurator.configure("src/main/resources/log4.properties");
    }

    /**
     * Metodo que se ejecuta antes de que cargue la vista
     * Redirige segun el rol a una vista u otra
     *
     * @param event before navigation event with event details
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
                event.rerouteTo(ClienteView.class);
            } else {
                event.rerouteTo(AdminView.class);
            }
        }
    }
}