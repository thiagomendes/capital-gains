package capital.gains.application.operation.service;

import capital.gains.application.operation.converter.OperationConverter;
import capital.gains.application.operation.dto.OperationInputDto;
import capital.gains.application.operation.dto.OperationOutputDto;
import capital.gains.domain.operation.entity.Operation;
import capital.gains.domain.operation.entity.OperationType;
import capital.gains.domain.operation.repository.OperationRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class OperationTaxCalculationServiceImpl implements OperationTaxCalculationService {

    private static final String TAX_ALIQUOT = "0.20";

    private static final String SELL_LIMIT = "20000";

    private BigDecimal totalLoss = BigDecimal.ZERO;

    private final OperationRepository operationRepository;

    public OperationTaxCalculationServiceImpl(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public OperationOutputDto getTaxByOperation(OperationInputDto operationInputDto) {
        Operation operation = OperationConverter.fromDto(operationInputDto);
        this.operationRepository.save(operation);
        return new OperationOutputDto(calculateOperationTax(operation));
    }

    private BigDecimal calculateOperationAmount(Operation operation) {
        return operation.getUnitCost().multiply(new BigDecimal(operation.getQuantity()));
    }

    private BigDecimal calculateOperationTax(Operation operation) {

        if (isBuy(operation)) {
            return BigDecimal.ZERO;
        }

        BigDecimal weightedAverage = calculateWeightedAverage();
        BigDecimal operationAmount = calculateOperationAmount(operation);

        if (isOperationWithGain(operation, weightedAverage)) {

            if (isOperationWithinSellLimit(operationAmount, new BigDecimal(SELL_LIMIT))) {
                return BigDecimal.ZERO;
            }

            BigDecimal gain = calculateGain(operationAmount, weightedAverage, operation.getQuantity());

            if (isThereLossToDiscount()) {
                gain = discountLoss(gain);
            }

            return calculateTaxFromGain(gain, new BigDecimal(TAX_ALIQUOT));
        }

        increaseTotalLoss(operation, weightedAverage, operationAmount);
        return BigDecimal.ZERO;

    }

    private boolean isThereLossToDiscount() {
        return this.totalLoss.compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean isOperationWithinSellLimit(BigDecimal operationAmount, BigDecimal sellLimit) {
        return operationAmount.compareTo(sellLimit) < 0;
    }

    private boolean isOperationWithGain(Operation operation, BigDecimal weightedAverage) {
        return operation.getUnitCost().compareTo(weightedAverage) > 0;
    }

    private void increaseTotalLoss(Operation operation, BigDecimal weightedAverage, BigDecimal operationAmount) {
        this.totalLoss = totalLoss.add(calculateLoss(operationAmount, weightedAverage, operation.getQuantity()));
    }

    public boolean isBuy(Operation operation) {
        return OperationType.BUY.equals(operation.getType());
    }

    private BigDecimal calculateTaxFromGain(BigDecimal gain, BigDecimal taxALiquot) {
        return gain.multiply(taxALiquot);
    }

    private BigDecimal discountLoss(BigDecimal gain) {
        if (this.totalLoss.compareTo(gain) > 0) {
            this.totalLoss = this.totalLoss.subtract(gain);
            return BigDecimal.ZERO;
        }

        BigDecimal result = gain.subtract(this.totalLoss);
        this.totalLoss = BigDecimal.ZERO;
        return result;
    }

    private BigDecimal calculateGain(BigDecimal operationAmount, BigDecimal weightedAverage, long operationQuantity) {
        return operationAmount.subtract(weightedAverage.multiply(new BigDecimal(operationQuantity)));
    }

    private BigDecimal calculateLoss(BigDecimal operationAmount, BigDecimal weightedAverage, long operationQuantity) {
        return weightedAverage.multiply(new BigDecimal(operationQuantity)).subtract(operationAmount);
    }

    public long getTotalQuantity() {
        return this.operationRepository
                .findAll()
                .stream()
                .mapToLong(o -> o.getType().equals(OperationType.BUY) ? o.getQuantity() : o.getQuantity() * -1)
                .sum();
    }

    private BigDecimal calculateWeightedAverage() {
        List<Operation> allOperations = this.operationRepository.findAll();

        BigDecimal totalQuantity = new BigDecimal(
                allOperations
                        .stream()
                        .filter(this::isBuy)
                        .mapToLong(Operation::getQuantity)
                        .sum()
        );

        return allOperations
                .stream()
                .filter(this::isBuy)
                .map(i -> (new BigDecimal(i.getQuantity()).multiply(i.getUnitCost())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(totalQuantity, RoundingMode.HALF_UP);
    }
}
