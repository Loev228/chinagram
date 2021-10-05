package ru.netcracker.chinagram.model;

import lombok.val;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractEntity {

    public AbstractEntity() {
        this.date = new Date();
    }

    @Id
    @Column(name = "id", length = 16, unique = true, nullable = false)
    protected UUID id =   UUID.randomUUID();

    @NotNull
    protected Date date;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
