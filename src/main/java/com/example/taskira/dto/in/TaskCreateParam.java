package com.example.taskira.dto.in;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Параметры для создания Задачи")
public class TaskCreateParam {

    @NotNull
    @Schema(description = "Заголовок")
    private String title;

    @NotNull
    @Schema(description = "Описание")
    private String description;

    @NotNull
    @Schema(description = "Дата завершения")
    private Instant dueDate;

}