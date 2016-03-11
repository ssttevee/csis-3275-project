package ca.douglascollege.flamingdodos.realestate.forms;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@RunWith(Theories.class)
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

    @Theory
    public void testButtons(Field f) throws Exception {
        JButton button = (JButton) f.get(mForm);
        button.doClick();
    }

    public static void addFieldsFromClass(Class<?> cls) {
        List<Field> fields = new ArrayList<>();
        for (Field f : cls.getDeclaredFields()) {
            if (f.getType().isAssignableFrom(JButton.class)) {
                f.setAccessible(true);
                fields.add(f);
            }
        }

        buttons = fields.toArray(new Field[fields.size()]);
    }

    @DataPoints
    public static Field[] buttons;
}