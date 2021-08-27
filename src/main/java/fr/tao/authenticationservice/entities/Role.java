package fr.tao.authenticationservice.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
@Entity
public class Role {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String roleName;
	
	public Role() {
		this.roleName = RoleName.USER.getRole();
	}
}
