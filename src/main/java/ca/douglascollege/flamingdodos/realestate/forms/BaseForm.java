package ca.douglascollege.flamingdodos.realestate.forms;

import javax.swing.*;
import java.awt.*;

public class BaseForm extends JFrame {
    protected static int calcCharsPerLine(JComponent component) {
        return component.getWidth() / component.getFontMetrics(component.getFont()).charWidth(' ');
    }

    public BaseForm() {
        this("");
    }

    public BaseForm(String title) {
        setTitle(title);
        setLocationRelativeTo(null);
    }

    @Override
    public void setTitle(String title) {
        super.setTitle((title.length() > 0 ? title + " - " : "") + "New Century Realty Company");
    }

    @Override
    public void setContentPane(Container contentPane) {
        super.setContentPane(contentPane);
    }

    public void open() {
        pack();
        setVisible(true);
    }

    public void close() {
        dispose();
    }

}
