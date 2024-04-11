package com.example.taskira.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskira.dto.in.PageRequestParam;
import com.example.taskira.dto.in.TaskCreateParam;
import com.example.taskira.dto.in.TaskUpdateParam;
import com.example.taskira.dto.out.PageResponse;
import com.example.taskira.dto.out.TaskResponse;
import com.example.taskira.service.TaskWebFacade;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Tag(name = "TaskController", description = "Сервис для работы с Задачами")
@RestController
@Validated
@RequestMapping(value = "tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskWebFacade webFacade;

    @NotNull
    @Operation(summary = "Получить список Задач")
    @GetMapping
    public PageResponse<TaskResponse> findTasks(@ParameterObject PageRequestParam param) {
        return webFacade.findTasks(param);
    }

    @NotNull
    @Operation(summary = "Создать Задачу")
    @PostMapping
    public TaskResponse createTask(@Valid @NotNull @RequestBody TaskCreateParam param) {
        return webFacade.create(param);
    }

    @NotNull
    @Operation(summary = "Найти Задачу")
    @GetMapping("/{id}")
    public TaskResponse findById(@NotNull @PathVariable("id") Long id) {
        return webFacade.findById(id);
    }

    @NotNull
    @Operation(summary = "Обновить Задачу")
    @PutMapping("/{id}")
    public TaskResponse updateTask(@NotNull @PathVariable("id") Long id,
            @Valid @NotNull @RequestBody TaskUpdateParam param) {
        return webFacade.update(id, param);
    }

    @Operation(summary = "Удалить Задачу")
    @DeleteMapping("/{id}")
    public void deleteTask(@NotNull @PathVariable("id") Long id) {
        webFacade.delete(id);
    }

}