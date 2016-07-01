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
import ca.douglascollege.flamingdodos.realestate.data.models.CustomerModel;
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
import java.util.regex.Pattern;

public class Customers extends BaseForm {
    private JPanel contentPane;
    private JList<CustomerModel> customerList;
    private JButton newButton;
    private JButton deleteButton;
    private JButton editButton;
    private JButton closeButton;
    private JButton filterButton;
    private JLabel phoneNumberLabel;

    private DatabaseQuery mQuery = new DatabaseQuery();
    private DefaultListModel<CustomerModel> customersListModel;

    public Customers() {
        super("Customers");

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
                                CustomerModel customer = new CustomerModel();

                                customer.firstName = (String) result.get("First Name");
                                customer.lastName = (String) result.get("Last Name");
                                customer.phoneNumber = (String) result.get("Phone Number");

                                try {
                                    NewCenturyDatabase.getInstance().insert(null, customer);
                                } catch (DatabaseException e) {
                                    JOptionPane.showMessageDialog(null, "Unable to save new customer");
                                }

                                updateList();
                            }
                        }).open();
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                CustomerModel customer = customerList.getSelectedValue();
                new PropertyInputForm(customer.toString() + " - Edit Agent")
                        .addProperty(String.class, "First Name", customer.firstName)
                        .addProperty(String.class, "Last Name", customer.lastName)
                        .addProperty(String.class, "Phone Number", customer.phoneNumber)
                        .setResultListener(new PropertyInputForm.ResultListener() {
                            @Override
                            public void onResult(Map<String, Object> result) {
                                CustomerModel customer = customerList.getSelectedValue();

                                customer.firstName = (String) result.get("First Name");
                                customer.lastName = (String) result.get("Last Name");
                                customer.phoneNumber = (String) result.get("Phone Number");

                                try {
                                    NewCenturyDatabase.getInstance().insert(customer.getRowId(), customer);
                                } catch (DatabaseException e) {
                                    JOptionPane.showMessageDialog(null, "Unable to save customer");
                                }

                                updateList();
                            }
                        }).open();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    NewCenturyDatabase.getInstance().delete(CustomerModel.class, customerList.getSelectedValue().getRowId());
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

        customerList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                if (customerList.getSelectedValue() == null) {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                } else {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);

                    CustomerModel customer = customerList.getSelectedValue();

                    phoneNumberLabel.setText(customer.phoneNumber);

                    pack();
                }
            }
        });

        customerList.setModel(customersListModel = new DefaultListModel<>());

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

            filters.add(new NumericPropertyFilter(CustomerModel.COLUMN_DELETED, NumericPropertyFilter.Operator.EQUAL, 0));

            IDatabaseCursor<CustomerModel> cursor = NewCenturyDatabase.getInstance().execute(CustomerModel.class, mQuery.setFilter(
                    new CompositeFilter(CompositeFilter.Operator.AND, filters)));

            customersListModel.clear();
            while (cursor.hasNext()) {
                CustomerModel customer = cursor.next();
                customersListModel.addElement(customer);
            }

            pack();
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Error attempting to list customers");
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
        customerList = new JList();
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
        customerList.setModel(defaultListModel1);
        customerList.setSelectionMode(0);
        contentPane.add(customerList, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(250, -1), null, 0, false));
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
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Phone #:");
        panel4.add(label1);
        phoneNumberLabel = new JLabel();
        phoneNumberLabel.setText("");
        panel4.add(phoneNumberLabel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
