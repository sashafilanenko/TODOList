import org.example.TODO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.example.TodoService;
import org.example.TodoRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository repository;

    @InjectMocks
    private TodoService service;

    @Test
    void shouldCreateTodo() {

        when(repository.save(any(TODO.class)))
                .thenAnswer(invocation -> {
                    TODO arg = invocation.getArgument(0);
                    return new TODO(1L, arg.getText());
                });

        TODO result = service.createTodo("Buy milk");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Buy milk", result.getText());

        verify(repository, times(1)).save(any(TODO.class));

        ArgumentCaptor<TODO> captor = ArgumentCaptor.forClass(TODO.class);
        verify(repository).save(captor.capture());
        TODO savedArgument = captor.getValue();
        assertNull(savedArgument.getId(), "до сохранения id должен быть null");
        assertEquals("Buy milk", savedArgument.getText());
    }

    @Test
    void shouldThrowWhenTextIsEmpty() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            service.createTodo("   ");
        });
        assertEquals("Text must not be empty", ex.getMessage());

        verify(repository, never()).save(any());
    }
}
