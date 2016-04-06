package ca.douglascollege.flamingdodos.realestate.generator;

import ca.douglascollege.flamingdodos.realestate.data.models.SaleTransactionModel;

public class BuyerStatementGenerator extends BaseGenerator {
    public final static double GST_PERCENT = 0.05;

    public BuyerStatementGenerator(SaleTransactionModel txn) {
        double amount = txn.amount;

        addLine().addSegment(-1, "New Century Realty Company");
        addBlankLine();

        addLine().addSegment(-1, "Buyer Statement");
        addBlankLine();

        addLine().addSegment(-8, "Buying Price").addSegment(1, "$").addSegment(3, String.format("%.2f", amount));
        addSeparator();
        addLine().addSegment(-8, "Total").addSegment(1, "$").addSegment(3, String.format("%.2f", amount));
    }

}
