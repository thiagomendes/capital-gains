package capital.gains.application.operation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OperationOutputDto {

    private BigDecimal tax;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    public OperationOutputDto(BigDecimal tax) {
        this.tax = tax;
    }

    public OperationOutputDto(BigDecimal tax, String error) {
        this.tax = tax;
        this.error = error;
    }

    public BigDecimal getTax() {
        return tax.setScale(2, RoundingMode.HALF_UP);
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
