package ru.netcracker.chinagram.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.chinagram.model.Comment;
import ru.netcracker.chinagram.model.Photo;
import ru.netcracker.chinagram.model.User;
import ru.netcracker.chinagram.services.interfaces.ChinaDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins="http://localhost:4200")
public class CommentController {

    private static final Logger log = Logger.getLogger(CommentController.class);

    @Autowired
    private ChinaDAO chinaDAO;

    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getComment(@PathVariable String commentId) { //working
        Comment comment = chinaDAO.get(Comment.class, UUID.fromString(commentId));
        if (comment != null) {
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{photoId}/{userId}")
    public ResponseEntity<Comment> createCommentByIdPhotoUser(@PathVariable String photoId, @PathVariable String userId, @RequestBody String content) { //working
        Photo photo = chinaDAO.get(Photo.class, UUID.fromString(photoId));
        User user = chinaDAO.get(User.class, UUID.fromString(userId));
        if (photo != null && user != null) {
            Comment comment = new Comment(content, photo, user);
            log.info(" Сomment created:\n{\nuser_id: " +user.getId() +"\nuser_name: "+user.getUsername()+"\nPhoto_id: "+
                    photo.getId()+"\nComment_id: "+comment.getId()+"\nContent: "+comment.getContent()+"\n}\n\n");
            chinaDAO.persist(comment);
            return new ResponseEntity<>(comment, HttpStatus.CREATED);
        } else {
            log.error("Can not create comment\n{\nuser_id: " +user.getId() +"\nuser_name: "+user.getUsername()+"\nPhoto_id: "+
                photo.getId()+"\n}\n\n");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{photoId}/{userId}")
    public ResponseEntity removeCommentByIdPhotoUser(@PathVariable String photoId, @PathVariable String userId) {
        Photo photo = chinaDAO.get(Photo.class, UUID.fromString(photoId));
        User user = chinaDAO.get(User.class, UUID.fromString(userId));
        if (photo != null && user != null) {
            for (int i = 0; i < photo.getComments().size(); ++i) {
                if (photo.getComments().get(i).getUser().getId() == user.getId()) {
                    log.info(" Сomment deleted:\n{\nuser_id: " +user.getId() +"\nuser_name: "+user.getUsername()+"\nPhoto_id: "+
                            photo.getId()+"\nComment_id: "+photo.getComments().get(i).getId()+"\nContent: "+photo.getComments().get(i).getContent()+"\n}\n\n");
                    chinaDAO.remove(photo.getComments().get(i));
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        log.error("Can not delete comment\n{\nuser_id: " +user.getId() +"\nuser_name: "+user.getUsername()+"\nPhoto_id: "+
                photo.getId()+"\n}\n\n");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/photo/users/{photoId}")
    public ResponseEntity<List<Comment>> getListCommentByPhotoId(@PathVariable String photoId) { //working
        Photo photo = chinaDAO.get(Photo.class, UUID.fromString(photoId));
        if (photo != null) {
            return new ResponseEntity<>(photo.getComments(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{commentId}")
    public ResponseEntity<User> getUserByCommentId(@PathVariable String commentId) { //working
        Comment comment = chinaDAO.get(Comment.class, UUID.fromString(commentId));
        if (comment != null) {
            User user = comment.getUser();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/count/{photoId}")
    public ResponseEntity<Integer> getAmountOfCommentsByPhotoId(@PathVariable String photoId) { //working
        Photo photo = chinaDAO.get(Photo.class, UUID.fromString(photoId));
        if (photo != null) {
            return new ResponseEntity<>(photo.getComments().size(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateCommentById(@PathVariable String commentId, @RequestBody String updateContent) { //working
        Comment comment = chinaDAO.get(Comment.class, UUID.fromString(commentId));
        if (comment != null) {
            comment.setContent(updateContent);
            log.info(" Сomment updated:\n{\nuser_id: " +comment.getUser().getId() +"\nuser_name: "+comment.getUser().getUsername()+"\nPhoto_id: "+
                    comment.getPhoto().getId()+"\nComment_id: "+comment.getId()+"\nContent: "+comment.getContent()+"\n}\n\n");
            chinaDAO.merge(comment);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } else {
            log.error("Can not update comment with comment_id: "+commentId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Comment> removeCommentById(@PathVariable String commentId) {
        Comment comment = chinaDAO.get(Comment.class, UUID.fromString(commentId));
        if (comment != null) {
            log.info(" Сomment deleted:\n{\nuser_id: " +comment.getUser().getId() +"\nuser_name: "+comment.getUser().getUsername()+"\nPhoto_id: "+
                    comment.getPhoto().getId()+"\nComment_id: "+comment.getId()+"\nContent: "+comment.getContent()+"\n}\n\n");
            chinaDAO.remove(comment);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("Can not delete comment with comment_id: " + commentId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/photo/users/{photoID}")
    public ResponseEntity<ArrayList<User>> getListCommentUsersByPhotoID(@PathVariable String photoID) { //working
        Photo photo = chinaDAO.get(Photo.class, UUID.fromString(photoID));
        if (photo != null) {
            ArrayList<User> users = new ArrayList<>();
            for (int i = 0; i < photo.getComments().size(); ++i) {
                users.add(photo.getComments().get(i).getUser());
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}




