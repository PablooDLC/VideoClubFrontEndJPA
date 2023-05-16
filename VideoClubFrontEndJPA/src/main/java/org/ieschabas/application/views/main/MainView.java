package org.ieschabas.application.views.main;

import javax.annotation.security.PermitAll;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;


@PermitAll
@Route("/")
public class MainView extends AppLayout implements BeforeEnterObserver {
	
	LoginForm loginForm = new LoginForm();
	
	public MainView() {
		super();
		//PropertyConfigurator.configure("src/main/resources/log4.properties");
	}
	
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
			if(grantedAuthority.getAuthority().equals("ROLE_USER")) {
				event.rerouteTo(ClienteView.class);
			} else {
				event.rerouteTo(AdminView.class);
			}
		}
	}
}