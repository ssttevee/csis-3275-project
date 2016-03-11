package ca.douglascollege.flamingdodos.realestate.forms;

import org.junit.BeforeClass;

public class BuyerStatementFormTest extends BaseFormTest<BuyerStatement> {
    @BeforeClass
    public static void setUpClass() {
        addFieldsFromClass(BuyerStatement.class);
    }

    @Override
    protected BuyerStatement createFormInstance() {
        return new BuyerStatement();
    }
}
