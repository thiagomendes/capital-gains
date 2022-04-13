package capital.gains.infra.operation.controller;

import capital.gains.infra.controller.OperationTaxCalculationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class OperationTaxCalculationControllerIntTest {

    private MockMvc mockMvc;

    @InjectMocks
    private OperationTaxCalculationController operationTaxCalculationController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(operationTaxCalculationController).build();
    }

    @Test
    void testGetTaxesFromOperationsList() throws Exception {
        String input = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000}]\n";
        String expectedOutput = "[{\"tax\":0.00},{\"tax\":10000.00},{\"tax\":0.00}]";

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/getTaxByOperations")
                                .content(input)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.content().json(expectedOutput))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
