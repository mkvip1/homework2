package com.example.taskira.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Параметры для обновления Задачи")
public class TaskUpdateParam extends TaskCreateParam {

    @NotNull
    @Schema(description = "Завершена")
    private boolean completed;

}