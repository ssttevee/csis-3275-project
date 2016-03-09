package ca.douglascollege.flamingdodos.realestate.forms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Agents extends BaseForm {
    private JPanel contentPane;
    private JList agentList;
    private JButton newButton;
    private JButton deleteButton;
    private JButton editButton;
    private JButton viewButton;
    private JButton closeButton;

    public Agents() {
        super("Agents");

        setContentPane(contentPane);

        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AgentsNew().open();
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SellerStatement().open();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CommissionSlip().open();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new BuyerStatement().open();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Agents.this.dispose();
            }
        });
    }
}
