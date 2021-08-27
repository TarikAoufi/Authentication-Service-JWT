package fr.tao.authenticationservice.controller;

import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tao.authenticationservice.entities.Role;
import fr.tao.authenticationservice.service.RoleService;

@RestController
public class RoleRestController {
	
	private RoleService roleService;

	public RoleRestController(RoleService roleService) {
		this.roleService = roleService;
	}
	
	@GetMapping(path = "/roles")
	@PostAuthorize("hasAuthority('USER')")
	public List<Role> roles() {
		return roleService.listRoles();		
	}
	
	@PostMapping(path = "/roles")
	@PostAuthorize("hasAuthority('ADMIN')")
	public Role saveRole(@RequestBody Role role) {
		return roleService.addRole(role);
	}
}
