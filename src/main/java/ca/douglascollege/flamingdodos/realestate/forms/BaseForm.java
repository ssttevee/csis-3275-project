package ca.douglascollege.flamingdodos.realestate.forms;

import javax.swing.*;
import java.awt.*;

public class BaseForm extends JFrame {
    public BaseForm() {
        this("");
    }

    public BaseForm(String title) {
        super((title.length() > 0 ? title + " - " : "") + "New Century Realty Company");

        setLocationRelativeTo(null);
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
