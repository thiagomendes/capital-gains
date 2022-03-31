package capital.gains.application.operation.service;

import capital.gains.application.operation.dto.OperationInputDto;
import capital.gains.application.operation.dto.OperationOutputDto;

public interface OperationTaxCalculationService {

    OperationOutputDto getTaxByOperation(OperationInputDto operationInputDto);
}
