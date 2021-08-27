package fr.tao.authenticationservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleName {
	
	USER("USER"),
	CUSTOMER_MANAGER("CUSTOMER_MANAGER"),
	PRODUCT_MANAGER("PRODUCT_MANAGER"),
	ORDER_MANAGER("ORDER_MANAGER"),
	ADMIN("ADMIN");
	
	String role;

}
