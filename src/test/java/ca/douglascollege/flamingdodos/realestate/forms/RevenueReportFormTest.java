package ca.douglascollege.flamingdodos.realestate.forms;

import org.junit.BeforeClass;

public class RevenueReportFormTest extends BaseFormTest<RevenueReport> {
    @BeforeClass
    public static void setUpClass() {
        addFieldsFromClass(RevenueReport.class);
    }

    @Override
    protected RevenueReport createFormInstance() {
        return new RevenueReport();
    }
}
