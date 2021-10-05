package ru.netcracker.chinagram.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.chinagram.model.User;
import ru.netcracker.chinagram.services.interfaces.ChinaDAO;
import ru.netcracker.chinagram.services.interfaces.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ChinaDAO chinaDAO;

    public boolean isValidUser(User user) {
        return user != null && chinaDAO.get(User.class, user.getId()) == null
                && chinaDAO.get(User.class, "username", user.getUsername()) == null;

    }

    public Optional<User> getFollowingUser(User user, String followingId) {
        return user.getFollowing().stream().filter(e -> e.getId().toString()
                .equals(followingId)).findFirst();

    }
}
