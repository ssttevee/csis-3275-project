package ca.douglascollege.flamingdodos.realestate.forms;

import javax.swing.*;

public class SellerStatement extends BaseForm {
    private JTextArea commissionText;
    private JButton printButton;
    private JButton closeButton;
    private JPanel contentPane;

    public SellerStatement() {
        super("Seller Statement");

        setContentPane(contentPane);
    }
}
