package capital.gains.application.operation.converter;

import capital.gains.application.operation.dto.OperationInputDto;
import capital.gains.domain.operation.entity.Operation;
import capital.gains.domain.operation.entity.OperationType;

public class OperationConverter {

    private OperationConverter() {
    }

    public static Operation fromDto(OperationInputDto operationInputDto) {
        Operation operation = new Operation();
        operation.setType(operationInputDto.getOperation().equals("buy") ? OperationType.BUY : OperationType.SELL);
        operation.setUnitCost(operationInputDto.getUnitCost());
        operation.setQuantity(operationInputDto.getQuantity());
        return operation;
    }
}
