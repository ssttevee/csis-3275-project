package ca.douglascollege.flamingdodos.realestate.forms;

import ca.douglascollege.flamingdodos.database.services.BaseService;
import ca.douglascollege.flamingdodos.realestate.data.NewCenturyDatabase;
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

public class PropertyList extends BaseForm {
    private JPanel contentPane;
    private JButton newButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton buyerStatementButton;
    private JButton sellerStatementButton;
    private JButton closeButton;
    private JList list1;
    private JLabel propertyTypeText;
    private JLabel listingPriceText;
    private JPanel propertyOutputPanel;
    private JPanel containerPanel;
    private JButton commissionSlipButton;

    private IPropertyListingDataSource mDataSource;

    public PropertyList() {
        this("", new IPropertyListingDataSource() {
            @Override
            public PropertyListingModel[] getPropertyListings(BaseService<PropertyListingModel> service) throws SqlJetException {
                return service.getAll();
            }
        });

        setTitle("All Property Listings");
    }

    public PropertyList(String name, IPropertyListingDataSource dataSource) {
        super(name + "'s Property Listings");

        setContentPane(contentPane);
        mDataSource = dataSource;

        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                JList source = (JList) e.getSource();
                PropertyListingModel model = (PropertyListingModel) source.getSelectedValue();

                if (model == null) {
                    propertyTypeText.setText("");
                    listingPriceText.setText("");

                    buyerStatementButton.setEnabled(false);
                    sellerStatementButton.setEnabled(false);
                    commissionSlipButton.setEnabled(false);
                    editButton.setEnabled(false);
                    return;
                } else {
                    editButton.setEnabled(true);

                    propertyTypeText.setText(model.propertyType.toString());
                    listingPriceText.setText(String.format("$%.2f", model.askingPrice));
                }

                if (model.status == PropertyListingModel.PropertyStatus.SOLD) {
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
                            NewCenturyDatabase.getInstance().getPropertyListingService().save(listing);
                            updateList();
                            pack();
                        } catch (SqlJetException e) {
                            e.printStackTrace();
                        }
                    }
                }).open();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                new NewPropertyListing((PropertyListingModel) list1.getSelectedValue(), new NewPropertyListing.AddListingCallback() {
                    @Override
                    public void addListing(PropertyListingModel listing) {
                        try {
                            NewCenturyDatabase.getInstance().getPropertyListingService().save(listing);
                            updateList();
                            pack();
                        } catch (SqlJetException e) {
                            e.printStackTrace();
                        }
                    }
                }).open();
            }
        });

        buyerStatementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                PropertyListingModel model = (PropertyListingModel) list1.getSelectedValue();

                try {
                    new BuyerStatement(model.getSaleTransaction(NewCenturyDatabase.getInstance().getSaleTransactionService())).open();
                } catch (SqlJetException e) {
                    e.printStackTrace();
                }
            }
        });

        sellerStatementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                PropertyListingModel model = (PropertyListingModel) list1.getSelectedValue();

                try {
                    new SellerStatement(model.getSaleTransaction(NewCenturyDatabase.getInstance().getSaleTransactionService())).open();
                } catch (SqlJetException e) {
                    e.printStackTrace();
                }
            }
        });

        commissionSlipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                PropertyListingModel model = (PropertyListingModel) list1.getSelectedValue();

                try {
                    new CommissionSlip(model.getSaleTransaction(NewCenturyDatabase.getInstance().getSaleTransactionService())).open();
                } catch (SqlJetException e) {
                    e.printStackTrace();
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        updateList();
    }

    public void updateList() {
        try {
            BaseService<PropertyListingModel> propertyListingService = NewCenturyDatabase.getInstance().getPropertyListingService();

            list1.setListData(mDataSource.getPropertyListings(propertyListingService));
        } catch (SqlJetException e) {
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
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        containerPanel.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Property Type");
        panel4.add(label1);
        propertyTypeText = new JLabel();
        propertyTypeText.setText("");
        panel4.add(propertyTypeText);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel3.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Listing Price:");
        panel5.add(label2);
        listingPriceText = new JLabel();
        listingPriceText.setText("");
        panel5.add(listingPriceText);
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
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    public interface IPropertyListingDataSource {
        PropertyListingModel[] getPropertyListings(BaseService<PropertyListingModel> service) throws SqlJetException;
    }

}
