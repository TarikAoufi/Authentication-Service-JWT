package fr.tao.authenticationservice.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tao.authenticationservice.entities.Role;
import fr.tao.authenticationservice.repository.RoleRepository;
import fr.tao.authenticationservice.service.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	
	private RoleRepository roleRepository;
	
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Role addRole(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public List<Role> listRoles() {
		return roleRepository.findAll();
	}

}
