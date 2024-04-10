package com.example.taskira.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskira.repository.entity.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
