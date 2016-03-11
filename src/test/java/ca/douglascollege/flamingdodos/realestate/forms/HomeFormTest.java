package ca.douglascollege.flamingdodos.realestate.forms;

import org.junit.BeforeClass;

public class HomeFormTest extends BaseFormTest<Home> {
    @BeforeClass
    public static void setUpClass() {
        addFieldsFromClass(Home.class);
    }

    @Override
    protected Home createFormInstance() {
        return new Home();
    }
}
