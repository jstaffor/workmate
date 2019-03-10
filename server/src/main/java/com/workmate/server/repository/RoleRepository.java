package com.workmate.server.repository;

import com.workmate.server.model.dao.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Long>
{
    Role findByRole(String role);
}