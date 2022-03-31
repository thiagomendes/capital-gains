package capital.gains.infra.operation.repository;

import capital.gains.domain.operation.entity.Operation;
import capital.gains.domain.operation.entity.OperationType;
import capital.gains.domain.operation.repository.OperationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

class OperationRepositoryInMemoryImplTest {

    @Test
    void testFindOperationsAfterSave() {
        OperationRepository operationRepository = new OperationRepositoryInMemoryImpl();

        Operation operation1 = new Operation();
        operation1.setQuantity(10);
        operation1.setUnitCost(new BigDecimal("10.00"));
        operation1.setType(OperationType.BUY);

        Operation operation2 = new Operation();
        operation2.setQuantity(15);
        operation2.setUnitCost(new BigDecimal("5.00"));
        operation2.setType(OperationType.SELL);

        operationRepository.save(operation1);
        operationRepository.save(operation2);

        List<Operation> allOperations = operationRepository.findAll();

        Assertions.assertEquals(2, allOperations.size());
        Assertions.assertEquals(OperationType.BUY, allOperations.get(0).getType());
        Assertions.assertEquals(OperationType.SELL, allOperations.get(1).getType());
    }
}
