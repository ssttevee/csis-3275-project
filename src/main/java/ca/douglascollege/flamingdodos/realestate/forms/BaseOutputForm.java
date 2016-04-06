package ca.douglascollege.flamingdodos.realestate.forms;

import ca.douglascollege.flamingdodos.realestate.generator.BaseGenerator;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public abstract class BaseOutputForm extends BaseForm {
    private BaseGenerator mGenerator;

    public BaseOutputForm() {
        this("");
    }

    public BaseOutputForm(String title) {
        super(title);
    }

    protected BaseGenerator getGenerator() {
        return mGenerator;
    }

    protected void setGenerator(BaseGenerator generator) {
        mGenerator = generator;
    }

    protected void init(JTextArea textArea, BaseGenerator generator) {
        mGenerator = generator;

        textArea.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent evt) {
                updateOutput((JTextArea) evt.getComponent());
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
    }

    protected void updateOutput(JTextArea textArea) {
        mGenerator.setWidth(calcCharsPerLine(textArea));

        textArea.setText(mGenerator.toString());

        pack();
    }

}
