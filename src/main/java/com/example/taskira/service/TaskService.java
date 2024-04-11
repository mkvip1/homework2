package com.example.taskira.service;

import org.springframework.data.domain.Page;

import com.example.taskira.dto.in.PageRequestParam;
import com.example.taskira.dto.in.TaskCreateParam;
import com.example.taskira.dto.in.TaskUpdateParam;
import com.example.taskira.repository.entity.TaskEntity;

import jakarta.validation.constraints.NotNull;

public interface TaskService {

    @NotNull
    Page<TaskEntity> findTasks(@NotNull PageRequestParam param);

    @NotNull
    TaskEntity create(@NotNull TaskCreateParam param);

    @NotNull
    TaskEntity findById(@NotNull Long id);

    @NotNull
    TaskEntity update(@NotNull Long id, @NotNull TaskUpdateParam param);

    void delete(@NotNull Long id);

}