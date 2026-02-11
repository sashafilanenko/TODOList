import org.example.controller.TaskController;
import org.example.model.Task;
import org.example.model.TaskRepository;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class TaskControllerTest {

    private TaskController controller;
    private TaskRepository repositoryMock;

    @BeforeEach
    void setUP(){
        repositoryMock = Mockito.mock(TaskRepository.class);
        controller = new TaskController(repositoryMock);
    }

    @Test
    void testAddTask_ShouldSaveToRepository(){
        String title = "test";
        String desc = "desc_test";

        controller.addTask(title, desc);

        verify(repositoryMock).save(any(Task.class));
    }

    @Test
    void testDeleteTask_ShouldCallReposDelete(){
        controller.deleteTask(5);

        verify(repositoryMock, times(1)).deleteById(5);
    }

}
