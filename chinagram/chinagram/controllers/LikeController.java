package ru.netcracker.chinagram.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.chinagram.model.Like;
import ru.netcracker.chinagram.model.Photo;
import ru.netcracker.chinagram.model.User;
import ru.netcracker.chinagram.services.interfaces.ChinaDAO;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/likes")
@CrossOrigin(origins="http://localhost:4200")
public class LikeController {

    private static final Logger log = Logger.getLogger(LikeController.class);

    @Autowired
    private ChinaDAO chinaDAO;

    @DeleteMapping("/{photoId}/{userId}")
    public ResponseEntity removeLikeByIdPhotoUser(@PathVariable String photoId, @PathVariable String userId) {
        Photo photo = chinaDAO.get(Photo.class, UUID.fromString(photoId));
        User user = chinaDAO.get(User.class, UUID.fromString(userId));
        if (photo != null && user != null) {
            for (int i = 0; i < photo.getLikes().size(); ++i) {
                if (photo.getLikes().get(i).getUser().getId() == user.getId()) {
                    log.info(" Like deleted:\n{\nuser_id: " +user.getId() +"\nuser_name: "+user.getUsername()+"\nPhoto_id: "+
                            photo.getId()+"\n}\n\n");
                    chinaDAO.remove(photo.getLikes().get(i));
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        log.error(" Can not delete like:\n{\nuser_id: " +userId +"\nuser_name: "+"\nPhoto_id: "+
                photoId+  "\n}\n\n");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{photoId}/{userId}")
    public ResponseEntity<Like> createLikeByIdPhotoUser(@PathVariable String photoId, @PathVariable String userId) { //working
        Photo photo = chinaDAO.get(Photo.class, UUID.fromString(photoId));
        User user = chinaDAO.get(User.class, UUID.fromString(userId));
        if (photo != null && user != null) {
            Like like = new Like(photo, user);
            log.info(" Like created:\n{\nuser_id: " +user.getId() +"\nuser_name: "+user.getUsername()+"\nPhoto_id: "+
                    photo.getId()+"\nLike_id: "+like.getId()+"\n}\n\n");
            chinaDAO.persist(like);
            return new ResponseEntity<>(like, HttpStatus.CREATED);
        } else {
            log.error(" Can not create like:\n{\nuser_id: " +userId +"\nuser_name: "+"\nPhoto_id: "+
                    photoId+  "\n}\n\n");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/photo/users/{photoID}")
    public ResponseEntity<ArrayList<User>> getListLikeUsersByPhotoID(@PathVariable String photoID) { //working
        Photo photo = chinaDAO.get(Photo.class, UUID.fromString(photoID));
        if (photo != null) {
            ArrayList<User> users = new ArrayList<>();
            for (int i = 0; i < photo.getLikes().size(); ++i) {
                users.add(photo.getLikes().get(i).getUser());
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{likeId}")
    public ResponseEntity<Like> getLikeById(@PathVariable String likeId) { //working
        Like like = chinaDAO.get(Like.class, UUID.fromString(likeId));
        if (like != null) {
            return new ResponseEntity<>(like, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/count/{photoId}")
    public ResponseEntity<Integer> getAmountOfLikesByPhotoId(@PathVariable String photoId) { //working
        Photo photo = chinaDAO.get(Photo.class, UUID.fromString(photoId));
        if (photo != null) {
            return new ResponseEntity<>(photo.getLikes().size(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{likeId}")
    public ResponseEntity<User> getUserByLikeId(@PathVariable String likeId) { //working
        Like like = chinaDAO.get(Like.class, UUID.fromString(likeId));
        if (like != null) {
            User user = like.getUser();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<Like> removeLikeById(@PathVariable String likeId) {
        Like like = chinaDAO.get(Like.class, UUID.fromString(likeId));
        if (like != null) {
            log.info(" Like deleted:\n{\nuser_id: " +like.getUser().getId() +"\nuser_name: "+like.getUser().getUsername()+"\nPhoto_id: "+
                    like.getPhoto().getId()+"\nLike_id: "+like.getId()+"\n}\n\n");
            chinaDAO.remove(like);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("Can not delete like: " + likeId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/is_like/{userId}/{photoId}")
    public boolean userLiked(@PathVariable String userId, @PathVariable String photoId)
    {
        Photo photo = chinaDAO.get(Photo.class, UUID.fromString(photoId));
        User user = chinaDAO.get(User.class, UUID.fromString(userId));
        if (photo != null && user != null) {
            for (int i = 0; i < photo.getLikes().size(); ++i) {
                if (photo.getLikes().get(i).getUser().getId() == user.getId()) {
                    return true;
                }
            }
        }
        return false;
    }
}

