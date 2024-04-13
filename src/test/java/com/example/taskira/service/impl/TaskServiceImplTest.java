package com.example.taskira.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.taskira.exception.EntityNotFoundException;
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

    /**
     * Тест проверяет кол-во вызовов репозитория.
     */
    @Test
    void findById__test_repo_usage_count() {
        final Long testTaskId = 3L;
        when(taskRepository.findById(testTaskId)).thenReturn(Optional.of(TaskDataUtils.testTaskEntity()));
        taskService.findById(testTaskId);
        Mockito.verify(taskRepository, Mockito.times(1)).findById(testTaskId);
    }

    /**
     * Тест проверяет исключение в случае отсутствия Задачи.
     */
    @Test
    void findById__test_entity_not_found_exception() {
        final Long testTaskId = 3L;
        final var exception = assertThrows(EntityNotFoundException.class, () -> taskService.findById(testTaskId));
        assertEquals("Task with id = 3 not found", exception.getMessage());
    }

    /**
     * Тест проверяет отсутствие мутаций у результата.
     */
    @Test
    void findById__test_no_mutation_result() {
        final Long testTaskId = 3L;
        when(taskRepository.findById(testTaskId)).thenReturn(Optional.of(TaskDataUtils.testTaskEntity()));

        final var actualTask = taskService.findById(testTaskId);
        final var expectedTask = TaskDataUtils.testTaskEntity();

        assertEqualsTaskEntity(expectedTask, actualTask);
    }

    /**
     * Тест проверяет кол-во вызовов репозитория.
     */
    @Test
    void delete__test_repo_usage_count() {
        final Long testTaskId = 3L;

        when(taskRepository.findById(testTaskId)).thenReturn(Optional.of(TaskDataUtils.testTaskEntity()));
        taskService.delete(testTaskId);

        Mockito.verify(taskRepository, Mockito.times(1)).findById(testTaskId);
        Mockito.verify(taskRepository, Mockito.times(1)).delete(any(TaskEntity.class));
    }

    /**
     * Тест проверяет исключение в случае отсутствия Задачи.
     */
    @Test
    void delete__test_entity_not_found_exception() {
        final Long testTaskId = 3L;
        final var exception = assertThrows(EntityNotFoundException.class, () -> taskService.delete(testTaskId));
        assertEquals("Task with id = 3 not found", exception.getMessage());
    }

    /**
     * Тест проверяет отсутствие мутаций.
     */
    @Test
    void delete__test_no_mutation_on_task_for_delete() {
        final Long testTaskId = 3L;
        when(taskRepository.findById(testTaskId)).thenReturn(Optional.of(TaskDataUtils.testTaskEntity()));

        taskService.delete(testTaskId);

        final var taskEntityToDeleteArgCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        Mockito.verify(taskRepository).delete(taskEntityToDeleteArgCaptor.capture());

        final var expectedTaskEntity = TaskDataUtils.testTaskEntity();
        final var actualTaskEntityArg = taskEntityToDeleteArgCaptor.getValue();

        assertEqualsTaskEntity(expectedTaskEntity, actualTaskEntityArg);
    }

    /**
     * Тест проверяет кол-во вызовов репозитория.
     */
    @Test
    void update__test_repo_usage_count() {
        final Long testTaskId = 3L;

        when(taskRepository.findById(testTaskId)).thenReturn(Optional.of(TaskDataUtils.testTaskEntity()));
        taskService.update(testTaskId, TaskDataUtils.testTaskUpdateParam());

        Mockito.verify(taskRepository, Mockito.times(1)).findById(testTaskId);
        Mockito.verify(taskRepository, Mockito.times(1)).save(any(TaskEntity.class));
    }

    /**
     * Тест проверяет исключение в случае отсутствия Задачи.
     */
    @Test
    void update__test_entity_not_found_exception() {
        final Long testTaskId = 3L;
        final var exception = assertThrows(EntityNotFoundException.class, () -> taskService.update(testTaskId,
                TaskDataUtils.testTaskUpdateParam()));
        assertEquals("Task with id = 3 not found", exception.getMessage());
    }

    /**
     * Тест проверяет отсутствие мутаций у входных параметров.
     */
    @Test
    void update__test_no_mutation_update_param() {
        final Long testTaskId = 3L;
        final String expectedTitle = "title33";
        final String expectedDescription = "description33";
        final Instant expectedDueDate = Instant.parse("2024-02-19T00:00:00.00Z");
        final boolean expectedCompleted = true;

        when(taskRepository.findById(testTaskId)).thenReturn(Optional.of(TaskDataUtils.testTaskEntity()));

        final var updateParam = TaskDataUtils.createTaskUpdateParam(expectedTitle, expectedDescription,
                expectedDueDate, expectedCompleted);
        taskService.update(testTaskId, updateParam);

        final var updTaskEntityArgCaptor = ArgumentCaptor.forClass(TaskEntity.class);
        Mockito.verify(taskRepository).save(updTaskEntityArgCaptor.capture());

        final var updTaskEntityArg = updTaskEntityArgCaptor.getValue();
        assertEquals(testTaskId, updTaskEntityArg.getId());
        assertEquals(expectedTitle, updTaskEntityArg.getTitle());
        assertEquals(expectedDescription, updTaskEntityArg.getDescription());
        assertEquals(expectedDueDate, updTaskEntityArg.getDueDate());
        assertEquals(expectedCompleted, updTaskEntityArg.isCompleted());
    }

    /**
     * Тест проверяет отсутствие мутаций у результата.
     */
    @Test
    void update__test_no_mutation_result() {
        final Long testTaskId = 3L;
        final String expectedTitle = "title33";
        final String expectedDescription = "description33";
        final Instant expectedDueDate = Instant.parse("2024-02-19T00:00:00.00Z");
        final boolean expectedCompleted = true;

        var testTaskEntity = TaskDataUtils.testTaskEntity();
        when(taskRepository.findById(testTaskId)).thenReturn(Optional.of(testTaskEntity));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(testTaskEntity);

        final var updateParam = TaskDataUtils.createTaskUpdateParam(expectedTitle, expectedDescription,
                expectedDueDate, expectedCompleted);
        final var actualTaskEntity = taskService.update(testTaskId, updateParam);

        assertEquals(testTaskId, actualTaskEntity.getId());
        assertEquals(expectedTitle, actualTaskEntity.getTitle());
        assertEquals(expectedDescription, actualTaskEntity.getDescription());
        assertEquals(expectedDueDate, actualTaskEntity.getDueDate());
        assertEquals(expectedCompleted, actualTaskEntity.isCompleted());
    }

    private void assertEqualsTaskEntity(TaskEntity expected, TaskEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getDueDate(), actual.getDueDate());
        assertEquals(expected.isCompleted(), actual.isCompleted());
    }

}