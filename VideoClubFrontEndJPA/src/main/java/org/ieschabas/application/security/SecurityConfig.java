package org.ieschabas.application.security;

import java.util.ArrayList;
import java.util.List;

import org.ieschabas.application.views.main.LoginView;
import org.ieschabas.clases.Administrador;
import org.ieschabas.clases.Cliente;
import org.ieschabas.clases.Usuario;
import org.ieschabas.daos.UsuariosDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import com.vaadin.flow.spring.security.VaadinWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
	/*	http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll().and()
                .logout().permitAll().and()
                .csrf().disable()
                .formLogin().disable();*/
		
		super.configure(http);
		setLoginView(http, LoginView.class);
		
		
		
	}

	@Bean
	public UserDetailsService users() {
		List<Usuario> usuarios = UsuariosDao.obtenerUsuario();
		List<UserDetails> listaUsuarios = new ArrayList<UserDetails>();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
		for (Usuario usuario : usuarios) {
			if (usuario instanceof Cliente) {

				UserDetails user = User.builder().username(usuario.getEmail())
						.password("{bcrypt}" + encoder.encode(usuario.getPassword())).authorities(usuario.getRol())
						.build();
				listaUsuarios.add(user);

			} else if (usuario instanceof Administrador) {
				UserDetails user = User.builder().username(usuario.getEmail())
						.password("{bcrypt}" + encoder.encode(usuario.getPassword())).authorities(usuario.getRol())
						.build();
				listaUsuarios.add(user);
			}
		}
		return new InMemoryUserDetailsManager(listaUsuarios);
	}

}
