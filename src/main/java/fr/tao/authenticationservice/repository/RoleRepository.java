package fr.tao.authenticationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tao.authenticationservice.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	public Role findByRoleName(String roleName);

}
