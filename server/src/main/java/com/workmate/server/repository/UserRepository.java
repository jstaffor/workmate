package com.workmate.server.repository;

import com.workmate.server.model.dao.Company;
import com.workmate.server.model.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long>
{
    User findByEmail(String email);


    @Query(value = "SELECT u FROM User u WHERE u.company = ?1")
    List<User> getAllUsersForCompany(Company company);
}
