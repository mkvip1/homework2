package com.example.taskira.controller;

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
import com.example.taskira.service.TaskWebFacade;
import com.example.taskira.utils.TaskDataUtils;

class TaskControllerTest {

    private final TaskController taskController;
    private final TaskWebFacade taskWebFacade;

    public TaskControllerTest() {
        taskWebFacade = Mockito.mock(TaskWebFacade.class);
        taskController = new TaskController(taskWebFacade);
    }

    /**
     * Тест проверяет кол-во вызовов.
     */
    @Test
    void findTasks__test_usage_count() {
        taskController.findTasks(TaskDataUtils.testParam());
        Mockito.verify(taskWebFacade, Mockito.times(1)).findTasks(any(PageRequestParam.class));
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
        taskController.findTasks(pageRequestParam);

        final var pageRequestParamArgCaptor = ArgumentCaptor.forClass(PageRequestParam.class);
        Mockito.verify(taskWebFacade).findTasks(pageRequestParamArgCaptor.capture());

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
        when(taskWebFacade.findTasks(any(PageRequestParam.class)))
                .thenReturn(TaskDataUtils.testPageResponse());

        final var actualPage = taskController.findTasks(TaskDataUtils.testParam());
        final var expectedPage = TaskDataUtils.testPageResponse();

        assertEquals(expectedPage.getContent().size(), actualPage.getContent().size());
        for (var i = 0; i < actualPage.getContent().size(); i++) {
            final var actualEl = actualPage.getContent().get(i);
            final var expectedEl = expectedPage.getContent().get(i);
            assertEqualsTaskResponse(expectedEl, actualEl);
        }
    }

    /**
     * Тест проверяет кол-во вызовов.
     */
    @Test
    void createTask__test_usage_count() {
        taskController.createTask(TaskDataUtils.testTaskCreateParam());
        Mockito.verify(taskWebFacade, Mockito.times(1)).create(any(TaskCreateParam.class));
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
        taskController.createTask(createParam);

        final var createParamArgCaptor = ArgumentCaptor.forClass(TaskCreateParam.class);
        Mockito.verify(taskWebFacade).create(createParamArgCaptor.capture());

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
        when(taskWebFacade.create(any(TaskCreateParam.class)))
                .thenReturn(TaskDataUtils.testTaskResponse());

        final var actualTask = taskController.createTask(TaskDataUtils.testTaskCreateParam());
        final var expectedTask = TaskDataUtils.testTaskResponse();

        assertEqualsTaskResponse(expectedTask, actualTask);
    }

    /**
     * Тест проверяет кол-во вызовов.
     */
    @Test
    void findById__test_usage_count() {
        final Long testTaskId = 3L;
        taskController.findById(testTaskId);
        Mockito.verify(taskWebFacade, Mockito.times(1)).findById(testTaskId);
    }

    /**
     * Тест проверяет протакливание исключения в случае отсутствия Задачи.
     */
    @Test
    void findById__test_entity_not_found_exception() {
        final Long testTaskId = 3L;
        when(taskWebFacade.findById(testTaskId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> taskController.findById(testTaskId));
    }

    /**
     * Тест проверяет отсутствие мутаций у результата.
     */
    @Test
    void findById__test_no_mutation_result() {
        final Long testTaskId = 3L;
        when(taskWebFacade.findById(testTaskId)).thenReturn(TaskDataUtils.testTaskResponse());

        final var actualTask = taskController.findById(testTaskId);
        final var expectedTask = TaskDataUtils.testTaskResponse();

        assertEqualsTaskResponse(expectedTask, actualTask);
    }

    /**
     * Тест проверяет кол-во вызовов.
     */
    @Test
    void updateTask__test_usage_count() {
        final Long testTaskId = 3L;
        taskController.updateTask(testTaskId, TaskDataUtils.testTaskUpdateParam());
        Mockito.verify(taskWebFacade, Mockito.times(1))
                .update(eq(testTaskId), any(TaskUpdateParam.class));
    }

    /**
     * Тест проверяет протакливание исключения в случае отсутствия Задачи.
     */
    @Test
    void updateTask__test_entity_not_found_exception() {
        final Long testTaskId = 3L;
        when(taskWebFacade.update(eq(testTaskId), any(TaskUpdateParam.class)))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> taskController.updateTask(testTaskId,
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
        taskController.updateTask(testTaskId, updateParam);

        final var updateParamArgCaptor = ArgumentCaptor.forClass(TaskUpdateParam.class);
        Mockito.verify(taskWebFacade).update(eq(testTaskId), updateParamArgCaptor.capture());

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

        when(taskWebFacade.update(eq(testTaskId), any(TaskUpdateParam.class)))
                .thenReturn(TaskDataUtils.testTaskResponse());

        final var actualTask = taskController.updateTask(testTaskId, TaskDataUtils.testTaskUpdateParam());
        final var expectedTask = TaskDataUtils.testTaskEntity();

        assertEquals(expectedTask.getId(), actualTask.getId());
        assertEquals(expectedTask.getTitle(), actualTask.getTitle());
        assertEquals(expectedTask.getDescription(), actualTask.getDescription());
        assertEquals(expectedTask.getDueDate(), actualTask.getDueDate());
        assertEquals(expectedTask.isCompleted(), actualTask.isCompleted());
    }

    /**
     * Тест проверяет кол-во вызовов.
     */
    @Test
    void deleteTask__test_usage_count() {
        final Long testTaskId = 3L;
        taskController.deleteTask(testTaskId);
        Mockito.verify(taskWebFacade, Mockito.times(1))
                .delete(testTaskId);
    }

    /**
     * Тест проверяет протакливание исключения в случае отсутствия Задачи.
     */
    @Test
    void deleteTask__test_entity_not_found_exception() {
        final Long testTaskId = 3L;
        doThrow(EntityNotFoundException.class).when(taskWebFacade).delete(testTaskId);

        assertThrows(EntityNotFoundException.class, () -> taskController.deleteTask(testTaskId));
    }

    private void assertEqualsTaskResponse(TaskResponse expected, TaskResponse actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getDueDate(), actual.getDueDate());
        assertEquals(expected.isCompleted(), actual.isCompleted());
    }

}