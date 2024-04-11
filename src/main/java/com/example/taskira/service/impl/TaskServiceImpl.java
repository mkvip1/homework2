package com.example.taskira.service.impl;

import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.example.taskira.dto.in.PageRequestParam;
import com.example.taskira.dto.in.TaskCreateParam;
import com.example.taskira.dto.in.TaskUpdateParam;
import com.example.taskira.exception.EntityNotFoundException;
import com.example.taskira.mapper.TaskMapper;
import com.example.taskira.repository.TaskRepository;
import com.example.taskira.repository.entity.TaskEntity;
import com.example.taskira.service.TaskService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @NotNull
    @Override
    public Page<TaskEntity> findTasks(@NotNull PageRequestParam param) {
        return taskRepository.findAll(PageRequest.of(param.getPage(), param.getSize(),
                param.getDirection(), param.getProperty()));
    }

    @NotNull
    @Override
    @Transactional
    public TaskEntity create(@NotNull TaskCreateParam param) {
        var newTask = TaskMapper.INSTANCE.toEntity(param);
        return taskRepository.save(newTask);
    }

    @NotNull
    @Override
    public TaskEntity findById(@NotNull Long id) {
        return taskRepository.findById(id)
                .orElseThrow(entityNotFoundExceptionSupplier(id));
    }

    @NotNull
    @Override
    @Transactional
    public TaskEntity update(@NotNull Long id, @NotNull TaskUpdateParam param) {
        var task = this.findById(id);
        TaskMapper.INSTANCE.update(task, param);
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void delete(@NotNull Long id) {
        var task = this.findById(id);
        taskRepository.delete(task);
    }

    private Supplier<EntityNotFoundException> entityNotFoundExceptionSupplier(Long id) {
        return () -> new EntityNotFoundException(String.format("Task with id = %s not found", id));
    }

}