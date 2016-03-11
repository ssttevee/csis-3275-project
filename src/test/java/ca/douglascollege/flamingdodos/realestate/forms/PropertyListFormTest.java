package ca.douglascollege.flamingdodos.realestate.forms;

import org.junit.BeforeClass;

public class PropertyListFormTest extends BaseFormTest<PropertyList> {
    @BeforeClass
    public static void setUpClass() {
        addFieldsFromClass(PropertyList.class);
    }

    @Override
    protected PropertyList createFormInstance() {
        return new PropertyList();
    }
}
