package ru.netcracker.chinagram.services.interfaces;

import org.springframework.data.domain.Pageable;
import ru.netcracker.chinagram.model.Photo;
import ru.netcracker.chinagram.model.User;

import java.util.List;


public interface FeedService {

  List<Photo> getPhotoList(User user, Pageable pageable);

}
