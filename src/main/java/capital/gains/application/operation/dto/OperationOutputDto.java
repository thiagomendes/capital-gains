package capital.gains.application.operation.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OperationOutputDto {

    private BigDecimal tax;

    public OperationOutputDto(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTax() {
        return tax.setScale(2, RoundingMode.HALF_UP);
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
}
