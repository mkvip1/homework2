package com.example.taskira.dto.out;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Задача")
public class TaskResponse {

    @NotNull
    @Schema(description = "Идентификатор")
    private Long id;

    @NotNull
    @Schema(description = "Заголовок")
    private String title;

    @NotNull
    @Schema(description = "Описание")
    private String description;

    @NotNull
    @Schema(description = "Дата завершения")
    private Instant dueDate;

    @NotNull
    @Schema(description = "Завершена")
    private boolean completed;

}