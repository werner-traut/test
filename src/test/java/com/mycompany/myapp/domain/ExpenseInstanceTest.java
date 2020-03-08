package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class ExpenseInstanceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpenseInstance.class);
        ExpenseInstance expenseInstance1 = new ExpenseInstance();
        expenseInstance1.setId(1L);
        ExpenseInstance expenseInstance2 = new ExpenseInstance();
        expenseInstance2.setId(expenseInstance1.getId());
        assertThat(expenseInstance1).isEqualTo(expenseInstance2);
        expenseInstance2.setId(2L);
        assertThat(expenseInstance1).isNotEqualTo(expenseInstance2);
        expenseInstance1.setId(null);
        assertThat(expenseInstance1).isNotEqualTo(expenseInstance2);
    }
}
