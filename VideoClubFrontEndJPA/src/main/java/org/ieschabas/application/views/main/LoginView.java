package org.ieschabas.application.views.main;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@PermitAll
@Route("login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6148132937265716136L;
	private final LoginForm loginForm = new LoginForm();
	
	public LoginView() {
        
		addClassName("login");
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		
		
        loginForm.setAction("login");
        add(loginForm);
        
    }

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		
		// inform the user about an authentication error
		if(event.getLocation()
		        .getQueryParameters()
		        .getParameters()
		        .containsKey("error")) {
		            loginForm.setError(true);
		        }
	}
	
}
