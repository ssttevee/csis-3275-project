package ca.douglascollege.flamingdodos.realestate.generator;

import ca.douglascollege.flamingdodos.realestate.data.models.SaleTransactionModel;

public class CommissionSlipGenerator extends BaseGenerator {

    public CommissionSlipGenerator(SaleTransactionModel txn) {
        double first100k = txn.amount >= 100000 ? 8000 : txn.amount * 0.08;
        double remaining = txn.amount >= 100000 ? 0 : (txn.amount - 100000) * 0.03;
        double gst = (first100k + remaining) * 0.05;

        addLine().addSegment(-1, "New Century Realty Company");
        addBlankLine();

        addLine().addSegment(-1, "Commission Slip");
        addBlankLine();

        addLine().addSegment(-8, "First $100,000 - 8%").addSegment(1, "$").addSegment(3, String.format("%.2f", first100k));

        if (remaining > 0) {
            addLine().addSegment(-8, "Remaining Value - 3%").addSegment(1, "$").addSegment(3, String.format("%.2f", remaining));
        }

        addSeparator();
        addLine().addSegment(-8, "Subtotal").addSegment(1, "$").addSegment(3, String.format("%.2f", first100k + remaining));
        addBlankLine();

        addLine().addSegment(-8, "GST - 5%").addSegment(1, "$").addSegment(3, String.format("%.2f", gst));
        addLine().addSegment(-8, "Deal Fee").addSegment(1, "$").addSegment(3, String.format("%.2f", 300f));
        addSeparator();
        addLine().addSegment(-8, "Total").addSegment(1,"$").addSegment(3, String.format("%.2f", first100k + remaining + gst + 300));
    }

}
