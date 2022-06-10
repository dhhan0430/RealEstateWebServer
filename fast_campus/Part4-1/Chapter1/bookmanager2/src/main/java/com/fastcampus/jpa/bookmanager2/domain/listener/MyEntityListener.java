package com.fastcampus.jpa.bookmanager2.domain.listener;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class MyEntityListener {
    @PrePersist
    public void prePersist(Object obj) {
        if (obj instanceof Auditable) {
            ((Auditable) obj).setCreatedAt((LocalDateTime.now()));
            ((Auditable) obj).setUpdatedAt((LocalDateTime.now()));
        }
    }

    @PreUpdate
    public void preUpdate(Object obj) {
        if (obj instanceof Auditable) {
            ((Auditable) obj).setUpdatedAt((LocalDateTime.now()));
        }
    }
}
