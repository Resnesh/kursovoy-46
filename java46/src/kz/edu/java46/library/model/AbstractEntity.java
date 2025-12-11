package kz.edu.java46.library.model;

import java.io.Serializable;

public abstract class AbstractEntity implements Serializable {
    private Long id;
    private static long ID_COUNTER = 0L;

    public AbstractEntity() {
        this.id = ++ID_COUNTER;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        if (id > ID_COUNTER) {
            ID_COUNTER = id;
        }
    }

}