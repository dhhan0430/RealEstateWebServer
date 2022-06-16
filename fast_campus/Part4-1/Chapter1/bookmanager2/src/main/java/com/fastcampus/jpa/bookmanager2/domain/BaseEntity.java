package com.fastcampus.jpa.bookmanager2.domain;

import com.fastcampus.jpa.bookmanager2.domain.listener.Auditable;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass // 해당 클래스의 필드를 상속받는 entity에 포함시켜주겠다
@EntityListeners(value = AuditingEntityListener.class)
public class BaseEntity implements Auditable {

    @CreatedDate
    // columnDefinition: auto ddl 할 때 주는 추가적인 속성 값.
    @Column(columnDefinition = "datetime(6) default now(6) comment '생성시간'",
            nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @LastModifiedDate
    @Column(columnDefinition = "datetime(6) default now(6) comment '수정시간'",
            nullable = false)
    private LocalDateTime updatedAt;
}
