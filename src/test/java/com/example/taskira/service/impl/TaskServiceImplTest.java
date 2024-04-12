package com.example.taskira.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.taskira.repository.TaskRepository;
import com.example.taskira.repository.entity.TaskEntity;
import com.example.taskira.service.TaskService;
import com.example.taskira.utils.TaskDataUtils;

class TaskServiceImplTest {

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    public TaskServiceImplTest() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskServiceImpl(taskRepository);
    }

    /**
     * Тест проверяет кол-во вызовов репозитория.
     */
    @Test
    void findTasks__test_repo_usage_count() {
        taskService.findTasks(TaskDataUtils.testParam());
        Mockito.verify(taskRepository, Mockito.times(1)).findAll(any(Pageable.class));
    }

    /**
     * Тест проверяет отсутствие мутаций у объектов, которые вернул репозиторий
     */
    @Test
    void findTasks__test_no_mutation_result_page() {
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(TaskDataUtils.testPage());

        final var actualPage = taskService.findTasks(TaskDataUtils.testParam());
        final var expectedPage = TaskDataUtils.testPage();

        assertEquals(expectedPage.getContent().size(), actualPage.getContent().size());
        for (var i = 0; i < actualPage.getContent().size(); i++) {
            final var actualEl = actualPage.getContent().get(i);
            final var expectedEl = expectedPage.getContent().get(i);
            assertEqualsTaskEntity(expectedEl, actualEl);
        }
    }

    /**
     * Тест проверяет отсутствие мутаций у входных параметров.
     */
    @Test
    void findTasks__test_no_mutation_page_request_param() {
        final int expectedPage = 22;
        final int expectedSize = 100;
        final String expectedProperty = "title";
        final Sort.Direction expectedDirection = Sort.Direction.DESC;

        final var pageRequestParam = TaskDataUtils.createPageRequestParam(expectedPage, expectedSize, expectedProperty, expectedDirection);
        taskService.findTasks(pageRequestParam);

        final var pageableParamsArgCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(taskRepository).findAll(pageableParamsArgCaptor.capture());

        final var pageableParamsArg = pageableParamsArgCaptor.getValue();
        assertEquals(expectedPage, pageableParamsArg.getPageNumber());
        assertEquals(expectedSize, pageableParamsArg.getPageSize());
        final var sortFromPageRequestParam = Sort.by(expectedDirection, expectedProperty);
        assertEquals(sortFromPageRequestParam, pageableParamsArg.getSort());
    }

    /**
     * Тест проверяет кол-во вызовов репозитория.
     */
    @Test
    void create__test_repo_usage_count() {
        taskService.create(TaskDataUtils.testTaskCreateParam());
        Mockito.verify(taskRepository, Mockito.times(1)).save(any(TaskEntity.class));
    }

    /**
     * Тест проверяет отсутствие мутаций у входных параметров.
     */
    @Test
    void create__test_no_mutation_create_param() {
        final String expectedTitle = "title1";
        final String expectedDescription = "description1";
        final Instant expectedDueDate = Instant.parse("2024-02-18T18:35:24.00Z");

        final var createParam = TaskDataUtils.createTaskCreateParam(expectedTitle, expectedDescription, expectedDueDate);
        taskService.create(createParam);

        final var newTaskEntityArgCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        Mockito.verify(taskRepository).save(newTaskEntityArgCaptor.capture());

        final var newTaskEntityArg = newTaskEntityArgCaptor.getValue();
        assertEquals(expectedTitle, newTaskEntityArg.getTitle());
        assertEquals(expectedDescription, newTaskEntityArg.getDescription());
        assertEquals(expectedDueDate, newTaskEntityArg.getDueDate());
    }

    /**
     * Тест проверяет отсутствие мутаций у результата.
     */
    @Test
    void create__test_no_mutation_result() {
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(TaskDataUtils.testTaskEntity());

        final var actualTask = taskService.create(TaskDataUtils.testTaskCreateParam());
        final var expectedTask = TaskDataUtils.testTaskEntity();

        assertEqualsTaskEntity(expectedTask, actualTask);
    }

    private void assertEqualsTaskEntity(TaskEntity expected, TaskEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getDueDate(), actual.getDueDate());
        assertEquals(expected.isCompleted(), actual.isCompleted());
    }

}