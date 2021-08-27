package fr.tao.authenticationservice;

import java.util.HashSet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.tao.authenticationservice.entities.AppUser;
import fr.tao.authenticationservice.entities.Role;
import fr.tao.authenticationservice.entities.RoleName;
import fr.tao.authenticationservice.service.RoleService;
import fr.tao.authenticationservice.service.UserService;

@SpringBootApplication
public class AuthenticationServiceApplication {
	
	private static final String ADMIN = "admin";
	private static final String USER1 = "user1";
	private static final String USER2 = "user2";
	private static final String USER3 = "user3";
	private static final String USER4 = "user4";

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}
	
	/**
	 * Cette méthode permet de crypter le mot de passe dans la base de données, 
	 * on utilisant la classe BCryptPasswordEncoder
	 * 
	 */
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CommandLineRunner start(UserService userService, RoleService roleService) {
		return args -> {
			roleService.addRole(new Role(null, RoleName.USER.getRole()));
			roleService.addRole(new Role(null, RoleName.ADMIN.getRole()));
			roleService.addRole(new Role(null, RoleName.CUSTOMER_MANAGER.getRole()));
			roleService.addRole(new Role(null, RoleName.PRODUCT_MANAGER.getRole()));
			roleService.addRole(new Role(null, RoleName.ORDER_MANAGER.getRole()));
			
			userService.addUser(new AppUser(null, USER1, "123", new HashSet<>()));
			userService.addUser(new AppUser(null, ADMIN, "123", new HashSet<>()));
			userService.addUser(new AppUser(null, USER2, "123", new HashSet<>()));
			userService.addUser(new AppUser(null, USER3, "123", new HashSet<>()));
			userService.addUser(new AppUser(null, USER4, "123", new HashSet<>()));
			
			userService.addRoleToUser(USER1, RoleName.USER.getRole());
			userService.addRoleToUser(ADMIN, RoleName.USER.getRole());
			userService.addRoleToUser(ADMIN, RoleName.ADMIN.getRole());
			userService.addRoleToUser(USER2, RoleName.USER.getRole());
			userService.addRoleToUser(USER2, RoleName.CUSTOMER_MANAGER.getRole());
			userService.addRoleToUser(USER3, RoleName.USER.getRole());
			userService.addRoleToUser(USER3, RoleName.PRODUCT_MANAGER.getRole());
			userService.addRoleToUser(USER4, RoleName.USER.getRole());
			userService.addRoleToUser(USER4, RoleName.ORDER_MANAGER.getRole());
		};
	}

}
