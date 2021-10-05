package ru.netcracker.chinagram.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.chinagram.model.Photo;
import ru.netcracker.chinagram.model.User;
import ru.netcracker.chinagram.services.interfaces.ChinaDAO;

import java.util.UUID;

@RestController
@RequestMapping("/photos")
@CrossOrigin(origins="http://localhost:4200")
public class PhotoController {

    private static final Logger log = Logger.getLogger(PhotoController.class);

    @Autowired
    private ChinaDAO chinaDAO;

    @GetMapping("/{photoId}")
    public ResponseEntity<Photo> getPhotoById(@PathVariable String photoId) { //working
        Photo photo = chinaDAO.get(Photo.class, UUID.fromString(photoId));
        if (photo != null) {
            return new ResponseEntity<>(photo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Photo> createPhotoByUserId(@PathVariable String userId, @RequestBody String image) { //working
        User user = chinaDAO.get(User.class, UUID.fromString(userId));
        if (user != null) {
            Photo photo = new Photo();
            photo.setUser(user);
            log.info(" Photo created:\n{\nuser_id: " +user.getId() + "\nuser_name: "+user.getUsername()+"\nphoto_id: "+photo.getId()+"}\n\n");
            chinaDAO.persist(photo);
            return new ResponseEntity<>(photo, HttpStatus.CREATED);
        } else {
            log.error("Can not create photo with user_id: " +user.getId() + "user_name: "+user.getUsername());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping()
    public ResponseEntity<Photo> createPhoto(@RequestBody Photo photo) { //working
      chinaDAO.persist(photo);
        return new ResponseEntity<>(photo, HttpStatus.CREATED);
    }

    @GetMapping("/user/{photoId}")
    public ResponseEntity<User> getUserByPhotoId(@PathVariable String photoId) { //working
        Photo photo = chinaDAO.get(Photo.class, UUID.fromString(photoId));
        if (photo != null) {
            User user = photo.getUser();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Photo> removePhotoById(@PathVariable String photoId) {
        Photo photo = chinaDAO.get(Photo.class, UUID.fromString(photoId));
        if (photo != null) {
            log.info(" Photo deleted:\n{\nuser_id: " +photo.getUser().getId() + "\nuser_name: "+photo.getUser().getUsername()+
                    "\nphoto_id: "+"\n}\n\n");
            chinaDAO.remove(photo);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("Can not delete photo with photo_id: "+photo.getId());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping()
    public ResponseEntity<Photo> updatePhoto(@RequestBody Photo photo) { //working
        if (photo != null) {
            log.info(" Photo updated:\n{\nuser_id: " +photo.getUser() +
                    "\nphoto_id: "+photo.getId()+"\n}\n\n");
            chinaDAO.merge(photo);
            return new ResponseEntity<>(photo, HttpStatus.OK);
        } else {
            log.error("Can not update photo with photo_id: "+photo.getId());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
