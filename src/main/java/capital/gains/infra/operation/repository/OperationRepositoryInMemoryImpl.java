package capital.gains.infra.operation.repository;

import capital.gains.domain.operation.entity.Operation;
import capital.gains.domain.operation.repository.OperationRepository;

import java.util.ArrayList;
import java.util.List;

public class OperationRepositoryInMemoryImpl implements OperationRepository {

    private final List<Operation> operations = new ArrayList<>();

    @Override
    public void save(Operation operation) {
        this.operations.add(operation);
    }

    @Override
    public List<Operation> findAll() {
        return this.operations;
    }
}
