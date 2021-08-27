package fr.tao.authenticationservice.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name="\"user\"")
public class AppUser {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // equiv => @JsonIgnore
	private String password;  
	@ManyToMany(fetch = FetchType.EAGER) // Dès qu'on charge un user on aura ses roles
	private Set<Role> roles = new HashSet<>();

}
