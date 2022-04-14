package capital.gains.application.operation.service;

import capital.gains.application.operation.dto.OperationInputDto;
import capital.gains.application.operation.dto.OperationOutputDto;
import capital.gains.domain.operation.entity.Operation;

public interface OperationTaxCalculationService {

    OperationOutputDto getTaxByOperation(OperationInputDto operationInputDto);

    public long getTotalQuantity();

    public boolean isBuy(Operation operation);
}
