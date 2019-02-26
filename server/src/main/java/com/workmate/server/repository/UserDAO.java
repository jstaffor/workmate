package com.workmate.server.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.workmate.server.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserDAO implements IUserDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    public User getActiveUser(String userName)
    {
        User activeUserInfo = new User();
        short enabled = 1;
        List<?> list = entityManager.createQuery("SELECT u FROM User u WHERE userName=? and enabled=?")
                .setParameter(1, userName).setParameter(2, enabled).getResultList();
        if(!list.isEmpty()) {
            activeUserInfo = (User)list.get(0);
        }
        return activeUserInfo;
    }
}
