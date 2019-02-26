package com.workmate.server.repository;

import com.workmate.server.domain.User;

public interface IUserDAO
{
    User getActiveUser(String userName);
}
