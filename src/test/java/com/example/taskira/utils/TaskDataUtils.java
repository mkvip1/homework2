package com.example.taskira.utils;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.example.taskira.dto.in.PageRequestParam;
import com.example.taskira.dto.in.TaskCreateParam;
import com.example.taskira.repository.entity.TaskEntity;

public class TaskDataUtils {

    private TaskDataUtils() {
    }

    public static TaskCreateParam testTaskCreateParam() {
        return createTaskCreateParam("title1", "description1", Instant.parse("2024-02-18T18:35:24.00Z"));
    }

    public static TaskCreateParam createTaskCreateParam(String title, String description, Instant dueDate) {
        var taskCreateParam = new TaskCreateParam();
        taskCreateParam.setTitle(title);
        taskCreateParam.setDescription(description);
        taskCreateParam.setDueDate(dueDate);
        return taskCreateParam;
    }

    public static Page<TaskEntity> testPage() {
        return new PageImpl<>(testList(), testPageRequest(), 100);
    }

    public static List<TaskEntity> testList() {
        var task1 = createTaskEntity(1L, "title1", "description1", "2024-01-30T18:35:24.00Z", true);
        var task2 = createTaskEntity(2L, "title2", "description2", "2024-02-18T18:35:24.00Z", false);
        var task3 = createTaskEntity(3L, "title3", "description3", "2024-03-30T18:35:24.00Z", false);
        return List.of(task1, task2, task3);
    }

    public static TaskEntity testTaskEntity() {
        return createTaskEntity(3L, "title3", "description3", "2024-03-30T18:35:24.00Z", false);
    }

    public static TaskEntity createTaskEntity(Long id, String title, String description,
            String dueDate, boolean completed) {
        var e = new TaskEntity();
        e.setId(id);
        e.setTitle(title);
        e.setDescription(description);
        e.setDueDate(Instant.parse(dueDate));
        e.setCompleted(completed);
        return e;
    }

    public static PageRequest testPageRequest() {
        return PageRequest.of(0, 10, Sort.Direction.ASC, "title");
    }

    public static PageRequestParam testParam() {
        return createPageRequestParam(0, 10, "title", Sort.Direction.ASC);
    }

    public static PageRequestParam createPageRequestParam(int page, int size, String property, Sort.Direction direction) {
        var p = new PageRequestParam();
        p.setPage(page);
        p.setSize(size);
        p.setProperty(property);
        p.setDirection(direction);
        return p;
    }

}
