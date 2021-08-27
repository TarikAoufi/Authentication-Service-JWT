package fr.tao.authenticationservice.service;

import java.util.List;

import fr.tao.authenticationservice.entities.AppUser;

public interface UserService {
	
	public AppUser addUser(AppUser user);
	public AppUser findUserById(Long id);
	public AppUser findUserByUsername(String username);
	public AppUser saveOrUpdate(AppUser user);
 	public AppUser updateUser(AppUser user);
	public void deleteUser(Long id);
	public void addRoleToUser(String username, String roleName);	
	public List<AppUser> listUsers();
	
}
