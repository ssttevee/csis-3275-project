package ca.douglascollege.flamingdodos.realestate.forms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home extends BaseForm {
    private JButton viewAgentsButton;
    private JButton viewPropertiesButton;
    private JButton button1;
    private JButton revenueReportButton;
    private JPanel contentPane;

    public Home() {
        setContentPane(contentPane);

        viewAgentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Agents().open();
            }
        });
        viewPropertiesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PropertyList().open();
            }
        });
        revenueReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RevenueReport().open();
            }
        });

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
