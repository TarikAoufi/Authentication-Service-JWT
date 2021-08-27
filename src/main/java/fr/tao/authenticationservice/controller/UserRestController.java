package fr.tao.authenticationservice.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tao.authenticationservice.entities.AppUser;
import fr.tao.authenticationservice.model.RoleUserForm;
import fr.tao.authenticationservice.service.UserService;

@RestController
public class UserRestController {
	
	private UserService userService;
	
	public UserRestController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping(path = "/users/{id}")
	@PostAuthorize("hasAuthority('USER')")
	public AppUser findById(@PathVariable(name = "id") Long id) {
		return userService.findUserById(id);		
	}
	
	@GetMapping(path = "/users")
	@PostAuthorize("hasAuthority('USER')")
	public List<AppUser> users() {
		return userService.listUsers();		
	}
	
	@PostMapping(path = "/users")
	@Secured("ADMIN")
	public AppUser saveUser(@RequestBody AppUser user) {
		return userService.addUser(user);
	}
	
	
	@PutMapping("/users/{id}")
	@Secured("ADMIN")
	public AppUser saveOrUpdate(@PathVariable(name = "id") Long id, @RequestBody AppUser user) {
		user.setId(id);
		return userService.saveOrUpdate(user);
	}
	
	/*
	@PutMapping(value = "/users/{id}") 
	@PostAuthorize("hasAuthority('ADMIN')")
	public AppUser updateUser(@PathVariable(name = "id") Long id, @RequestBody AppUser user) {
		user.setId(id);
		return userService.updateUser(user);
	}
	*/
	
	@DeleteMapping(value = "/users/{id}") 
	@PostAuthorize("hasAuthority('ADMIN')")
	public void deleteUser(@PathVariable(name = "id") Long id) {
		userService.deleteUser(id);
	}
	
	@PostMapping(path = "/addRoleToUser")
	@PostAuthorize("hasAuthority('ADMIN')")
	public void addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
		userService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRoleName());
	}
	
	@GetMapping(path = "/profile")  
	public AppUser profile(Principal principal) {
		return userService.findUserByUsername(principal.getName());
	}
}
