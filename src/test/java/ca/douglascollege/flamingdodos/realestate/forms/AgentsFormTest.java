package ca.douglascollege.flamingdodos.realestate.forms;

import org.junit.BeforeClass;

public class AgentsFormTest extends BaseFormTest<Agents> {
    @BeforeClass
    public static void setUpClass() {
        addFieldsFromClass(Agents.class);
    }

    @Override
    protected Agents createFormInstance() {
        return new Agents();
    }
}
