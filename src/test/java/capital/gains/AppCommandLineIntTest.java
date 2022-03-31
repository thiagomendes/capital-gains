package capital.gains;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AppCommandLineIntTest {

    @Test
    void testInputOperationListAndGetOutputTaxes() throws JsonProcessingException {
        String input = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000}]\n";
        String output = AppCommandLine.processInput(input);
        String expectedOutput = "[{\"tax\":0.00},{\"tax\":10000.00},{\"tax\":0.00}]";
        Assertions.assertEquals(expectedOutput, output);
    }
}
