package capital.gains.infra.controller;

import capital.gains.application.operation.dto.OperationInputDto;
import capital.gains.application.operation.dto.OperationOutputDto;
import capital.gains.application.operation.service.OperationTaxCalculationService;
import capital.gains.application.operation.service.OperationTaxCalculationServiceImpl;
import capital.gains.infra.operation.repository.OperationRepositoryInMemoryImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OperationTaxCalculationController {

    @PostMapping("/getTaxByOperations")
    public List<OperationOutputDto> getTaxByOperations(@RequestBody List<OperationInputDto> operations) {
        OperationTaxCalculationService operationTaxCalculationService = new OperationTaxCalculationServiceImpl(new OperationRepositoryInMemoryImpl());
        return operations.stream().map(operationTaxCalculationService::getTaxByOperation).collect(Collectors.toList());
    }
}
