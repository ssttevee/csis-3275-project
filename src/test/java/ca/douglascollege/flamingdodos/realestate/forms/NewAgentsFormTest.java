package ca.douglascollege.flamingdodos.realestate.forms;

import org.junit.BeforeClass;

public class NewAgentsFormTest extends BaseFormTest<NewAgents> {
    @BeforeClass
    public static void setUpClass() {
        addFieldsFromClass(NewAgents.class);
    }

    @Override
    protected NewAgents createFormInstance() {
        return new NewAgents();
    }
}
