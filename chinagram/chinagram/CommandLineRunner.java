package ru.netcracker.chinagram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.netcracker.chinagram.model.Like;
import ru.netcracker.chinagram.model.Comment;
import ru.netcracker.chinagram.model.Photo;
import ru.netcracker.chinagram.model.User;
import ru.netcracker.chinagram.services.interfaces.ChinaDAO;

import java.util.Random;

@Component
public class CommandLineRunner implements org.springframework.boot.CommandLineRunner {

    @Autowired
    ChinaDAO chinaDAO;

    @Override
    public void run(String... arg0) {/**
       String[] array = {"Vasia_", "Ivan_", "Misha_", "Petr_", "Innokentiy_"};
        Random r = new Random();

        User user1 = new User(array[r.nextInt(3)] + r.nextInt(1000),
                "123", "информация");

        User user2 = new User(array[r.nextInt(3)] + r.nextInt(1000),
                "123", "информация");

        Photo photo = new Photo();
        photo.setUser(user1);
        photo.setImage("dsd");

        chinaDAO.persist(user1);
        chinaDAO.persist(user2);
        chinaDAO.merge(photo);

        Like like = new Like(photo, user2);
        chinaDAO.merge(like);

        user1.getFollowers().add(user2);
        chinaDAO.merge(user1);

        Comment comment = new Comment("fdfd", photo, user2);
        photo.getComments().add(comment);
        chinaDAO.merge(comment);

**/

    }

}
