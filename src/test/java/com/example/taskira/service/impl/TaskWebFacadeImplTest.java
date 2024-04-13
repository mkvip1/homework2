package com.example.taskira.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;

import com.example.taskira.dto.in.PageRequestParam;
import com.example.taskira.dto.in.TaskCreateParam;
import com.example.taskira.dto.in.TaskUpdateParam;
import com.example.taskira.dto.out.TaskResponse;
import com.example.taskira.exception.EntityNotFoundException;
import com.example.taskira.repository.entity.TaskEntity;
import com.example.taskira.service.TaskService;
import com.example.taskira.service.TaskWebFacade;
import com.example.taskira.utils.TaskDataUtils;

class TaskWebFacadeImplTest {

    private final TaskWebFacade taskWebFacade;
    private final TaskService taskService;

    public TaskWebFacadeImplTest() {
        taskService = Mockito.mock(TaskService.class);
        taskWebFacade = new TaskWebFacadeImpl(taskService);
    }

    /**
     * Тест проверяет кол-во вызовов сервиса.
     */
    @Test
    void findTasks__test_usage_count() {
        taskWebFacade.findTasks(TaskDataUtils.testParam());
        Mockito.verify(taskService, Mockito.times(1)).findTasks(any(PageRequestParam.class));
    }

    /**
     * Тест проверяет отсутствие мутаций у входных параметров.
     */
    @Test
    void findTasks__test_no_mutation_param() {
        final int expectedPage = 22;
        final int expectedSize = 100;
        final String expectedProperty = "title";
        final Sort.Direction expectedDirection = Sort.Direction.DESC;

        final var pageRequestParam = TaskDataUtils.createPageRequestParam(expectedPage, expectedSize, expectedProperty, expectedDirection);
        taskWebFacade.findTasks(pageRequestParam);

        final var pageRequestParamArgCaptor = ArgumentCaptor.forClass(PageRequestParam.class);
        Mockito.verify(taskService).findTasks(pageRequestParamArgCaptor.capture());

        final var pageRequestParamArg = pageRequestParamArgCaptor.getValue();
        assertEquals(expectedPage, pageRequestParamArg.getPage());
        assertEquals(expectedSize, pageRequestParamArg.getSize());
        assertEquals(expectedProperty, pageRequestParamArg.getProperty());
        assertEquals(expectedDirection, pageRequestParamArg.getDirection());
    }

    /**
     * Тест проверяет отсутствие мутаций у результата
     */
    @Test
    void findTasks__test_no_mutation_result() {
        when(taskService.findTasks(any(PageRequestParam.class)))
                .thenReturn(TaskDataUtils.testPage());

        final var actualResponsePage = taskWebFacade.findTasks(TaskDataUtils.testParam());
        final var expectedPage = TaskDataUtils.testPage();

        assertEquals(expectedPage.getContent().size(), actualResponsePage.getContent().size());
        for (var i = 0; i < actualResponsePage.getContent().size(); i++) {
            final var actualEl = actualResponsePage.getContent().get(i);
            final var expectedEl = expectedPage.getContent().get(i);
            assertEqualsTaskEntityWithTaskResponse(expectedEl, actualEl);
        }
    }

    /**
     * Тест проверяет кол-во вызовов сервиса.
     */
    @Test
    void createTask__test_usage_count() {
        taskWebFacade.create(TaskDataUtils.testTaskCreateParam());
        Mockito.verify(taskService, Mockito.times(1)).create(any(TaskCreateParam.class));
    }

    /**
     * Тест проверяет отсутствие мутаций у входных параметров.
     */
    @Test
    void createTask__test_no_mutation_param() {
        final String expectedTitle = "title1";
        final String expectedDescription = "description1";
        final Instant expectedDueDate = Instant.parse("2024-02-18T18:35:24.00Z");

        final var createParam = TaskDataUtils.createTaskCreateParam(expectedTitle, expectedDescription, expectedDueDate);
        taskWebFacade.create(createParam);

        final var createParamArgCaptor = ArgumentCaptor.forClass(TaskCreateParam.class);
        Mockito.verify(taskService).create(createParamArgCaptor.capture());

        final var createParamArg = createParamArgCaptor.getValue();
        assertEquals(expectedTitle, createParamArg.getTitle());
        assertEquals(expectedDescription, createParamArg.getDescription());
        assertEquals(expectedDueDate, createParamArg.getDueDate());
    }

    /**
     * Тест проверяет отсутствие мутаций у результата.
     */
    @Test
    void createTask__test_no_mutation_result() {
        when(taskService.create(any(TaskCreateParam.class)))
                .thenReturn(TaskDataUtils.testTaskEntity());

        final var actualTaskResponse = taskWebFacade.create(TaskDataUtils.testTaskCreateParam());
        final var expectedTask = TaskDataUtils.testTaskEntity();

        assertEqualsTaskEntityWithTaskResponse(expectedTask, actualTaskResponse);
    }

    /**
     * Тест проверяет кол-во вызовов сервиса.
     */
    @Test
    void findById__test_usage_count() {
        final Long testTaskId = 3L;
        taskWebFacade.findById(testTaskId);
        Mockito.verify(taskService, Mockito.times(1)).findById(testTaskId);
    }

    /**
     * Тест проверяет протакливание исключения в случае отсутствия Задачи.
     */
    @Test
    void findById__test_entity_not_found_exception() {
        final Long testTaskId = 3L;
        when(taskService.findById(testTaskId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> taskWebFacade.findById(testTaskId));
    }

    /**
     * Тест проверяет отсутствие мутаций у результата.
     */
    @Test
    void findById__test_no_mutation_result() {
        final Long testTaskId = 3L;
        when(taskService.findById(testTaskId)).thenReturn(TaskDataUtils.testTaskEntity());

        final var actualTaskResponse = taskWebFacade.findById(testTaskId);
        final var expectedTask = TaskDataUtils.testTaskEntity();

        assertEqualsTaskEntityWithTaskResponse(expectedTask, actualTaskResponse);
    }

    /**
     * Тест проверяет кол-во вызовов сервиса.
     */
    @Test
    void updateTask__test_usage_count() {
        final Long testTaskId = 3L;
        taskWebFacade.update(testTaskId, TaskDataUtils.testTaskUpdateParam());
        Mockito.verify(taskService, Mockito.times(1)).update(eq(testTaskId), any(TaskUpdateParam.class));
    }

    /**
     * Тест проверяет протакливание исключения в случае отсутствия Задачи.
     */
    @Test
    void updateTask__test_entity_not_found_exception() {
        final Long testTaskId = 3L;
        when(taskService.update(eq(testTaskId), any(TaskUpdateParam.class)))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> taskWebFacade.update(testTaskId,
                TaskDataUtils.testTaskUpdateParam()));
    }

    /**
     * Тест проверяет отсутствие мутаций у входных параметров.
     */
    @Test
    void updateTask__test_no_mutation_param() {
        final Long testTaskId = 3L;
        final String expectedTitle = "title33";
        final String expectedDescription = "description33";
        final Instant expectedDueDate = Instant.parse("2024-02-19T00:00:00.00Z");
        final boolean expectedCompleted = true;

        final var updateParam = TaskDataUtils.createTaskUpdateParam(expectedTitle, expectedDescription,
                expectedDueDate, expectedCompleted);
        taskWebFacade.update(testTaskId, updateParam);

        final var updateParamArgCaptor = ArgumentCaptor.forClass(TaskUpdateParam.class);
        Mockito.verify(taskService).update(eq(testTaskId), updateParamArgCaptor.capture());

        final var updateParamArg = updateParamArgCaptor.getValue();
        assertEquals(expectedTitle, updateParamArg.getTitle());
        assertEquals(expectedDescription, updateParamArg.getDescription());
        assertEquals(expectedDueDate, updateParamArg.getDueDate());
        assertEquals(expectedCompleted, updateParamArg.isCompleted());
    }

    /**
     * Тест проверяет отсутствие мутаций у результата.
     */
    @Test
    void updateTask__test_no_mutation_result() {
        final Long testTaskId = 3L;

        when(taskService.update(eq(testTaskId), any(TaskUpdateParam.class)))
                .thenReturn(TaskDataUtils.testTaskEntity());

        final var actualTaskResponse = taskWebFacade.update(testTaskId, TaskDataUtils.testTaskUpdateParam());
        final var expectedTask = TaskDataUtils.testTaskEntity();

        assertEquals(expectedTask.getId(), actualTaskResponse.getId());
        assertEquals(expectedTask.getTitle(), actualTaskResponse.getTitle());
        assertEquals(expectedTask.getDescription(), actualTaskResponse.getDescription());
        assertEquals(expectedTask.getDueDate(), actualTaskResponse.getDueDate());
        assertEquals(expectedTask.isCompleted(), actualTaskResponse.isCompleted());
    }

    /**
     * Тест проверяет кол-во вызовов сервиса.
     */
    @Test
    void deleteTask__test_usage_count() {
        final Long testTaskId = 3L;
        taskWebFacade.delete(testTaskId);
        Mockito.verify(taskService, Mockito.times(1)).delete(testTaskId);
    }

    /**
     * Тест проверяет протакливание исключения в случае отсутствия Задачи.
     */
    @Test
    void deleteTask__test_entity_not_found_exception() {
        final Long testTaskId = 3L;
        doThrow(EntityNotFoundException.class).when(taskService).delete(testTaskId);

        assertThrows(EntityNotFoundException.class, () -> taskWebFacade.delete(testTaskId));
    }

    private void assertEqualsTaskEntityWithTaskResponse(TaskEntity expected, TaskResponse actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getDueDate(), actual.getDueDate());
        assertEquals(expected.isCompleted(), actual.isCompleted());
    }

}