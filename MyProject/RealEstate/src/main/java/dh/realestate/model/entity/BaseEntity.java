package dh.realestate.model.entity;

import dh.realestate.model.entity.listener.Auditable;
import dh.realestate.model.entity.listener.BaseEntityListener;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@MappedSuperclass
//@EntityListeners(value = AuditingEntityListener.class)
@EntityListeners(value = BaseEntityListener.class)
public abstract class BaseEntity implements Auditable {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public abstract Long getId();
}
