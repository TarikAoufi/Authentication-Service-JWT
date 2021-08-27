package fr.tao.authenticationservice.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tao.authenticationservice.entities.AppUser;
import fr.tao.authenticationservice.entities.Role;
import fr.tao.authenticationservice.entities.RoleName;
import fr.tao.authenticationservice.error.UserNotFoundException;
import fr.tao.authenticationservice.repository.RoleRepository;
import fr.tao.authenticationservice.repository.UserRepository;
import fr.tao.authenticationservice.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	public static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public AppUser addUser(AppUser user) {
		String pw = user.getPassword();
		if (pw != null)
			user.setPassword(passwordEncoder.encode(pw));

		// rôle par défaut
		Role defaultRole = roleRepository.findByRoleName(RoleName.USER.getRole());

		Set<Role> roles = user.getRoles();
		if (!roles.isEmpty()) {
			user.setRoles(new HashSet<>());
			roles.forEach(r -> {
				Role role = Optional.ofNullable(roleRepository.findByRoleName(r.getRoleName())).orElse(defaultRole);
				user.getRoles().add(role);
			});
		}

		user.getRoles().add(defaultRole);

		return userRepository.save(user);
	}

	@Override
	public AppUser findUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User id not found : " + id));
	}

	@Override
	public AppUser findUserByUsername(String username) {
		return Optional.ofNullable(userRepository.findByUsername(username))
				.orElseThrow(() -> new UsernameNotFoundException("This username is not found : " + username));
	}

	@Override
	public AppUser saveOrUpdate(AppUser user) {
		// rôle par défaut
		Role defaultRole = roleRepository.findByRoleName(RoleName.USER.getRole());
		return userRepository.findById(user.getId()).map(u -> {
			u.setUsername(Optional.ofNullable(user.getUsername())
					.orElse(u.getUsername()));
			if (user.getPassword() != null)
				u.setPassword(passwordEncoder.encode(user.getPassword()));

			Set<Role> roles = user.getRoles();
			if (!roles.isEmpty()) {
				u.setRoles(new HashSet<>());
				roles.forEach(r -> {
					Role role = Optional.ofNullable(roleRepository.findByRoleName(r.getRoleName()))
							.orElse(defaultRole);
					u.getRoles().add(role);
				});
			} else {
				u.setRoles(userRepository.getOne(user.getId()).getRoles());
			}
			return userRepository.save(u);
		}).orElseGet(() -> {
			String pw = user.getPassword();
			if (pw != null)
				user.setPassword(passwordEncoder.encode(pw));

			Set<Role> roles = user.getRoles();
			if (roles.isEmpty()) {
				user.getRoles().add(defaultRole);
			} else {
				user.setRoles(new HashSet<>());
				roles.forEach(r -> {
					Role role = Optional.ofNullable(roleRepository.findByRoleName(r.getRoleName()))
							.orElse(defaultRole);
					user.getRoles().add(role);
				});

				user.setRoles(user.getRoles());
			}
			return userRepository.save(user);
		});
	}

	@Override
	public AppUser updateUser(AppUser user) {

		AppUser entity = findUserById(user.getId());

		entity.setUsername(Optional.ofNullable(user.getUsername())
				.orElse(entity.getUsername()));

		if (user.getPassword() != null)
			entity.setPassword(passwordEncoder.encode(user.getPassword()));

		Set<Role> roles = user.getRoles();
		if (!roles.isEmpty()) {
			entity.setRoles(new HashSet<>());
			roles.forEach(r -> {
				Role role = Optional.ofNullable(roleRepository.findByRoleName(r.getRoleName()))
						.orElse(roleRepository.findByRoleName(RoleName.USER.getRole()));
				entity.getRoles().add(role);
			});
		} else {
			entity.setRoles(userRepository.getOne(user.getId()).getRoles());
		}
		return userRepository.save(entity);
	}

	@Override
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		AppUser user = userRepository.findByUsername(username);
		if (user == null) {
			user = addUser(new AppUser(null, username, null, new HashSet<>()));
			saveOrUpdate(user);
		}
		Role role = Optional.ofNullable(roleRepository.findByRoleName(roleName))
				.orElse(roleRepository.findByRoleName(RoleName.USER.getRole()));
		user.getRoles().add(role);
	}

	@Override
	public List<AppUser> listUsers() {
		return userRepository.findAll();
	}

}
