package fr.tao.authenticationservice.service;

import java.util.List;

import fr.tao.authenticationservice.entities.Role;

public interface RoleService {
	
	public Role addRole(Role role);
	public List<Role> listRoles();

}
