package ca.douglascollege.flamingdodos.realestate.forms;

import javax.swing.*;

public class BuyerStatement extends BaseForm {
    private JTextArea commissionText;
    private JButton printButton;
    private JButton closeButton;
    private JPanel contentPane;

    public BuyerStatement() {
        super("Buyer Statement");

        setContentPane(contentPane);
    }
}
