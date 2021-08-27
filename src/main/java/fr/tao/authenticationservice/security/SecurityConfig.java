package fr.tao.authenticationservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import fr.tao.authenticationservice.filters.JwtAuthenticationFilter;
import fr.tao.authenticationservice.filters.JwtAuthorizationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
			
	private UserDetailServiceImpl userDetailService;
	private BCryptPasswordEncoder passwordEncoder;

	public SecurityConfig(UserDetailServiceImpl userDetailService, BCryptPasswordEncoder passwordEncoder) {
		this.userDetailService = userDetailService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/refreshToken/**", "/login/**").permitAll();
		//http.formLogin();
		//http.authorizeRequests().antMatchers(HttpMethod.POST, "/users/**").hasAnyAuthority(RoleName.ADMIN.getRole());
		//http.authorizeRequests().antMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority(RoleName.USER.getRole());
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean()));
		http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	/**
	 * Permet de désactiver l'ajout du préfixe ROLE_
	 * 
	 * Spring Security ajoute le préfixe ROLE_ au nom du rôle par défaut.
	 * Par ex. avec l'annotation @Secured("ADMIN"), 
	 * Spring recherchera le rôle ROLE_ADMIN.
	 */
	@Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Supprime le préfixe ROLE_
    }
		
}
