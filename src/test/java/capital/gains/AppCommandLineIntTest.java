package capital.gains;

import capital.gains.application.operation.dto.OperationOutputDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class AppCommandLineIntTest {

    @Test
    void testInputOperationListAndGetOutputTaxes() throws JsonProcessingException {
        String input = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000}]\n";
        String output = AppCommandLine.processInput(input);
        String expectedOutput = "[{\"tax\":0.00},{\"tax\":10000.00},{\"tax\":0.00}]";
        Assertions.assertEquals(expectedOutput, output);
    }

    @Test
    void testSellMoreThanLimit() throws JsonProcessingException {
        String input = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 11000}]\n";
        List<OperationOutputDto> operationsOutput = new ArrayList<>();

        String output = AppCommandLine.processInput(input);
        String expectedOutput = "[{\"tax\":0.00},{\"tax\":0.00,\"error\":\"Can't sell more stocks than you have\"}]";

        Assertions.assertEquals(expectedOutput, output);
    }

    @Test
    void testSellMoreThanLimitWithTwoSell() throws JsonProcessingException {
        String input = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 100},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 50}, {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 60}]\n";
        List<OperationOutputDto> operationsOutput = new ArrayList<>();

        String output = AppCommandLine.processInput(input);
        String expectedOutput = "[{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00,\"error\":\"Can't sell more stocks than you have\"}]";

        Assertions.assertEquals(expectedOutput, output);
    }

    @Test
    void testSellMoreThanLimitWithNoBuys() throws JsonProcessingException {
        String input = "[{\"operation\":\"sell\", \"unit-cost\":10.00, \"quantity\": 100},{\"operation\":\"buy\", \"unit-cost\":20.00, \"quantity\": 50}, {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 60}]\n";
        List<OperationOutputDto> operationsOutput = new ArrayList<>();

        String output = AppCommandLine.processInput(input);
        String expectedOutput = "[{\"tax\":0.00,\"error\":\"Can't sell more stocks than you have\"},{\"tax\":0.00},{\"tax\":0.00,\"error\":\"Can't sell more stocks than you have\"}]";

        Assertions.assertEquals(expectedOutput, output);
    }

    @Test
    void testSellMoreThanLimitWithErroBetween() throws JsonProcessingException {
        String input = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 100},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 110}, {\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 60}]\n";
        List<OperationOutputDto> operationsOutput = new ArrayList<>();

        String output = AppCommandLine.processInput(input);
        String expectedOutput = "[{\"tax\":0.00},{\"tax\":0.00,\"error\":\"Can't sell more stocks than you have\"},{\"tax\":0.00}]";

        Assertions.assertEquals(expectedOutput, output);
    }
}
