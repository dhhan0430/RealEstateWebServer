package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoResponse {
    private Long id;
    private String title;
    private Long order;
    private Boolean completed;
    private String url;

    public ToDoResponse(ToDoModel toDoModel) {
        this.id = toDoModel.getId();
        this.title = toDoModel.getTitle();
        this.order = toDoModel.getOrder();
        this.completed = toDoModel.getCompleted();

        this.url = "http://localhost:8080/" + this.id;
    }
}
