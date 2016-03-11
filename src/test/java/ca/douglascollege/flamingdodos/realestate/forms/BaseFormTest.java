package ca.douglascollege.flamingdodos.realestate.forms;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.lang.reflect.Field;

public abstract class BaseFormTest<T extends BaseForm> {
    private T mForm;

    protected abstract T createFormInstance();

    @Before
    public void setUp() throws Exception {
        mForm = createFormInstance();
        mForm.open();
    }

    @After
    public void tearDown() throws Exception {
        mForm.close();
    }

    @Test
    public void testButtons() throws Exception {
        for (Field f : getClass().getDeclaredFields()) {
            if (f.getType().isAssignableFrom(JButton.class)) {

                f.setAccessible(true);

                setUp();
                JButton button = (JButton) f.get(mForm);
                button.doClick();
                tearDown();
            }
        }
    }
}