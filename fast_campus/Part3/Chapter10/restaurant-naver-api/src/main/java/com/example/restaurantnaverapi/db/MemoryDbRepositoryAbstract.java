package com.example.restaurantnaverapi.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

abstract public class MemoryDbRepositoryAbstract<T extends MemoryDbEntity>
        implements MemoryDbRepositoryIfs<T> {

    private final List<T> db = new ArrayList<>();
    private int index = 0;


    @Override
    public Optional<T> findById(int index) {
        return db.stream().filter(it -> it.getIndex() == index).findFirst();
    }

    @Override
    public T save(T entity) {

        var optionalEntity = db.stream()
                .filter(it -> it.getIndex() == entity.getIndex()).findFirst();

        // case 1. db에 데이터가 없는 경우
        if (optionalEntity.isEmpty()) {
            index++;
            entity.setIndex(index);
            db.add(entity);

            return entity;
        }
        // case 2. db에 이미 데이터가 있는 경우
        else {
            var preIndex = optionalEntity.get().getIndex();
            entity.setIndex(preIndex);
            deleteById(preIndex);
            db.add(entity);

            return entity;
        }

    }

    @Override
    public void deleteById(int index) {

        var optionalEntity = db.stream()
                .filter(it -> it.getIndex() == index).findFirst();

        if (optionalEntity.isPresent()) {
            db.remove(optionalEntity.get());
        }
    }

    @Override
    public List<T> findAll() {
        return db;
    }
}
