package ca.douglascollege.flamingdodos.realestate.forms;

import javax.swing.*;

public class CommissionSlip extends BaseForm {
    private JPanel contentPane;
    private JTextArea commissionText;
    private JButton closeButton;
    private JButton printButton;

    public CommissionSlip() {
        super("Commission Slip for {AGENT_NAME}");

        setContentPane(contentPane);
    }
}
