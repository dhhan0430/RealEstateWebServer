package dh.realestate.model.entity.listener;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class BaseEntityListener {
    @PrePersist
    public void prePersist(Object obj) {
        if (obj instanceof Auditable) {
            ((Auditable)obj).setCreatedAt((LocalDateTime.now()));
            ((Auditable)obj).setUpdatedAt((LocalDateTime.now()));
        }
    }

    @PreUpdate
    public void preUpdate(Object obj) {
        if (obj instanceof Auditable) {
            ((Auditable)obj).setUpdatedAt((LocalDateTime.now()));
        }
    }
}
