package com.example.taskira.dto.in;

import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Параметры для пагинации")
public class PageRequestParam {

    @Schema(description = "Номер страницы")
    private int page = 0;

    @Schema(description = "Размер")
    private int size = 10;

    @Schema(description = "Поле для сортировки")
    private String property = "id";

    @Schema(description = "Направление сортировки")
    private Sort.Direction direction = Sort.Direction.ASC;
}
