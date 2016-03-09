package ca.douglascollege.flamingdodos.realestate.forms;

import javax.swing.*;

public class PropertyList extends BaseForm {
    private JPanel contentPane;
    private JButton newButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton buyerStatementButton;
    private JButton sellerStatementButton;
    private JButton closeButton;
    private JList list1;

    public PropertyList() {
        super("Property List");

        setContentPane(contentPane);
    }
}
