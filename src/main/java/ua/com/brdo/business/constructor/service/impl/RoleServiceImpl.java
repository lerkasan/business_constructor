package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import ua.com.brdo.business.constructor.service.NotFoundException;
import ua.com.brdo.business.constructor.model.Role;
import ua.com.brdo.business.constructor.repository.RoleRepository;
import ua.com.brdo.business.constructor.service.RoleService;

@Service("RoleService")
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepo;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Transactional
    @Override
    public Role create(final Role role) {
        Objects.requireNonNull(role);
        return roleRepo.saveAndFlush(role);
    }

    @Transactional
    @Override
    public Role update(final Role role) {
        Objects.requireNonNull(role);
        return roleRepo.saveAndFlush(role);
    }

    @Transactional
    @Override
    public void delete(final long id) {
        roleRepo.delete(id);
    }

    @Override
    public Role findById(final long id) {
        return roleRepo.findOne(id);
    }

    @Override
    public Role findByTitle(final String title) {
        return roleRepo.findByTitle(title).orElseThrow(() -> new NotFoundException("Role with given title was not found."));
    }

    @Override
    public List<Role> findAll() {
        return roleRepo.findAll();
    }
}


