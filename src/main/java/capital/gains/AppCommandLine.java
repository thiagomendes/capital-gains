package capital.gains;

import capital.gains.application.operation.dto.OperationInputDto;
import capital.gains.application.operation.dto.OperationOutputDto;
import capital.gains.application.operation.service.OperationTaxCalculationService;
import capital.gains.application.operation.service.OperationTaxCalculationServiceImpl;
import capital.gains.infra.operation.repository.OperationRepositoryInMemoryImpl;
import capital.gains.infra.common.util.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AppCommandLine {

    public static void main(String[] args) throws JsonProcessingException {

        System.out.println("Capital Gains is Running!");
        System.out.println("-------------------------\n");

        Scanner scanner = new Scanner(System.in);
        String line;

        while (!(line = scanner.nextLine()).trim().isEmpty()) {
            System.out.println(processInput(line));
        }
    }

    public static String processInput(String line) throws JsonProcessingException {
        List<OperationInputDto> operations = Arrays.asList(JsonParser.toObject(line, OperationInputDto[].class));
        OperationTaxCalculationService operationTaxCalculationService = new OperationTaxCalculationServiceImpl(new OperationRepositoryInMemoryImpl());

        List<OperationOutputDto> operationsTaxes = operations
                .stream()
                .map(operationTaxCalculationService::getTaxByOperation)
                .collect(Collectors.toList());

        return JsonParser.toJson(operationsTaxes);
    }
}
