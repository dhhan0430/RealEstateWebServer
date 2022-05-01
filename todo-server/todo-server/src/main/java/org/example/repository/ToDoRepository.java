package org.example.repository;

import org.example.model.ToDoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// DB에는 ToDoEntity만 저장할 것이다. ToDoRequest랑 ToDoResponse는 저장할 필요 없다.
@Repository
public interface ToDoRepository extends JpaRepository<ToDoModel, Long> {
}
