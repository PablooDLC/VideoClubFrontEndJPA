package org.ieschabas.application.views.main;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

/**
 * Vista de inicio de sesión que muestra un formulario de inicio de sesión
 * Permite acceder a todos los usuarios
 */
@PermitAll
@Route("login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private static final long serialVersionUID = 6148132937265716136L;
    private final LoginForm loginForm = new LoginForm();

    /**
     * Inicializa el constructor de la clase
     * Configura la apariencia y el diseño de la vista de inicio de sesión.
     */
    public LoginView() {

        addClassName("login");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);


        loginForm.setAction("login");
        add(loginForm);

    }

    /**
     * Este metodo que se ejecuta antes de que se cargue la vista de inicio de sesión.
     * Comprueba si ha habido un error y muestra un mensaje de error.
     *
     * @param event
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        // inform the user about an authentication error
        if (event.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }

}
