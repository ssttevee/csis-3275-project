package ca.douglascollege.flamingdodos.realestate.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home extends BaseForm {
    private JButton viewAgentsButton;
    private JButton viewPropertiesButton;
    private JButton revenueReportButton;
    private JPanel contentPane;
    private JButton viewCustomersButton;

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
        viewCustomersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Customers().open();
            }
        });

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        viewPropertiesButton = new JButton();
        viewPropertiesButton.setText("View Properties");
        contentPane.add(viewPropertiesButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        revenueReportButton = new JButton();
        revenueReportButton.setText("Revenue Report");
        contentPane.add(revenueReportButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        viewAgentsButton = new JButton();
        viewAgentsButton.setText("View Agents");
        contentPane.add(viewAgentsButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        viewCustomersButton = new JButton();
        viewCustomersButton.setText("View Customers");
        contentPane.add(viewCustomersButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
