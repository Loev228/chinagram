package ru.netcracker.chinagram.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "china_user")
@NoArgsConstructor
public class User extends AbstractEntity {

    public User(String username, String password, String info) {
        this.username = username;
        this.password = password;
        this.information = info;
    }

    @Column(unique = true)
    @NotNull
    private String username;

    @NotNull
    private String password;

    private String information;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    List<Photo> photos = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> followers = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinTable(name = "china_user_followers",
            uniqueConstraints = @UniqueConstraint(columnNames = {"followers_id", "user_id"}),
            joinColumns = @JoinColumn(name = "followers_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> following = new ArrayList<>();


    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    List<Like> likes = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    List<Comment> comments = new ArrayList<>();


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public List<User> getFollowers() {
        return followers;
    }


    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    @JsonIgnore
    public List<User> getFollowing() {
        return following;
    }


    public void setFollowing(List<User> following) {
        this.following = following;
    }


    public String getInformation() {
        return information;
    } //@JsonIgnore здесь не нужен (наверное)

    public void setInformation(String information) {
        this.information = information;
    }

    @JsonIgnore
    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }


}
