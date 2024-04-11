package com.example.taskira.service;

import com.example.taskira.dto.in.PageRequestParam;
import com.example.taskira.dto.in.TaskCreateParam;
import com.example.taskira.dto.in.TaskUpdateParam;
import com.example.taskira.dto.out.PageResponse;
import com.example.taskira.dto.out.TaskResponse;

import jakarta.validation.constraints.NotNull;

public interface TaskWebFacade {

    @NotNull
    PageResponse<TaskResponse> findTasks(@NotNull PageRequestParam param);

    @NotNull
    TaskResponse create(@NotNull TaskCreateParam param);

    @NotNull
    TaskResponse findById(@NotNull Long id);

    @NotNull
    TaskResponse update(@NotNull Long id, @NotNull TaskUpdateParam param);

    void delete(@NotNull Long id);

}