package capital.gains;

import capital.gains.application.operation.dto.OperationInputDto;
import capital.gains.application.operation.dto.OperationOutputDto;
import capital.gains.application.operation.service.OperationTaxCalculationService;
import capital.gains.application.operation.service.OperationTaxCalculationServiceImpl;
import capital.gains.infra.common.util.JsonParser;
import capital.gains.infra.operation.repository.OperationRepositoryInMemoryImpl;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
        List<OperationOutputDto> operationsTaxes = new ArrayList<>();

        operations.forEach(i -> {
            if ("sell".equals(i.getOperation()) && i.getQuantity() > operationTaxCalculationService.getTotalQuantity()) {
                operationsTaxes.add(new OperationOutputDto(BigDecimal.ZERO, "Can't sell more stocks than you have"));
            } else {
                operationsTaxes.add(operationTaxCalculationService.getTaxByOperation(i));
            }
        });

        return JsonParser.toJson(operationsTaxes);
    }
}
