package capital.gains.application.operation.service;

import capital.gains.application.operation.converter.OperationConverter;
import capital.gains.application.operation.dto.OperationInputDto;
import capital.gains.application.operation.dto.OperationOutputDto;
import capital.gains.domain.operation.entity.Operation;
import capital.gains.domain.operation.repository.OperationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationTaxCalculationServiceImplTest {

    @InjectMocks
    private OperationTaxCalculationServiceImpl operationService;

    @Spy
    private OperationRepository operationRepository;

    private ArrayList<Operation> inMemoryOperations;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        this.inMemoryOperations = new ArrayList<>();
        doNothing().when(operationRepository).save(any());
        when(operationRepository.findAll()).thenReturn(inMemoryOperations);
    }

    @DisplayName("(case #1) - Simple buy and sell, within the limit, no need to deduct tax")
    @Test
    void testSimpleBuyAndSellWithinTheLimit() {
        OperationInputDto operationInputDtoBuy1 = new OperationInputDto();
        operationInputDtoBuy1.setOperation("buy");
        operationInputDtoBuy1.setQuantity(100);
        operationInputDtoBuy1.setUnitCost(new BigDecimal("10.00"));

        OperationInputDto operationInputDtoSell2 = new OperationInputDto();
        operationInputDtoSell2.setOperation("sell");
        operationInputDtoSell2.setQuantity(50);
        operationInputDtoSell2.setUnitCost(new BigDecimal("15.00"));

        OperationInputDto operationInputDtoSell3 = new OperationInputDto();
        operationInputDtoSell3.setOperation("sell");
        operationInputDtoSell3.setQuantity(50);
        operationInputDtoSell3.setUnitCost(new BigDecimal("15.00"));

        List<OperationOutputDto> operationsOutput = new ArrayList<>();

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoBuy1));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoBuy1));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell2));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell2));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell3));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell3));

        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(0).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(1).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(2).getTax());
    }

    @DisplayName("(case #2) - Simple buy, sell over the limit with need for tax deduction, and sell with loss")
    @Test
    void testSellWithTaxDeductionAndSellWithLoss() {
        OperationInputDto operationInputDtoBuy1 = new OperationInputDto();
        operationInputDtoBuy1.setOperation("buy");
        operationInputDtoBuy1.setQuantity(10000);
        operationInputDtoBuy1.setUnitCost(new BigDecimal("10.00"));

        OperationInputDto operationInputDtoSell2 = new OperationInputDto();
        operationInputDtoSell2.setOperation("sell");
        operationInputDtoSell2.setQuantity(5000);
        operationInputDtoSell2.setUnitCost(new BigDecimal("20.00"));

        OperationInputDto operationInputDtoSell3 = new OperationInputDto();
        operationInputDtoSell3.setOperation("sell");
        operationInputDtoSell3.setQuantity(5000);
        operationInputDtoSell3.setUnitCost(new BigDecimal("5.00"));

        List<OperationOutputDto> operationsOutput = new ArrayList<>();

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoBuy1));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoBuy1));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell2));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell2));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell3));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell3));

        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(0).getTax());
        Assertions.assertEquals(new BigDecimal("10000.00"), operationsOutput.get(1).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(2).getTax());
    }

    @DisplayName("(case #3) - Sell with loss, and sell with gain discounting loss")
    @Test
    void testSellWithGainDiscountingLoss() {
        OperationInputDto operationInputDtoBuy1 = new OperationInputDto();
        operationInputDtoBuy1.setOperation("buy");
        operationInputDtoBuy1.setQuantity(10000);
        operationInputDtoBuy1.setUnitCost(new BigDecimal("10.00"));

        OperationInputDto operationInputDtoSell2 = new OperationInputDto();
        operationInputDtoSell2.setOperation("sell");
        operationInputDtoSell2.setQuantity(5000);
        operationInputDtoSell2.setUnitCost(new BigDecimal("5.00"));

        OperationInputDto operationInputDtoSell3 = new OperationInputDto();
        operationInputDtoSell3.setOperation("sell");
        operationInputDtoSell3.setQuantity(3000);
        operationInputDtoSell3.setUnitCost(new BigDecimal("20.00"));

        List<OperationOutputDto> operationsOutput = new ArrayList<>();

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoBuy1));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoBuy1));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell2));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell2));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell3));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell3));

        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(0).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(1).getTax());
        Assertions.assertEquals(new BigDecimal("1000.00"), operationsOutput.get(2).getTax());
    }

    @DisplayName("(case #4) - Sell with no gain and loss")
    @Test
    void testSellWithNoGainAndLoss() {
        OperationInputDto operationInputDtoBuy1 = new OperationInputDto();
        operationInputDtoBuy1.setOperation("buy");
        operationInputDtoBuy1.setQuantity(10000);
        operationInputDtoBuy1.setUnitCost(new BigDecimal("10.00"));

        OperationInputDto operationInputDtoBuy2 = new OperationInputDto();
        operationInputDtoBuy2.setOperation("buy");
        operationInputDtoBuy2.setQuantity(5000);
        operationInputDtoBuy2.setUnitCost(new BigDecimal("25.00"));

        OperationInputDto operationInputDtoSell3 = new OperationInputDto();
        operationInputDtoSell3.setOperation("sell");
        operationInputDtoSell3.setQuantity(10000);
        operationInputDtoSell3.setUnitCost(new BigDecimal("15.00"));

        List<OperationOutputDto> operationsOutput = new ArrayList<>();

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoBuy1));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoBuy1));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoBuy2));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoBuy2));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell3));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell3));

        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(0).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(1).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(2).getTax());
    }

    @DisplayName("(case #5) - Sell with no gain or loss, and sell with gain, discounting tax")
    @Test
    void testSellWithNoGainAndLossAndSellWithGainDiscountingTax() {
        OperationInputDto operationInputDtoBuy1 = new OperationInputDto();
        operationInputDtoBuy1.setOperation("buy");
        operationInputDtoBuy1.setQuantity(10000);
        operationInputDtoBuy1.setUnitCost(new BigDecimal("10.00"));

        OperationInputDto operationInputDtoBuy2 = new OperationInputDto();
        operationInputDtoBuy2.setOperation("buy");
        operationInputDtoBuy2.setQuantity(5000);
        operationInputDtoBuy2.setUnitCost(new BigDecimal("25.00"));

        OperationInputDto operationInputDtoSell3 = new OperationInputDto();
        operationInputDtoSell3.setOperation("sell");
        operationInputDtoSell3.setQuantity(10000);
        operationInputDtoSell3.setUnitCost(new BigDecimal("15.00"));

        OperationInputDto operationInputDtoSell4 = new OperationInputDto();
        operationInputDtoSell4.setOperation("sell");
        operationInputDtoSell4.setQuantity(5000);
        operationInputDtoSell4.setUnitCost(new BigDecimal("25.00"));

        List<OperationOutputDto> operationsOutput = new ArrayList<>();

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoBuy1));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoBuy1));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoBuy2));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoBuy2));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell3));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell3));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell4));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell4));

        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(0).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(1).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(2).getTax());
        Assertions.assertEquals(new BigDecimal("10000.00"), operationsOutput.get(3).getTax());
    }

    @DisplayName("(case #6) - Dissolve loss between sales")
    @Test
    void testDissolveLossBetweenSales() {
        OperationInputDto operationInputDtoBuy1 = new OperationInputDto();
        operationInputDtoBuy1.setOperation("buy");
        operationInputDtoBuy1.setQuantity(10000);
        operationInputDtoBuy1.setUnitCost(new BigDecimal("10.00"));

        OperationInputDto operationInputDtoSell2 = new OperationInputDto();
        operationInputDtoSell2.setOperation("sell");
        operationInputDtoSell2.setQuantity(5000);
        operationInputDtoSell2.setUnitCost(new BigDecimal("2.00"));

        OperationInputDto operationInputDtoSell3 = new OperationInputDto();
        operationInputDtoSell3.setOperation("sell");
        operationInputDtoSell3.setQuantity(2000);
        operationInputDtoSell3.setUnitCost(new BigDecimal("20.00"));

        OperationInputDto operationInputDtoSell4 = new OperationInputDto();
        operationInputDtoSell4.setOperation("sell");
        operationInputDtoSell4.setQuantity(2000);
        operationInputDtoSell4.setUnitCost(new BigDecimal("20.00"));

        OperationInputDto operationInputDtoSell5 = new OperationInputDto();
        operationInputDtoSell5.setOperation("sell");
        operationInputDtoSell5.setQuantity(1000);
        operationInputDtoSell5.setUnitCost(new BigDecimal("25.00"));

        List<OperationOutputDto> operationsOutput = new ArrayList<>();

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoBuy1));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoBuy1));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell2));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell2));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell3));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell3));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell4));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell4));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell5));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell5));

        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(0).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(1).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(2).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(3).getTax());
        Assertions.assertEquals(new BigDecimal("3000.00"), operationsOutput.get(4).getTax());
    }

    @DisplayName("(case #7) - Dissolve loss and deduct tax on the same sale")
    @Test
    void testDissolveLossAndDeductTaxOnTheSameSale() {
        OperationInputDto operationInputDtoBuy1 = new OperationInputDto();
        operationInputDtoBuy1.setOperation("buy");
        operationInputDtoBuy1.setQuantity(10000);
        operationInputDtoBuy1.setUnitCost(new BigDecimal("10.00"));

        OperationInputDto operationInputDtoSell2 = new OperationInputDto();
        operationInputDtoSell2.setOperation("sell");
        operationInputDtoSell2.setQuantity(5000);
        operationInputDtoSell2.setUnitCost(new BigDecimal("2.00"));

        OperationInputDto operationInputDtoSell3 = new OperationInputDto();
        operationInputDtoSell3.setOperation("sell");
        operationInputDtoSell3.setQuantity(2000);
        operationInputDtoSell3.setUnitCost(new BigDecimal("20.00"));

        OperationInputDto operationInputDtoSell4 = new OperationInputDto();
        operationInputDtoSell4.setOperation("sell");
        operationInputDtoSell4.setQuantity(2000);
        operationInputDtoSell4.setUnitCost(new BigDecimal("20.00"));

        OperationInputDto operationInputDtoSell5 = new OperationInputDto();
        operationInputDtoSell5.setOperation("sell");
        operationInputDtoSell5.setQuantity(1000);
        operationInputDtoSell5.setUnitCost(new BigDecimal("25.00"));

        OperationInputDto operationInputDtoBuy6 = new OperationInputDto();
        operationInputDtoBuy6.setOperation("buy");
        operationInputDtoBuy6.setQuantity(10000);
        operationInputDtoBuy6.setUnitCost(new BigDecimal("20.00"));

        OperationInputDto operationInputDtoSell7 = new OperationInputDto();
        operationInputDtoSell7.setOperation("sell");
        operationInputDtoSell7.setQuantity(5000);
        operationInputDtoSell7.setUnitCost(new BigDecimal("5.00"));

        OperationInputDto operationInputDtoSell8 = new OperationInputDto();
        operationInputDtoSell8.setOperation("sell");
        operationInputDtoSell8.setQuantity(4350);
        operationInputDtoSell8.setUnitCost(new BigDecimal("30.00"));

        OperationInputDto operationInputDtoSell9 = new OperationInputDto();
        operationInputDtoSell9.setOperation("sell");
        operationInputDtoSell9.setQuantity(650);
        operationInputDtoSell9.setUnitCost(new BigDecimal("30.00"));

        List<OperationOutputDto> operationsOutput = new ArrayList<>();

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoBuy1));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoBuy1));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell2));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell2));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell3));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell3));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell4));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell4));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell5));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell5));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoBuy6));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoBuy6));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell7));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell7));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell8));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell8));

        operationsOutput.add(operationService.getTaxByOperation(operationInputDtoSell9));
        inMemoryOperations.add(OperationConverter.fromDto(operationInputDtoSell9));

        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(0).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(1).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(2).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(3).getTax());
        Assertions.assertEquals(new BigDecimal("3000.00"), operationsOutput.get(4).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(5).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(6).getTax());
        Assertions.assertEquals(new BigDecimal("3050.00"), operationsOutput.get(7).getTax());
        Assertions.assertEquals(new BigDecimal("0.00"), operationsOutput.get(8).getTax());
    }
}
