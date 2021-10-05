package ru.netcracker.chinagram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.chinagram.exceptions.Errors;
import ru.netcracker.chinagram.model.Photo;
import ru.netcracker.chinagram.model.User;
import ru.netcracker.chinagram.services.interfaces.ChinaDAO;
import ru.netcracker.chinagram.services.interfaces.FeedService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/feed")
@CrossOrigin
public class FeedController {

    @Autowired
    private ChinaDAO chinaDAO;

    @Autowired
    private FeedService feedService;

    @GetMapping("/{userId}")
    public ResponseEntity getFeed(Pageable pageable, @PathVariable @NotNull String userId) {
        User user = chinaDAO.get(User.class, UUID.fromString(userId));

        if (user != null) {
            return new ResponseEntity<>(
                    feedService.getPhotoList(user, pageable),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format(Errors.USER_WITH_ID_NOT_FOUND, userId), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * тестовые контроллеры
     **/

    @GetMapping("/test")
    public ResponseEntity getFeedListTest() {
        List<Photo> photos = chinaDAO.findAll(Photo.class);
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }

    @GetMapping("/test/pageable")
    public ResponseEntity getFeedListTestWithPaging(Pageable pageable) {
        List<Photo> photos = chinaDAO.findAll(Photo.class, pageable);
        return new ResponseEntity<>(photos, HttpStatus.OK);
    }

}
