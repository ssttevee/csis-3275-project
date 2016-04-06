package ca.douglascollege.flamingdodos.realestate.forms;

import ca.douglascollege.flamingdodos.database.sqlite.services.BaseService;
import ca.douglascollege.flamingdodos.realestate.data.NewCenturyDatabase;
import ca.douglascollege.flamingdodos.realestate.data.models.AgentModel;
import ca.douglascollege.flamingdodos.realestate.data.models.PropertyListingModel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.tmatesoft.sqljet.core.SqlJetException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.regex.Pattern;

public class Agents extends BaseForm {
    private JPanel contentPane;
    private JList agentList;
    private JButton newButton;
    private JButton deleteButton;
    private JButton editButton;
    private JButton closeButton;
    private JLabel hireDateLabel;
    private JLabel soldPropertiesCountLabel;
    private JLabel totalListingsCountLabel;

    private BaseService<AgentModel> agentService;

    public Agents() {
        super("Agents");

        setContentPane(contentPane);

        try {
            agentService = NewCenturyDatabase.getInstance().getAgentService();
        } catch (SqlJetException e) {
            JOptionPane.showMessageDialog(null, "Error accessing database.");
            close();
            return;
        }

        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    AgentModel agentModel = agentService.newModel();

                    String name = (String) JOptionPane.showInputDialog(
                            (Component) evt.getSource(),
                            "Enter the name of the new agent.",
                            "New Agent",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            null);

                    if (name != null) {
                        String[] parts = name.split(Pattern.quote(" "));
                        agentModel.hireDate = new Date(System.currentTimeMillis());
                        agentModel.firstName = parts[0];

                        if (parts.length > 1) {
                            agentModel.lastName = parts[1];
                        }

                        agentService.save(agentModel);

                        updateList();
                    }
                } catch (SqlJetException e) {
                    e.printStackTrace();
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    AgentModel agentModel = (AgentModel) agentList.getSelectedValue();

                    String name = (String) JOptionPane.showInputDialog(
                            (Component) evt.getSource(),
                            "Enter the name of the new agent.",
                            "New Agent",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            agentModel.firstName + " " + agentModel.lastName);

                    if (name != null) {
                        String[] parts = name.split(Pattern.quote(" "));
                        agentModel.hireDate = new Date(System.currentTimeMillis());
                        agentModel.firstName = parts[0];

                        if (parts.length > 1) {
                            agentModel.lastName = parts[1];
                        }

                        agentService.save(agentModel);

                        updateList();
                    }
                } catch (SqlJetException e) {
                    e.printStackTrace();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    agentService.delete((AgentModel) agentList.getSelectedValue());
                    updateList();
                } catch (SqlJetException e) {
                    e.printStackTrace();
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        agentList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                if (agentList.getSelectedValue() == null) {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                } else {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);

                    try {
                        BaseService<PropertyListingModel> service = NewCenturyDatabase.getInstance().getPropertyListingService();

                        AgentModel agent = ((AgentModel) agentList.getSelectedValue());

                        hireDateLabel.setText(agent.hireDate.toString());

                        int countSold = 0;

                        PropertyListingModel[] properties = service.lookup("agent_id", agent.getRowId());
                        for (PropertyListingModel property : properties) {
                            if (property.status == PropertyListingModel.PropertyStatus.SOLD) {
                                countSold++;
                            }
                        }

                        totalListingsCountLabel.setText("" + properties.length);
                        soldPropertiesCountLabel.setText("" + countSold);
                    } catch (SqlJetException e) {
                        JOptionPane.showMessageDialog(null, "Error reading agent info");
                        e.printStackTrace();
                    }

                    pack();
                }
            }
        });

        updateList();
    }

    private void updateList() {
        try {
            BaseService<AgentModel> agentService = NewCenturyDatabase.getInstance().getAgentService();
            agentList.setListData(agentService.getAll());
        } catch (SqlJetException e) {
            JOptionPane.showMessageDialog(null, "Error attempting to list agents");
            close();
        }
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
        contentPane.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        agentList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("Laurie Mendoza");
        defaultListModel1.addElement("Morris Spencer");
        defaultListModel1.addElement("Andrew Munoz");
        defaultListModel1.addElement("Francisco Gross");
        defaultListModel1.addElement("Maggie Armstrong");
        defaultListModel1.addElement("Rosalie Torres");
        defaultListModel1.addElement("Beulah Barnett");
        defaultListModel1.addElement("Jamie Barrett");
        defaultListModel1.addElement("Cristina Valdez");
        defaultListModel1.addElement("Megan Nguyen");
        agentList.setModel(defaultListModel1);
        agentList.setSelectionMode(0);
        contentPane.add(agentList, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(250, -1), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        newButton = new JButton();
        newButton.setText("New");
        panel2.add(newButton);
        editButton = new JButton();
        editButton.setEnabled(false);
        editButton.setText("Edit");
        panel2.add(editButton);
        deleteButton = new JButton();
        deleteButton.setEnabled(false);
        deleteButton.setText("Delete");
        panel2.add(deleteButton);
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        closeButton = new JButton();
        closeButton.setText("Close");
        panel1.add(closeButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Hire Date:");
        panel4.add(label1);
        hireDateLabel = new JLabel();
        hireDateLabel.setText("");
        panel4.add(hireDateLabel);
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel3.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Total Listings:");
        panel5.add(label2);
        totalListingsCountLabel = new JLabel();
        totalListingsCountLabel.setText("");
        panel5.add(totalListingsCountLabel);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel3.add(panel6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Sold Properties:");
        panel6.add(label3);
        soldPropertiesCountLabel = new JLabel();
        soldPropertiesCountLabel.setText("");
        panel6.add(soldPropertiesCountLabel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
