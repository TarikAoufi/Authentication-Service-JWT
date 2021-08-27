package fr.tao.authenticationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tao.authenticationservice.entities.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long>{
	
	public AppUser findByUsername(String username);

}
