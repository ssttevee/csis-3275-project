package ca.douglascollege.flamingdodos.realestate.generator;

import ca.douglascollege.flamingdodos.realestate.data.models.SaleTransactionModel;

public class SellerStatementGenerator extends BaseGenerator {

    public SellerStatementGenerator(SaleTransactionModel txn) {
        double agentFee = txn.getAgentFee();
        double tax = agentFee * 0.05;

        addLine().addSegment(-1, "New Century Realty Company");
        addBlankLine();

        addLine().addSegment(-1, "Seller Statement");
        addBlankLine();

        addLine().addSegment(-8, "Selling Price").addSegment(1, "$").addSegment(3, String.format("%.2f", txn.amount));
        addLine().addSegment(-8, "Agent Fee").addSegment(1, "$").addSegment(3, String.format("%.2f", -agentFee));
        addLine().addSegment(-8, "GST - 5% of Agent Fee").addSegment(1, "$").addSegment(3, String.format("%.2f", -tax));
        addSeparator();
        addLine().addSegment(-8, "Total").addSegment(1, "$").addSegment(3, String.format("%.2f", txn.amount - agentFee - tax));
    }

}
