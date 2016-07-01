package ca.douglascollege.flamingdodos.realestate.forms;

import ca.douglascollege.flamingdodos.database.exceptions.DatabaseException;
import ca.douglascollege.flamingdodos.database.interfaces.DatabaseQuery;
import ca.douglascollege.flamingdodos.database.interfaces.IDatabaseCursor;
import ca.douglascollege.flamingdodos.database.interfaces.IDatabaseQueryFilter;
import ca.douglascollege.flamingdodos.database.sqlite.util.CompositeFilter;
import ca.douglascollege.flamingdodos.database.sqlite.util.NumericPropertyFilter;
import ca.douglascollege.flamingdodos.realestate.data.NewCenturyDatabase;
import ca.douglascollege.flamingdodos.realestate.data.models.AgentModel;
import ca.douglascollege.flamingdodos.realestate.data.models.CustomerModel;
import ca.douglascollege.flamingdodos.realestate.data.models.PropertyListingModel;
import ca.douglascollege.flamingdodos.realestate.data.models.SaleTransactionModel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PropertyList extends BaseForm {
    private JPanel contentPane;
    private JButton newButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton buyerStatementButton;
    private JButton sellerStatementButton;
    private JButton closeButton;
    private JList<PropertyListingModel> list1;
    private JLabel propertyTypeText;
    private JLabel listingPriceText;
    private JPanel propertyOutputPanel;
    private JPanel containerPanel;
    private JButton commissionSlipButton;
    private JLabel agentLabel;
    private JLabel customerLabel;

    private DatabaseQuery mQuery = new DatabaseQuery();
    private DefaultListModel<PropertyListingModel> propertyListModel;
    private IPropertyListingFilter mDataSource = null;

    public PropertyList(String name, IPropertyListingFilter dataSource) {
        this(dataSource);

        setTitle(name + "'s Property Listings");
    }

    public PropertyList() {
        this(null);
    }

    public PropertyList(IPropertyListingFilter dataSource) {
        super("All Property Listings");

        setContentPane(contentPane);
        mDataSource = dataSource;

        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                JList source = (JList) evt.getSource();
                PropertyListingModel property = (PropertyListingModel) source.getSelectedValue();

                if (property == null) {
                    propertyTypeText.setText("");
                    listingPriceText.setText("");
                    customerLabel.setText("");
                    agentLabel.setText("");

                    buyerStatementButton.setEnabled(false);
                    sellerStatementButton.setEnabled(false);
                    commissionSlipButton.setEnabled(false);
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    return;
                } else {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);

                    propertyTypeText.setText(property.propertyType.toString());
                    listingPriceText.setText(String.format("$%.2f", property.askingPrice));
                    try {
                        customerLabel.setText(NewCenturyDatabase.getInstance().lookup(CustomerModel.class, property.customerId).toString());
                    } catch (DatabaseException e) {
                        e.printStackTrace();
                    }
                    try {
                        agentLabel.setText(NewCenturyDatabase.getInstance().lookup(AgentModel.class, property.agentId).toString());
                    } catch (DatabaseException e) {
                        e.printStackTrace();
                    }
                }

                if (property.status == PropertyListingModel.PropertyStatus.SOLD) {
                    buyerStatementButton.setEnabled(true);
                    sellerStatementButton.setEnabled(true);
                    commissionSlipButton.setEnabled(true);
                } else {
                    buyerStatementButton.setEnabled(false);
                    sellerStatementButton.setEnabled(false);
                    commissionSlipButton.setEnabled(false);
                }

                pack();
            }
        });

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                new NewPropertyListing(null, new NewPropertyListing.AddListingCallback() {
                    @Override
                    public void addListing(PropertyListingModel listing) {
                        try {
                            NewCenturyDatabase.getInstance().insert(null, listing);
                            updateList();
                            pack();
                        } catch (DatabaseException e) {
                            e.printStackTrace();
                        }
                    }
                }).open();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                new NewPropertyListing(list1.getSelectedValue(), new NewPropertyListing.AddListingCallback() {
                    @Override
                    public void addListing(PropertyListingModel listing) {
                        try {
                            NewCenturyDatabase.getInstance().insert(listing.getRowId(), listing);
                            updateList();
                            pack();
                        } catch (DatabaseException e) {
                            e.printStackTrace();
                        }
                    }
                }).open();
            }
        });

        buyerStatementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                PropertyListingModel model = list1.getSelectedValue();

                try {
                    new BuyerStatement(getRelatedSaleTransaction(model)).open();
                } catch (DatabaseException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(null, "Couldn't find any transactions related to this property...");
                }
            }
        });

        sellerStatementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                PropertyListingModel model = (PropertyListingModel) list1.getSelectedValue();

                try {
                    new SellerStatement(getRelatedSaleTransaction(model)).open();
                } catch (DatabaseException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(null, "Couldn't find any transactions related to this property...");
                }
            }
        });

        commissionSlipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                PropertyListingModel model = (PropertyListingModel) list1.getSelectedValue();

                try {
                    new CommissionSlip(getRelatedAgent(model), getRelatedSaleTransaction(model)).open();
                } catch (DatabaseException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(null, "Couldn't find any transactions related to this property...");
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        list1.setModel(propertyListModel = new DefaultListModel<>());
        updateList();
    }

    private SaleTransactionModel getRelatedSaleTransaction(PropertyListingModel model) throws DatabaseException {
        IDatabaseCursor<SaleTransactionModel> txnsFound = NewCenturyDatabase.getInstance().execute(SaleTransactionModel.class,
                new DatabaseQuery().setFilter(
                        new NumericPropertyFilter(
                                SaleTransactionModel.COLUMN_LISTING_ID,
                                NumericPropertyFilter.Operator.EQUAL,
                                model.id)).setLimit(1));

        if (txnsFound.hasNext()) {
            return txnsFound.next();
        } else {
            return null;
        }
    }

    private AgentModel getRelatedAgent(PropertyListingModel model) throws DatabaseException {
        IDatabaseCursor<AgentModel> txnsFound = NewCenturyDatabase.getInstance().execute(AgentModel.class,
                new DatabaseQuery().setFilter(
                        new NumericPropertyFilter(
                                AgentModel.COLUMN_ID,
                                NumericPropertyFilter.Operator.EQUAL,
                                model.agentId)).setLimit(1));

        if (txnsFound.hasNext()) {
            return txnsFound.next();
        } else {
            return null;
        }
    }

    public void updateList() {
        try {
            List<IDatabaseQueryFilter> filters = new ArrayList<>();

            if (mDataSource != null)
                filters.add(mDataSource.getFilter());

            if (mQuery.getFilter() != null)
                filters.add(mQuery.getFilter());

            filters.add(new NumericPropertyFilter(AgentModel.COLUMN_DELETED, NumericPropertyFilter.Operator.EQUAL, 0));

            propertyListModel.clear();
            IDatabaseCursor<PropertyListingModel> cursor = NewCenturyDatabase.getInstance().execute(PropertyListingModel.class, mQuery.setFilter(new CompositeFilter(CompositeFilter.Operator.AND, filters)));
            while (cursor.hasNext()) {
                propertyListModel.addElement(cursor.next());
            }

            pack();
        } catch (DatabaseException e) {
            e.printStackTrace();
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
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
        containerPanel = new JPanel();
        containerPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(containerPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        list1 = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("Ocean View 1");
        defaultListModel1.addElement("Ocean View 2");
        defaultListModel1.addElement("Suburb 1");
        defaultListModel1.addElement("Suburb 2");
        defaultListModel1.addElement("Property 5");
        defaultListModel1.addElement("Property 6");
        defaultListModel1.addElement("Property 7");
        defaultListModel1.addElement("Property 8");
        defaultListModel1.addElement("Property 9");
        defaultListModel1.addElement("Property 10");
        list1.setModel(defaultListModel1);
        containerPanel.add(list1, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        propertyOutputPanel = new JPanel();
        propertyOutputPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, 0, false, true));
        containerPanel.add(propertyOutputPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        sellerStatementButton = new JButton();
        sellerStatementButton.setEnabled(false);
        sellerStatementButton.setText("Seller Statement");
        propertyOutputPanel.add(sellerStatementButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buyerStatementButton = new JButton();
        buyerStatementButton.setEnabled(false);
        buyerStatementButton.setText("Buyer Statement");
        propertyOutputPanel.add(buyerStatementButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        commissionSlipButton = new JButton();
        commissionSlipButton.setEnabled(false);
        commissionSlipButton.setText("Commission Slip");
        propertyOutputPanel.add(commissionSlipButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        containerPanel.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Property Type:");
        panel4.add(label1);
        propertyTypeText = new JLabel();
        propertyTypeText.setText("");
        panel4.add(propertyTypeText);
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel3.add(panel5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Listing Price:");
        panel5.add(label2);
        listingPriceText = new JLabel();
        listingPriceText.setText("");
        panel5.add(listingPriceText);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel3.add(panel6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Agent:");
        panel6.add(label3);
        agentLabel = new JLabel();
        agentLabel.setText("");
        panel6.add(agentLabel);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel3.add(panel7, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Customer:");
        panel7.add(label4);
        customerLabel = new JLabel();
        customerLabel.setText("");
        panel7.add(customerLabel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    public interface IPropertyListingFilter {
        IDatabaseQueryFilter getFilter() throws DatabaseException;
    }

}
