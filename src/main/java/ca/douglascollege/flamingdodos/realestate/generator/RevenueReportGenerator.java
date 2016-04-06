package ca.douglascollege.flamingdodos.realestate.generator;

public class RevenueReportGenerator extends BaseGenerator {

    public RevenueReportGenerator(String month, int year, int agentCount, int txnCount) {
        double agentsTotal = agentCount * 200;
        double txnsTotal = txnCount * 300;

        addLine().addSegment(-1, "New Century Realty Company");
        addBlankLine();

        addLine().addSegment(-1, "Revenue Report for " + month + ", " + year);
        addBlankLine();

        addLine().addSegment(-8, agentCount + " Agents @ $200").addSegment(1, "$").addSegment(3, String.format("%.2f", agentsTotal));
        addLine().addSegment(-8, txnCount + " Deals @ $300").addSegment(1, "$").addSegment(3, String.format("%.2f", txnsTotal));
        addSeparator();
        addLine().addSegment(-8, "Total").addSegment(1, "$").addSegment(3, String.format("%.2f", agentsTotal + txnsTotal));
    }


}
