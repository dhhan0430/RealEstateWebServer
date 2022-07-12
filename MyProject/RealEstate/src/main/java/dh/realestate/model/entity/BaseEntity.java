package dh.realestate.model.entity;

import dh.realestate.model.entity.listener.Auditable;
import dh.realestate.model.entity.listener.BaseEntityListener;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
//@EntityListeners(value = AuditingEntityListener.class)
@EntityListeners(value = BaseEntityListener.class)
public class BaseEntity implements Auditable {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
