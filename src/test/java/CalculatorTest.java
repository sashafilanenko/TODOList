import org.example.Calculator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    @Test
    void showAddTwoNums() {
        Calculator calculator = new Calculator();
        int res = calculator.add(2,3);
        assertEquals(5, res);
    }

    @Test
    void shouldWorkWithNegativeNumbers() {
        Calculator calculator = new Calculator();
        int result = calculator.add(-2, -3);
        assertEquals(-5, result);
    }
}
