package dh.realestate.model.entity.idgenerator;

import dh.realestate.model.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

public class IdGenerator<T extends BaseEntity, ID> {
    private JpaRepository<T, ID> jpaRepository;

    public Long generateId() {
        Long id = 0L;
        List<T> entityList = jpaRepository.findAll();

        if (entityList != null) {
            Iterator iter = entityList.iterator();
            while (iter.hasNext()) {
                T entity = (T) iter.next();
                if (entity.getId() != id + 1) {
                    break;
                }
                id = id + 1;
            }
        }

        return id + 1;
    }

    public void setJpaRepository(JpaRepository<T, ID> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
}
