package curso.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import curso.api.rest.service.ImplementacaoUserDetailsService;

//Mapeia URL, endereços, autoriza ou bloqueia a URL
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;

	// Configura as solicitações de acesso por HTTP
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Ativando a proteção contra usuário que não estão validados por token
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				// Ativando a restrição a URl
				.disable() // Desativa as configurações padrão de memória do Spring
				// Ativando a permissão para acesso página inicial do sistema, Exemplo:
				// index.html
				.authorizeHttpRequests().antMatchers("/").permitAll().antMatchers("/index").permitAll()
				
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				// URL de Logout - Redireciona após o user deslogar do sistema
				.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")

				// Mapeia a URL de Logout e invalida o Usuário
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))

		// Filtra requisições de login para autenticação
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), 
				UsernamePasswordAuthenticationFilter.class)
		// Filtra demais requisições para verificar a presenção do TOKEN JWT no HEADER HTTP
		.addFilterBefore(new JWTApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Service que irá consultar o usuário no BD
		auth.userDetailsService(implementacaoUserDetailsService)
				// Padrão de codificação da senha
				.passwordEncoder(new BCryptPasswordEncoder());
	}
}
