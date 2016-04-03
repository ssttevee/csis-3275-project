package ca.douglascollege.flamingdodos.realestate.forms;

import org.junit.BeforeClass;

public class CommissionSlipFormTest extends BaseFormTest<CommissionSlip> {
    @BeforeClass
    public static void setUpClass() {
        addFieldsFromClass(CommissionSlip.class);
    }

    @Override
    protected CommissionSlip createFormInstance() {
        return new CommissionSlip(null);
    }
}
