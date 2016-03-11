package ca.douglascollege.flamingdodos.realestate.forms;

import org.junit.BeforeClass;

public class SellerStatementFormTest extends BaseFormTest<SellerStatement> {
    @BeforeClass
    public static void setUpClass() {
        addFieldsFromClass(SellerStatement.class);
    }

    @Override
    protected SellerStatement createFormInstance() {
        return new SellerStatement();
    }
}
