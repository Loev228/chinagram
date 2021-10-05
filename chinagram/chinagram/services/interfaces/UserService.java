package ru.netcracker.chinagram.services.interfaces;

import org.springframework.stereotype.Service;
import ru.netcracker.chinagram.model.User;

import java.util.Optional;

@Service
public interface UserService {

    boolean isValidUser(User user);

    Optional<User> getFollowingUser(User user, String followingId);
}
