package com.example.taskira.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.taskira.dto.in.TaskCreateParam;
import com.example.taskira.dto.in.TaskUpdateParam;
import com.example.taskira.dto.out.PageResponse;
import com.example.taskira.dto.out.TaskResponse;
import com.example.taskira.repository.entity.TaskEntity;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.ERROR)
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "completed", ignore = true)
    TaskEntity toEntity(TaskCreateParam param);

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget TaskEntity task, TaskUpdateParam param);

    TaskResponse toTaskResponse(TaskEntity entity);

    PageResponse<TaskResponse> toPageResponse(Page<TaskEntity> page);

}