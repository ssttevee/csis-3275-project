package ca.douglascollege.flamingdodos.realestate.forms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AgentsNew extends BaseForm {
    private JPanel contentPane;
    private JTextField textField1;
    private JButton cancelButton;
    private JButton addAgentButton;

    public AgentsNew() {
        super("New Agent");

        setContentPane(contentPane);

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgentsNew.this.dispose();
            }
        });
    }
}
