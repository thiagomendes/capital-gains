package capital.gains.application.operation.converter;

import capital.gains.application.operation.dto.OperationInputDto;
import capital.gains.domain.operation.entity.Operation;
import capital.gains.domain.operation.entity.OperationType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class OperationConverterTest {

    @Test
    void testOperationConverterFromDto() {
        OperationInputDto operationInputDtoBuy1 = new OperationInputDto();
        operationInputDtoBuy1.setOperation("buy");
        operationInputDtoBuy1.setQuantity(100);
        operationInputDtoBuy1.setUnitCost(new BigDecimal("10.00"));

        OperationInputDto operationInputDtoSell2 = new OperationInputDto();
        operationInputDtoSell2.setOperation("sell");
        operationInputDtoSell2.setQuantity(50);
        operationInputDtoSell2.setUnitCost(new BigDecimal("15.00"));

        Operation operation1 = OperationConverter.fromDto(operationInputDtoBuy1);
        Operation operation2 = OperationConverter.fromDto(operationInputDtoSell2);

        Assertions.assertEquals(OperationType.BUY, operation1.getType());
        Assertions.assertEquals(100, operation1.getQuantity());
        Assertions.assertEquals(new BigDecimal("10.00"), operation1.getUnitCost());

        Assertions.assertEquals(OperationType.SELL, operation2.getType());
        Assertions.assertEquals(50, operation2.getQuantity());
        Assertions.assertEquals(new BigDecimal("15.00"), operation2.getUnitCost());
    }
}
