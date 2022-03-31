package capital.gains.domain.operation.repository;

import capital.gains.domain.operation.entity.Operation;

import java.util.List;

public interface OperationRepository {

    void save(Operation operation);

    List<Operation> findAll();

}
