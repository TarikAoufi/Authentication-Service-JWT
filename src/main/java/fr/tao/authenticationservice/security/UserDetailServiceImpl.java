package fr.tao.authenticationservice.security;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.tao.authenticationservice.entities.AppUser;
import fr.tao.authenticationservice.service.UserService;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	static final Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);
	private UserService accountService;
	
	public UserDetailServiceImpl(UserService accountService) {
		this.accountService = accountService;
	}
	
	/**
	 * Permet de chercher un utilisateur lors de son authentification
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = accountService.findUserByUsername(username);
		logger.info("Utilisateur : {} ", user);
		if (user == null)
			throw new UsernameNotFoundException("Aucun utilisateur trouvé avec le username: " + username);
		
		Collection<GrantedAuthority> authorities = getAuthorities(user);
		return new User(user.getUsername(), user.getPassword(), authorities);
	}
	
	public Collection<GrantedAuthority> getAuthorities(AppUser user) {
		final Collection<GrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(
				r -> authorities.add(new SimpleGrantedAuthority(r.getRoleName())));
		return authorities;
	}

}
