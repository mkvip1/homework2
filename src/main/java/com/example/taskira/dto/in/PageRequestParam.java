package com.example.taskira.dto.in;

import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Параметры для пагинации")
public class PageRequestParam {

    @NotNull
    @PositiveOrZero
    @Schema(description = "Номер страницы")
    private int page;

    @NotNull
    @PositiveOrZero
    @Schema(description = "Размер")
    private int size;

    @NotBlank
    @Schema(description = "Поле для сортировки")
    private String property;

    @NotNull
    @Schema(description = "Направление сортировки")
    private Sort.Direction direction;
}
