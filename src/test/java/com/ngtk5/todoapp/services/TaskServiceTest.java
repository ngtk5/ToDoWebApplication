package com.ngtk5.todoapp.services;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

import com.ngtk5.todoapp.beans.Task;
import com.ngtk5.todoapp.components.RandomGenerater;
import com.ngtk5.todoapp.mappers.TaskMapper;

public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService; // テスト対象のサービス

    @Mock
    private TaskMapper taskMapper; // TaskMapperのモック

    @Mock
    private RandomGenerater randomGenerater;

    // 初期化
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTaskList() throws Exception {
        // Given
        List<Task> exTaskList = List.of(
            new Task("taskId0001", "userId0001", "title 01", "description 01.", false, LocalDate.of(1111, 1, 1), LocalDateTime.now(), null),
            new Task("taskId0002", "userId0002", "title 02", "description 02.", false, LocalDate.of(2222, 2, 2), LocalDateTime.now(), null),
            new Task("taskId0003", "userId0003", "title 03", "description 03.", true, LocalDate.of(3333, 3, 3), LocalDateTime.now(), null)
        );
        doReturn(exTaskList).when(this.taskMapper).selectAll();
        // When
        List<Task> acTaskList = this.taskService.getAllTaskList();
        // Then
        assertThat(acTaskList).isEqualTo(exTaskList);
        verify(this.taskMapper, times(1)).selectAll();
    }

    @Test
    void sortTaskList_getTaskListOrderByDeadline() throws Exception {
        // Given
        List<Task> exTaskList = List.of(
            new Task("taskId0001", "userId0001", "title 01", "description 01.", false, LocalDate.of(1111, 1, 1), LocalDateTime.now(), null),
            new Task("taskId0002", "userId0001", "title 02", "description 02.", true, LocalDate.of(2222, 2, 2), LocalDateTime.now(), null)
        );
        doReturn(exTaskList).when(this.taskMapper).selectByUserIdOrderByDeadline(any());
        // When
        List<Task> acTaskList = this.taskService.sortTaskList("deadline", "test");
        // Then
        assertThat(acTaskList).isEqualTo(exTaskList);
        verify(this.taskMapper, times(1)).selectByUserIdOrderByDeadline(any());
    }

    @Test
    void sortTaskList_getTaskListOrderByDeadlineDown() throws Exception {
        // Given
        List<Task> exTaskList = List.of(
            new Task("taskId0001", "userId0001", "title 01", "description 01.", false, LocalDate.of(1111, 1, 1), LocalDateTime.now(), null),
            new Task("taskId0002", "userId0001", "title 02", "description 02.", true, LocalDate.of(2222, 2, 2), LocalDateTime.now(), null)
        );
        doReturn(exTaskList).when(this.taskMapper).selectByUserIdOrderByDeadlineDown(any());
        // When
        List<Task> acTaskList = this.taskService.sortTaskList("deadlineDown", "test");
        // Then
        assertThat(acTaskList).isEqualTo(exTaskList);
        verify(this.taskMapper, times(1)).selectByUserIdOrderByDeadlineDown(any());
    }

    @Test
    void sortTaskList_getTaskListOrderByTitle() throws Exception {
        // Given
        List<Task> exTaskList = List.of(
            new Task("taskId0001", "userId0001", "title 01", "description 01.", false, LocalDate.of(1111, 1, 1), LocalDateTime.now(), null),
            new Task("taskId0002", "userId0001", "title 02", "description 02.", true, LocalDate.of(2222, 2, 2), LocalDateTime.now(), null)
        );
        doReturn(exTaskList).when(this.taskMapper).selectByUserIdOrderByTitle(any());
        // When
        List<Task> acTaskList = this.taskService.sortTaskList("title", "test");
        // Then
        assertThat(acTaskList).isEqualTo(exTaskList);
        verify(this.taskMapper, times(1)).selectByUserIdOrderByTitle(any());
    }

    @Test
    void sortTaskList_getTaskListOrderByTitleDown() throws Exception {
        // Given
        List<Task> exTaskList = List.of(
            new Task("taskId0001", "userId0001", "title 01", "description 01.", false, LocalDate.of(1111, 1, 1), LocalDateTime.now(), null),
            new Task("taskId0002", "userId0001", "title 02", "description 02.", true, LocalDate.of(2222, 2, 2), LocalDateTime.now(), null)
        );
        doReturn(exTaskList).when(this.taskMapper).selectByUserIdOrderByTitleDown(any());
        // When
        List<Task> acTaskList = this.taskService.sortTaskList("titleDown", "test");
        // Then
        assertThat(acTaskList).isEqualTo(exTaskList);
        verify(this.taskMapper, times(1)).selectByUserIdOrderByTitleDown(any());
    }

    @Test
    void sortTaskList_getTaskListOrderByStatus() throws Exception {
        // Given
        List<Task> exTaskList = List.of(
            new Task("taskId0001", "userId0001", "title 01", "description 01.", false, LocalDate.of(1111, 1, 1), LocalDateTime.now(), null),
            new Task("taskId0002", "userId0001", "title 02", "description 02.", true, LocalDate.of(2222, 2, 2), LocalDateTime.now(), null)
        );
        doReturn(exTaskList).when(this.taskMapper).selectByUserIdOrderByStatus(any());
        // When
        List<Task> acTaskList = this.taskService.sortTaskList("status", "test");
        // Then
        assertThat(acTaskList).isEqualTo(exTaskList);
        verify(this.taskMapper, times(1)).selectByUserIdOrderByStatus(any());
    }

    @Test
    void sortTaskList_getTaskListOrderByStatusDown() throws Exception {
        // Given
        List<Task> exTaskList = List.of(
            new Task("taskId0001", "userId0001", "title 01", "description 01.", false, LocalDate.of(1111, 1, 1), LocalDateTime.now(), null),
            new Task("taskId0002", "userId0001", "title 02", "description 02.", true, LocalDate.of(2222, 2, 2), LocalDateTime.now(), null)
        );
        doReturn(exTaskList).when(this.taskMapper).selectByUserIdOrderByStatusDown(any());
        // When
        List<Task> acTaskList = this.taskService.sortTaskList("statusDown", "test");
        // Then
        assertThat(acTaskList).isEqualTo(exTaskList);
        verify(this.taskMapper, times(1)).selectByUserIdOrderByStatusDown(any());
    }

    @Test
    void getTask() throws Exception {
        // Given
        Task exTask = new Task("taskId0001", "userId0001", "title 01", "description 01.", false, LocalDate.of(1111, 1, 1), LocalDateTime.now(), null);
        doReturn(exTask).when(this.taskMapper).selectByTaskId(any(), any());
        // When
        Task acTask = this.taskService.getTask("null", "null");
        // Then
        assertThat(acTask).isEqualTo(exTask);
        verify(this.taskMapper, times(1)).selectByTaskId(any(), any());
    }

    @Test
    void update() throws Exception {
        // Given
        Task exTask = new Task("taskId0001", "userId0001", "title 01", "description 01.", false, LocalDate.of(1111, 1, 1), LocalDateTime.now(), null);
        doReturn(1).when(this.taskMapper).update(any());
        // When
        int updateCount = this.taskService.update(exTask);
        // Then
        assertThat(updateCount).isEqualTo(1);
        verify(this.taskMapper, times(1)).update(any());
    }

    @Test
    void delete() throws Exception {
        // Given
        doReturn(1).when(this.taskMapper).delete(any(), any());
        // When
        int deleteCount = this.taskService.delete("test", "test");
        // Then
        assertThat(deleteCount).isEqualTo(1);
        verify(this.taskMapper, times(1)).delete(any(), any());
    }

    @Test
    void add() throws Exception {
        // Given
        Task exTask = new Task("taskId0001", "userId0001", "title 01", "description 01.", false, LocalDate.of(1111, 1, 1), LocalDateTime.now(), null);
        doReturn(1).when(this.taskMapper).insert(any());
        // When
        int addCount = this.taskService.add(exTask);
        // Then
        assertThat(addCount).isEqualTo(1);
        verify(this.taskMapper, times(1)).insert(any());
    }

    @Test
    void completedAllDelete() throws Exception {
        // Given
        doReturn(3).when(this.taskMapper).completedAllDelete(any());
        // When
        int completedAllDeleteCount = this.taskService.completedAllDelete("test");
        // Then
        assertThat(completedAllDeleteCount).isEqualTo(3);
        verify(this.taskMapper, times(1)).completedAllDelete(any());
    }

    @Test
    void createNewTaskId() throws Exception {
        // Given
        doReturn("testTaskId").when(this.randomGenerater).generateRandomString(10);
        doReturn(0).when(this.taskMapper).selectCheckTaskId(any());
        // When
        String acTaskId = this.taskService.createNewTaskId();
        // Then
        assertThat(acTaskId).isEqualTo("testTaskId");
        verify(this.randomGenerater, times(1)).generateRandomString(10);
        verify(this.taskMapper, times(1)).selectCheckTaskId(any());
    }
}