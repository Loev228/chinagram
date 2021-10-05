package ru.netcracker.chinagram.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "china_like")
@AllArgsConstructor
@NoArgsConstructor
public class Like extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private Photo photo;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;


    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
