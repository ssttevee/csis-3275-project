package ca.douglascollege.flamingdodos.realestate.forms;

import ca.douglascollege.flamingdodos.database.exceptions.DatabaseException;
import ca.douglascollege.flamingdodos.database.interfaces.DatabaseQuery;
import ca.douglascollege.flamingdodos.database.interfaces.IDatabaseCursor;
import ca.douglascollege.flamingdodos.database.interfaces.IDatabaseQueryFilter;
import ca.douglascollege.flamingdodos.database.sqlite.util.CompositeFilter;
import ca.douglascollege.flamingdodos.database.sqlite.util.NumericPropertyFilter;
import ca.douglascollege.flamingdodos.database.sqlite.util.StringPropertyFilter;
import ca.douglascollege.flamingdodos.realestate.data.NewCenturyDatabase;
import ca.douglascollege.flamingdodos.realestate.data.models.AgentModel;
import ca.douglascollege.flamingdodos.realestate.data.models.PropertyListingModel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Agents extends BaseForm {
    private JPanel contentPane;
    private JList<AgentModel> agentList;
    private JButton newButton;
    private JButton deleteButton;
    private JButton editButton;
    private JButton closeButton;
    private JLabel hireDateLabel;
    private JLabel soldPropertiesCountLabel;
    private JLabel totalListingsCountLabel;
    private JButton filterButton;
    private JButton listingsButton;
    private JLabel phoneNumberLabel;

    private DatabaseQuery mQuery = new DatabaseQuery();
    private DefaultListModel<AgentModel> agentsListModel;

    public Agents() {
        super("Agents");

        setContentPane(contentPane);

        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                new PropertyInputForm("New Agent")
                        .addProperty(String.class, "First Name")
                        .addProperty(String.class, "Last Name")
                        .addProperty(String.class, "Phone Number")
                        .setResultListener(new PropertyInputForm.ResultListener() {
                            @Override
                            public void onResult(Map<String, Object> result) {
                                AgentModel agentModel = new AgentModel();

                                agentModel.hireDate = new Date(System.currentTimeMillis());
                                agentModel.firstName = (String) result.get("First Name");
                                agentModel.lastName = (String) result.get("Last Name");
                                agentModel.phoneNumber = (String) result.get("Phone Number");

                                try {
                                    NewCenturyDatabase.getInstance().insert(null, agentModel);
                                } catch (DatabaseException e) {
                                    JOptionPane.showMessageDialog(null, "Unable to save new agent");
                                }

                                updateList();
                            }
                        }).open();
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                AgentModel agent = agentList.getSelectedValue();
                new PropertyInputForm(agent.toString() + " - Edit Agent")
                        .addProperty(String.class, "First Name", agent.firstName)
                        .addProperty(String.class, "Last Name", agent.lastName)
                        .addProperty(String.class, "Phone Number", agent.phoneNumber)
                        .setResultListener(new PropertyInputForm.ResultListener() {
                            @Override
                            public void onResult(Map<String, Object> result) {
                                AgentModel agent = agentList.getSelectedValue();

                                agent.firstName = (String) result.get("First Name");
                                agent.lastName = (String) result.get("Last Name");
                                agent.phoneNumber = (String) result.get("Phone Number");

                                try {
                                    NewCenturyDatabase.getInstance().insert(agent.getRowId(), agent);
                                } catch (DatabaseException e) {
                                    JOptionPane.showMessageDialog(null, "Unable to save agent");
                                }

                                updateList();
                            }
                        }).open();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    AgentModel agent = agentList.getSelectedValue();
                    agent.deleted = true;

                    NewCenturyDatabase.getInstance().insert(agent.getRowId(), agent);

                    updateList();
                } catch (DatabaseException e) {
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
                    listingsButton.setEnabled(false);
                } else {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    listingsButton.setEnabled(true);

                    try {
                        AgentModel agent = agentList.getSelectedValue();

                        hireDateLabel.setText(agent.hireDate.toString());

                        int count = 0;
                        int countSold = 0;

                        IDatabaseCursor<PropertyListingModel> properties = NewCenturyDatabase.getInstance().execute(
                                PropertyListingModel.class,
                                new DatabaseQuery().setFilter(
                                        new NumericPropertyFilter(
                                                PropertyListingModel.COLUMN_AGENT_ID,
                                                NumericPropertyFilter.Operator.EQUAL,
                                                agent.getRowId())));

                        while (properties.hasNext()) {
                            count++;
                            if (properties.next().status == PropertyListingModel.PropertyStatus.SOLD) {
                                countSold++;
                            }
                        }

                        phoneNumberLabel.setText(agent.phoneNumber);
                        totalListingsCountLabel.setText("" + count);
                        soldPropertiesCountLabel.setText("" + countSold);
                    } catch (DatabaseException e) {
                        JOptionPane.showMessageDialog(null, "Error reading agent info");
                        e.printStackTrace();
                        close();
                    }

                    pack();
                }
            }
        });

        agentList.setModel(agentsListModel = new DefaultListModel<>());

        listingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PropertyList(agentList.getSelectedValue().toString(), new PropertyList.IPropertyListingFilter() {
                    @Override
                    public IDatabaseQueryFilter getFilter() throws DatabaseException {
                        return new NumericPropertyFilter(PropertyListingModel.COLUMN_AGENT_ID, NumericPropertyFilter.Operator.EQUAL, agentList.getSelectedValue().id);
                    }
                }).open();
            }
        });

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PropertyInputForm("Filter Agents")
                        .addProperty(String.class, "First Name")
                        .addProperty(String.class, "Last Name")
                        .setResultListener(new PropertyInputForm.ResultListener() {
                            @Override
                            public void onResult(Map<String, Object> result) {
                                List<IDatabaseQueryFilter> filters = new ArrayList<>();
                                for (Map.Entry<String, Object> entry : result.entrySet()) {
                                    if (entry.getValue() != null) {
                                        String propName;
                                        switch (entry.getKey()) {
                                            case "First Name":
                                                propName = AgentModel.COLUMN_FIRST_NAME;
                                                break;
                                            case "Last Name":
                                                propName = AgentModel.COLUMN_LAST_NAME;
                                                break;
                                            default:
                                                continue;
                                        }
                                        filters.add(new StringPropertyFilter(propName, StringPropertyFilter.Operator.CONTAINS, (String) entry.getValue()));
                                    }
                                }
                                mQuery = mQuery.setFilter(new CompositeFilter(CompositeFilter.Operator.AND, filters.toArray(new IDatabaseQueryFilter[filters.size()])));
                                updateList();
                            }
                        })
                        .open();
            }
        });

        updateList();
    }

    private void updateList() {
        try {
            List<IDatabaseQueryFilter> filters = new ArrayList<>();

            if (mQuery.getFilter() != null)
                filters.add(mQuery.getFilter());

            filters.add(new NumericPropertyFilter(AgentModel.COLUMN_DELETED, NumericPropertyFilter.Operator.EQUAL, 0));

            IDatabaseCursor<AgentModel> cursor = NewCenturyDatabase.getInstance().execute(AgentModel.class, mQuery.setFilter(
                    new CompositeFilter(CompositeFilter.Operator.AND, filters)));

            agentsListModel.clear();
            while (cursor.hasNext()) {
                AgentModel agent = cursor.next();
                agentsListModel.addElement(agent);
            }
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error attempting to list agents");
            e.printStackTrace();
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
        filterButton = new JButton();
        filterButton.setText("Filter");
        panel2.add(filterButton);
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        closeButton = new JButton();
        closeButton.setText("Close");
        panel1.add(closeButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
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
        panel3.add(spacer2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel3.add(panel5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Total Listings:");
        panel5.add(label2);
        totalListingsCountLabel = new JLabel();
        totalListingsCountLabel.setText("");
        panel5.add(totalListingsCountLabel);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel3.add(panel6, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Sold Properties:");
        panel6.add(label3);
        soldPropertiesCountLabel = new JLabel();
        soldPropertiesCountLabel.setText("");
        panel6.add(soldPropertiesCountLabel);
        listingsButton = new JButton();
        listingsButton.setEnabled(false);
        listingsButton.setText("Listings");
        panel3.add(listingsButton, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel3.add(panel7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Phone #:");
        panel7.add(label4);
        phoneNumberLabel = new JLabel();
        phoneNumberLabel.setText("");
        panel7.add(phoneNumberLabel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
