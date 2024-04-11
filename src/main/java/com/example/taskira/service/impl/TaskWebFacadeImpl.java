package com.example.taskira.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.example.taskira.dto.in.PageRequestParam;
import com.example.taskira.dto.in.TaskCreateParam;
import com.example.taskira.dto.in.TaskUpdateParam;
import com.example.taskira.dto.out.PageResponse;
import com.example.taskira.dto.out.TaskResponse;
import com.example.taskira.mapper.TaskMapper;
import com.example.taskira.service.TaskService;
import com.example.taskira.service.TaskWebFacade;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskWebFacadeImpl implements TaskWebFacade {

    private final TaskService taskService;

    @NotNull
    @Override
    public PageResponse<TaskResponse> findTasks(@NotNull PageRequestParam param) {
        var page = taskService.findTasks(param);
        return TaskMapper.INSTANCE.toPageResponse(page);
    }

    @NotNull
    @Override
    @Transactional
    public TaskResponse create(@NotNull TaskCreateParam param) {
        var createdTask = taskService.create(param);
        return TaskMapper.INSTANCE.toTaskResponse(createdTask);
    }

    @NotNull
    @Override
    public TaskResponse findById(@NotNull Long id) {
        var task = taskService.findById(id);
        return TaskMapper.INSTANCE.toTaskResponse(task);
    }

    @NotNull
    @Override
    @Transactional
    public TaskResponse update(@NotNull Long id, @NotNull TaskUpdateParam param) {
        var updatedTask = taskService.update(id, param);
        return TaskMapper.INSTANCE.toTaskResponse(updatedTask);
    }

    @Override
    @Transactional
    public void delete(@NotNull Long id) {
        taskService.delete(id);
    }

}