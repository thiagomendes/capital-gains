package capital.gains.infra.common.util;

import capital.gains.application.operation.dto.OperationInputDto;
import capital.gains.application.operation.dto.OperationOutputDto;
import capital.gains.infra.common.util.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

class JsonParserTest {

    @Test
    void testOperationInputDtoListJsonToObjectParse() throws JsonProcessingException {
        String jsonInput = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 100},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 50},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 50}]";
        List<OperationInputDto> operations = Arrays.asList(JsonParser.toObject(jsonInput, OperationInputDto[].class));

        Assertions.assertEquals(3, operations.size());
        Assertions.assertEquals("buy", operations.get(0).getOperation());
        Assertions.assertEquals("sell", operations.get(1).getOperation());
        Assertions.assertEquals("sell", operations.get(2).getOperation());
    }

    @Test
    void testOperationOutputDtoListObjectToJsonParse() throws JsonProcessingException {
        OperationOutputDto operationOutputDto1 = new OperationOutputDto(new BigDecimal("0.00"));
        OperationOutputDto operationOutputDto2 = new OperationOutputDto(new BigDecimal("10000.00"));
        OperationOutputDto operationOutputDto3 = new OperationOutputDto(new BigDecimal("0.00"));
        List<OperationOutputDto> operationsTaxes = Arrays.asList(operationOutputDto1, operationOutputDto2, operationOutputDto3);

        String jsonOutput = JsonParser.toJson(operationsTaxes);
        String expectedJson = "[{\"tax\":0.00},{\"tax\":10000.00},{\"tax\":0.00}]";

        Assertions.assertEquals(expectedJson, jsonOutput);
    }
}
