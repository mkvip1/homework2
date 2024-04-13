package com.example.taskira.dto.out;

import java.util.List;

import com.example.taskira.dto.in.PageRequestParam;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Страница")
public class PageResponse<T> {

    @NotNull
    @Schema(description = "Контент")
    private List<T> content;

    @NotNull
    @Schema(description = "Общее кол-во страниц")
    private long totalPages;

    @NotNull
    @Schema(description = "Общее кол-во элементов")
    private long totalElements;

    @NotNull
    @Schema(description = "Номер страницы")
    private long number;

}
