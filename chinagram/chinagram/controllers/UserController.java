package ru.netcracker.chinagram.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.chinagram.exceptions.Errors;
import ru.netcracker.chinagram.model.User;
import ru.netcracker.chinagram.services.interfaces.ChinaDAO;
import ru.netcracker.chinagram.services.interfaces.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class UserController {

    private static final Logger log = Logger.getLogger(UserController.class);

    @Autowired
    private ChinaDAO chinaDAO;

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody User user) {
        if (userService.isValidUser(user)) {
            log.info(" User created:\n{\nuser_id: " +user.getId() + "\nuser_date: " +user.getDate()+"\nuser_name: "+user.getUsername()+"\n}\n\n");
            chinaDAO.persist(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } else {
            log.error(String.format(Errors.USER_IS_NOT_VALID, user.getId(), user.getUsername()));
            return new ResponseEntity<>(String.format(Errors.USER_IS_NOT_VALID, user.getId(), user.getUsername()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/users")
    public ResponseEntity updateUser(@RequestBody User user) {
        if (userService.isValidUser(user)) {
            log.info(" User updated:\n{\nuser_id: " +user.getId() + "\nuser_date: " +user.getDate()+"\nuser_name: "+user.getUsername()+"\n}\n\n");
            chinaDAO.merge(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            log.error("Can not update user: "+String.format(Errors.USER_IS_NOT_VALID, user.getId(), user.getUsername()));
            return new ResponseEntity<>(String.format(Errors.USER_IS_NOT_VALID, user.getId(), user.getUsername()),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(path = "/users/{userId}")
    public ResponseEntity getUserById(@PathVariable String userId) {
        User user = chinaDAO.get(User.class, UUID.fromString(userId));
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format(Errors.USER_WITH_ID_NOT_FOUND, userId),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/users/by_username/{username}")
    public ResponseEntity getUserByName(@PathVariable String username) {
        User user = chinaDAO.get(User.class, "username", username);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format(Errors.USER_WITH_NAME_NOT_FOUND, username),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/users_list/by_username/{username}")
    public ResponseEntity getUserListByName(@PathVariable String username) {
        List<User> users = chinaDAO.findAllByFieldLike(User.class, "username", username);
        return new ResponseEntity<>(users, HttpStatus.OK);

    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity removeUserById(@PathVariable String userId) {
        User user = chinaDAO.get(User.class, UUID.fromString(userId));
        if (user != null) {
            log.info(" User deleted:\n{\nuser_id: " +user.getId() + "\nuser_date: " +user.getDate()+"\nuser_name: "+user.getUsername()+"\n}\n\n");
            chinaDAO.remove(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("Can not delete user"+(String.format(Errors.USER_WITH_ID_NOT_FOUND, userId)));
            return new ResponseEntity<>(String.format(Errors.USER_WITH_ID_NOT_FOUND, userId), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/followers/{followerId}/{followingId}")
    public ResponseEntity followUser(@PathVariable String followerId, @PathVariable String followingId) {
        User followerUser = chinaDAO.get(User.class, UUID.fromString(followerId));
        User followingUser = chinaDAO.get(User.class, UUID.fromString(followingId));
        if (followerUser != null) {

            if (followingUser != null) {
                if (!userService.getFollowingUser(followerUser, followingId).isPresent()) {
                    followerUser.getFollowing().add(followingUser);
                    chinaDAO.merge(followerUser);
                    ResponseEntity<List> responseEntity =
                            new ResponseEntity(followerUser.getFollowing(), HttpStatus.OK);
                    return responseEntity;

                } else {
                    return new ResponseEntity<>(String.format(Errors.USER_ALREADY_FOLLOWS_USER_WITH_ID, followerId, followingId), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(String.format(Errors.USER_WITH_ID_NOT_FOUND, followingId),
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(String.format(Errors.USER_WITH_ID_NOT_FOUND, followerId),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/followers/{followerId}/{followingId}")
    public ResponseEntity unfollowUser(@PathVariable String followerId, @PathVariable String followingId) {
        User followerUser = chinaDAO.get(User.class, UUID.fromString(followerId));
        if (followerUser != null) {
            Optional<User> optionalFollowingUser = userService.getFollowingUser(followerUser, followingId);

            if (optionalFollowingUser.isPresent()) {

                User followingUser = optionalFollowingUser.get();
                followerUser.getFollowing().remove(followingUser);
                chinaDAO.merge(followerUser);
                ResponseEntity<List<User>> responseEntity =
                        new ResponseEntity<>(followerUser.getFollowing(), HttpStatus.OK);
                return responseEntity;

            } else {
                return new ResponseEntity<>(String.format(Errors.USER_DOESNT_FOLLOW_USER_WITH_ID,
                        followerId, followingId),
                        HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity<>(String.format(Errors.USER_WITH_ID_NOT_FOUND, followerId),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity getFollowers(@PathVariable String userId) {
        User user = chinaDAO.get(User.class, UUID.fromString(userId));
        if (user != null) {
            return new ResponseEntity<>(user.getFollowers(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format(Errors.USER_WITH_ID_NOT_FOUND, userId),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{followerId}/{followingId}")
    public ResponseEntity isUserFollowing(@PathVariable String followerId, @PathVariable String followingId) {
        User followerUser = chinaDAO.get(User.class, UUID.fromString(followerId));
        User followingUser = chinaDAO.get(User.class, UUID.fromString(followingId));
        if (followerUser != null) {
            if (followingUser != null) {
                Optional<User> optionalUser = userService.getFollowingUser(followerUser, followingId);
                return new ResponseEntity<>(optionalUser.isPresent(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(String.format(Errors.USER_WITH_ID_NOT_FOUND, followingId),
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(String.format(Errors.USER_WITH_ID_NOT_FOUND, followerId),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/followings/{userId}")
    public ResponseEntity getFollowing(@PathVariable String userId) {
        User user = chinaDAO.get(User.class, UUID.fromString(userId));
        if (user != null) {
            return new ResponseEntity<>(user.getFollowing(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format(Errors.USER_WITH_ID_NOT_FOUND, userId),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/followers_amount/{userId}")
    public ResponseEntity getAmountOFFollowers(@PathVariable String userId) {
        User user = chinaDAO.get(User.class, UUID.fromString(userId));
        if (user != null) {
            return new ResponseEntity<>(user.getFollowers().size(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format(Errors.USER_WITH_ID_NOT_FOUND, userId),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users/followings_amount/{userId}")
    public ResponseEntity getAmountOfFollowings(@PathVariable String userId) {
        User user = chinaDAO.get(User.class, UUID.fromString(userId));
        if (user != null) {
            return new ResponseEntity<>(user.getFollowing().size(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format(Errors.USER_WITH_ID_NOT_FOUND, userId),
                    HttpStatus.BAD_REQUEST);
        }
    }


}
