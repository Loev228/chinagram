package ru.netcracker.chinagram.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.netcracker.chinagram.model.Photo;
import ru.netcracker.chinagram.model.User;
import ru.netcracker.chinagram.services.interfaces.FeedService;

import java.util.List;

@Service
public class FeedServiceImpl  implements FeedService {

@Autowired
private ChinaDAOImpl chinaDAO;

  public List<Photo> getPhotoList(User user, Pageable pageable){
        if (user!=null && user.getId()!=null) {
            List photos =
                    chinaDAO.executeSqlQuery("select * from photo where photo.user_id in" +
                                    " (select user_id from china_user_followers where followers_id = '%s')",
                            user.getId().toString().replaceAll("-", ""), Photo.class, pageable);
            return photos;
        }
        else return null;
  }

}
